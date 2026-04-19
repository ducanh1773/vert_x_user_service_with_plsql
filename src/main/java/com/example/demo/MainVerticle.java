package com.example.demo;

import com.example.demo.handler.UserHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new MainVerticle());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    UserHandler handler = new UserHandler();

    router.post("/users").handler(handler::create);
    router.get("/users/:id").handler(handler::getById);
    router.put("/users/:id").handler(handler::update);
    router.delete("/users/:id").handler(handler::delete);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(9992);
  }
}
