package com.example.demo.handler;

import com.example.demo.service.UserService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserHandler {
  private final UserService service = new UserService();

  public void create(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();
    ctx.json(service.create(body));
  }

  public void getById(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    ctx.json(service.getById(id));
  }

  public void update(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    JsonObject body = ctx.body().asJsonObject();
    ctx.json(service.update(id, body));
  }

  public void delete(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    ctx.json(service.delete(id));
  }
}
