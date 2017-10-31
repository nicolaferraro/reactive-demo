package reactive.demo;

import io.reactivex.Flowable;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.streams.Pump;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.reactivestreams.ReactiveReadStream;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.reactivex.FlowableHelper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.toolbox.AggregationStrategies;
import reactive.demo.grpc.Point;

import java.util.LinkedList;
import java.util.List;

public class VertxApp {

    private static final Logger LOG = LoggerFactory.getLogger(VertxApp.class);

    public static void main(String[] args) throws Exception {

        VertxOptions options = new VertxOptions();
        //options.setBlockedThreadCheckInterval(1000*60*60);

        Vertx vertx = Vertx.vertx(options);
        CamelContext camel = new DefaultCamelContext();
        CamelReactiveStreamsService rxCamel = CamelReactiveStreams.get(camel);
        configure(camel);

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        server.requestHandler(router::accept);
        router.route("/web/*").handler(StaticHandler.create("web"));
        router.route("/eventbus/*").handler(eventBusHandler(vertx));

        ReactiveReadStream<Object> points = ReactiveReadStream.readStream();

        FlowableHelper.toFlowable(vertx.eventBus().consumer("draw.positions").bodyStream())
                .map(Utils::pointToCircle)
                .subscribe(rxCamel.streamSubscriber("raw-points", JsonObject.class));

        Flowable.fromPublisher(rxCamel.fromStream("enhanced-points", JsonObject.class))
                .subscribe(points);

        camel.start();
        Pump.pump(points, vertx.eventBus().publisher("draw.points")).start();
        server.listen(8080);
    }

    private static void configure(CamelContext camel) throws Exception {

        new RouteBuilder() {
            @Override
            public void configure() throws Exception {



                from("reactive-streams:raw-points")
                        .wireTap("direct:another-one")
                        .wireTap("direct:fill-the-gap")
                        .to("seda:output");


                from("direct:fill-the-gap")
                        .setHeader("drawing").body(JsonObject.class, j -> j.getInteger("drawing"))
                            .aggregate(header("drawing"))
                            .aggregationStrategy(AggregationStrategies.flexible().accumulateInCollection(LinkedList.class))
                            .completionTimeout(50)
                        .transform().body(List.class, l -> new JsonArray(l).toString())
                        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .to("netty4-http:http://localhost:8181/fill-the-gaps?useRelativePath=true")
                        .transform().body(String.class, JsonArray::new)
                        .split().body()
                        .to("seda:output");


                from("direct:another-one")
                        .transform().body(JsonObject.class, Utils::toPoint)
                        .to("grpc://localhost:8282/reactive.demo.grpc.Images?method=Enhance&producerStrategy=STREAMING&streamRepliesTo=seda:grpc-stream");

                from("seda:grpc-stream")
                        .transform().body(Point.class, Utils::toJsonObject)
                        .to("seda:output");


                from("seda:output")
                        .to("reactive-streams:enhanced-points");


            }
        }.addRoutesToCamelContext(camel);

    }


    private static SockJSHandler eventBusHandler(Vertx vertx) {
        BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddressRegex("draw\\..+"))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("draw\\..+"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                LOG.info("A socket was created");
            }
            if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                LOG.info("connection closed: {}");
            }
            event.complete(true);
        });
    }

}
