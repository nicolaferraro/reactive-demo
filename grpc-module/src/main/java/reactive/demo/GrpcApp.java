/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package reactive.demo;

import java.time.Duration;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import reactive.demo.grpc.ImagesGrpc;
import reactive.demo.grpc.Point;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

/**
 * @author nicola
 * @since 09/06/2017
 */
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

            TopicProcessor<Point> processor = TopicProcessor.create();

            Flux<Point> main = processor.map(point -> Point.newBuilder(point).setColor("#777799").setX(point.getX() + 20).setY(point.getY() + 20).build());


            Flux<Point> secondary = processor
                    .window(Duration.ofSeconds(4), Duration.ofSeconds(1))
                    .flatMap(Flux::buffer)
                    .map(MyAwesomeStuff::execute)
                    .flatMapIterable(s -> s);


            main.mergeWith(secondary).subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

            return new StreamObserver<Point>() {
                @Override
                public void onNext(Point point) {
                    processor.onNext(point);
                }

                @Override
                public void onError(Throwable throwable) {
                    processor.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    processor.onComplete();
                }
            };
        }

    }

}
