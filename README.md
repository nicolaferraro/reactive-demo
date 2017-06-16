# Reactive Demo

This demo shows how it's possible to use Apache Camel to route event streams in a system composed of
a Vert.x microservice, a Spring-Boot 2 (M1) WebFlux service and a gRPC streaming endpoint.
 
## Running the demo

All three applications have a main that starts the application locally.

Once the applications are started, connect to [http://localhost:8080/web/](http://localhost:8080/web/)
to start drawing.

## About

Inspired by [quickdraw.withgoogle.com](https://quickdraw.withgoogle.com/).