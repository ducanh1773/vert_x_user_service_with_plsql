package com.example.demo;

import com.example.demo.handler.UserHandler;
import com.example.demo.repository.sql.UserRepository;
import com.example.demo.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) {

    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(3306)
      .setHost("127.0.0.1")
      .setDatabase("vertx_demo")
      .setUser("root")
      .setPassword("123456");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    MySQLPool pool = MySQLPool.pool(vertx, connectOptions, poolOptions);

    UserRepository repo = new UserRepository(pool);
    UserService service = new UserService(repo);
    UserHandler handler = new UserHandler(service);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/api/users").handler(handler::findAll);
    router.get("/api/users/:id").handler(handler::findById);

    // gọi hàm qua eventBus
    router.post("/api/users").handler(handler::createUser);
    router.post("/api/users/update").handler(handler::updateUser);
    vertx.deployVerticle(new com.example.demo.EventBus.UserEventBus(service));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(9092)
      .onSuccess(ok -> startPromise.complete())
      .onFailure(startPromise::fail);
  }
}

