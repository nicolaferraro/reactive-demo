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

import io.vertx.core.json.JsonObject;

import reactive.demo.grpc.Point;

/**
 * There must be a better way of doing this.
 */
public class Utils {

    public static JsonObject toJsonObject(Point p) {
        return new JsonObject().put("color", p.getColor()).put("drawing", p.getDrawing()).put("x", p.getX()).put("y", p.getY()).put("radius", p.getRadius());
    }

    public static Point toPoint(JsonObject j) {
        return Point.newBuilder().setColor(j.getString("color")).setDrawing(j.getInteger("drawing")).setX(j.getInteger("x")).setY(j.getInteger("y")).setRadius(j.getInteger("radius")).build();
    }

    public static JsonObject pointToCircle(Object point) {
        return ((JsonObject) point).put("radius", 5).put("color", "#666666");
    }

}
