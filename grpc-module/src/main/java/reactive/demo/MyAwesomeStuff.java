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


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import reactive.demo.grpc.Point;

/**
 * @author nicola
 * @since 12/06/2017
 */
public class MyAwesomeStuff {


    public static List<Point> execute(List<Point> source) {
//        if (Math.random() >= 0.8) {
//            return source.stream().map(point -> Point.newBuilder(point).setColor("#000000").setX(point.getX() + 80).build()).collect(Collectors.toList());
//        } else {
//            return Collections.emptyList();
//        }
        return Collections.emptyList();
    }

}
