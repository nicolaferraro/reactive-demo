package reactive.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

    @RestController
    @RequestMapping("/fill-the-gaps")
    static class Web {

        @PostMapping
        public Flux<Point> enhance(@RequestBody Flux<Point> points) {
            Point dummy = new Point(-1, "dummy", -1, -1, -1);
            return points.scan(Tuples.of(dummy, dummy), (prev, point) -> Tuples.of(prev.getT2(), point))
                    .filter(t -> !t.getT1().equals(dummy))
                    .flatMapIterable(twoPoints -> Arrays.asList(
                            twoPoints.getT1(),
                            mean(twoPoints.getT1(), twoPoints.getT2()),
                            twoPoints.getT2()));
        }

        private Point mean(Point p1, Point p2) {
            return new Point(p1.getDrawing(), "#FF0000" /* RED */, Math.max(1, p1.getRadius() - 2), (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY())/2);
        }

    }

}
