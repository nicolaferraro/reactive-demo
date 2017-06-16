package reactive.demo;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import reactive.demo.grpc.Point;

public class MyAwesomeStuff {

    private static Map<Integer, Long> lastTimestamp = new ConcurrentHashMap<>();

    public static List<Point> execute(List<Point> sourceList) {

        if (sourceList.size() > 10) {
            int drawing = sourceList.get(0).getDrawing();

            if (canDraw(drawing)) {
                List<Point> filtered = sourceList.stream().filter(p -> p.getDrawing() == drawing).collect(Collectors.toList());

                if (filtered.size() > 10) {
                    Point center = center(filtered);

                    int minDistance = minDistance(filtered, center);
                    int maxDistance = maxDistance(filtered, center);
                    int meanDistance = meanDistance(filtered, center);
                    int startEndDistance = distance(filtered.get(0), filtered.get(filtered.size() - 1));

                    int tolerance = meanDistance / 3;
                    if (startEndDistance < 15 && meanDistance - minDistance < tolerance && maxDistance - meanDistance < tolerance) {
                        lastTimestamp.put(drawing, System.currentTimeMillis());
                        return produceImage(center, meanDistance);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean canDraw(int drawing) {
        Long last = lastTimestamp.get(drawing);
        if (last == null) {
            last = 0L;
        }
        long now = System.currentTimeMillis();
        return now - last > 2000;
    }

    private static List<Point> produceImage(Point center, int meanDistance) {
        int radius = 6;
        int shift = -meanDistance - 20;
        String color = "#000022";
        double step = Math.PI / meanDistance;

        List<Point> result = new LinkedList<>();
        for (double angle = 0; angle <= 2 * Math.PI; angle += step) {
            result.add(Point.newBuilder()
                    .setX(shift + (int)(center.getX() + Math.cos(angle) * meanDistance))
                    .setY(shift + (int)(center.getY() + Math.sin(angle) * meanDistance))
                    .setColor(color)
                    .setRadius(radius)
                    .build());
        }

        for (double angle = (Math.PI/10); angle <= Math.PI - (Math.PI/10); angle += step) {
            result.add(Point.newBuilder()
                    .setX(shift + (int)(center.getX() + Math.cos(angle) * (meanDistance / 2)))
                    .setY(shift + (int)(center.getY() + Math.sin(angle) * (meanDistance / 2)))
                    .setColor(color)
                    .setRadius(radius)
                    .build());
        }

        result.add(Point.newBuilder()
                .setX(shift + center.getX() - meanDistance * 2 / 5)
                .setY(shift + center.getY() - meanDistance * 2 / 5)
                .setColor(color)
                .setRadius(radius)
                .build());

        result.add(Point.newBuilder()
                .setX(shift + center.getX() + meanDistance * 2 / 5)
                .setY(shift + center.getY() - meanDistance * 2 / 5)
                .setColor(color)
                .setRadius(radius)
                .build());

        return result;
    }

    private static int maxDistance(List<Point> source, Point other) {
        return source.stream().map(p -> distance(p, other)).max(Comparator.naturalOrder()).get();
    }

    private static int minDistance(List<Point> source, Point other) {
        return source.stream().map(p -> distance(p, other)).min(Comparator.naturalOrder()).get();
    }

    private static int meanDistance(List<Point> source, Point other) {
        long maxDistance = source.stream().map(p -> (long)distance(p, other)).reduce((d1, d2) -> d1 + d2).get();
        return (int)(maxDistance / source.size());
    }

    private static int distance(Point p1, Point p2) {
        return (int) Math.sqrt(((long)p1.getX() - p2.getX())*(p1.getX() - p2.getX()) + ((long)p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
    }

    private static Point center(List<Point> points) {
        int xMin = points.stream().map(Point::getX).min(Comparator.naturalOrder()).get();
        int xMax = points.stream().map(Point::getX).max(Comparator.naturalOrder()).get();

        int yMin = points.stream().map(Point::getY).min(Comparator.naturalOrder()).get();
        int yMax = points.stream().map(Point::getY).max(Comparator.naturalOrder()).get();

        return Point.newBuilder().setX((xMin + xMax) / 2).setY((yMin + yMax) / 2).build();
    }

}
