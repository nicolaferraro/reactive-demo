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

/**
 * @author nicola
 * @since 08/06/2017
 */
public class Point {

    private int drawing;

    private String color;

    private int radius;

    private int x;

    private int y;

    public Point() {
    }

    public Point(int drawing, String color, int radius, int x, int y) {
        this.drawing = drawing;
        this.color = color;
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public int getDrawing() {
        return drawing;
    }

    public void setDrawing(int drawing) {
        this.drawing = drawing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("drawing=").append(drawing);
        sb.append(", color='").append(color).append('\'');
        sb.append(", radius=").append(radius);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
