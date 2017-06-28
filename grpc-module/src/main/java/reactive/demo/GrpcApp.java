package reactive.demo;

import java.time.Duration;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import reactive.demo.grpc.ImagesGrpc;
import reactive.demo.grpc.Point;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class GrpcApp {

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8282)
                .addService(new ImagesService())
                .build()
                .start();


        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown();
            }
        });

        server.awaitTermination();
    }

    private static class ImagesService extends ImagesGrpc.ImagesImplBase {

        @Override
        public StreamObserver<Point> enhance(StreamObserver<Point> responseObserver) {

            TopicProcessor<Point> input = TopicProcessor.create();

            Flux<Point> main = input.map(point -> Point.newBuilder(point).setColor("#777799").setX(point.getX() + 20).setY(point.getY() + 20).build());


            Flux<Point> secondary = input
                    .window(Duration.ofSeconds(4), Duration.ofSeconds(1))
                    .flatMap(Flux::buffer)
                    .map(MyAwesomeStuff::execute)
                    .flatMapIterable(s -> s);


            main.mergeWith(secondary).subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

            return new StreamObserver<Point>() {
                @Override
                public void onNext(Point point) {
                    input.onNext(point);
                }

                @Override
                public void onError(Throwable throwable) {
                    input.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    input.onComplete();
                }
            };
        }

    }

}
