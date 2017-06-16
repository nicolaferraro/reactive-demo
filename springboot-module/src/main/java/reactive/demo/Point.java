package reactive.demo;


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
