package reactive.demo;

import io.vertx.core.json.JsonObject;

import reactive.demo.grpc.Point;

/**
 * There must be a better way of doing mapping.
 */
public class Utils {

    public static JsonObject toJsonObject(Point p) {
        return new JsonObject().put("color", p.getColor()).put("drawing", p.getDrawing()).put("x", p.getX()).put("y", p.getY()).put("radius", p.getRadius());
    }

    public static Point toGrpcPoint(JsonObject j) {
        return Point.newBuilder().setColor(j.getString("color")).setDrawing(j.getInteger("drawing")).setX(j.getInteger("x")).setY(j.getInteger("y")).setRadius(j.getInteger("radius")).build();
    }

    public static JsonObject positionToPoint(Object position) {
        return ((JsonObject) position).put("radius", 5).put("color", "#666666");
    }

}
