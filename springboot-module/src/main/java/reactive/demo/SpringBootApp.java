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

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

/**
 * @author nicola
 * @since 08/06/2017
 */
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
            return points.scan(Tuples.<Point, Point>of(null, null), (prev, point) -> Tuples.of(prev.getT2(), point))
                    .filter(t -> t.getT1() != null)
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
