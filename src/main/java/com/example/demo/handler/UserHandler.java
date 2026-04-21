package com.example.demo.handler;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static java.lang.System.err;

public class UserHandler {
  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  public void findAll(RoutingContext routingContext) {
    userService.getAllUsers()
      .onSuccess(data -> {
        System.out.println("Lấy dữ liệu thành công: " + data.encodePrettily());
        routingContext.json(data);
      })
      .onFailure(err -> {
        err.printStackTrace(); // In lỗi ra màn hình đen (Console) để xem báo gì
        routingContext.fail(500, err);
      });
  }

  public void getAllByProcedure(RoutingContext ctx) {
    ctx.vertx().eventBus().<JsonArray>request("user.find.all.by.plsql", new JsonObject(), reply -> {
      if (reply.succeeded()) {
        ctx.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(200)
          .end(reply.result().body().encode());
      } else {
        ctx.response()
          .setStatusCode(500)
          .putHeader("content-type", "application/json")
          .end(new JsonObject().put("error", reply.cause().getMessage()).encode());
      }
    });

  }

  public void findById(RoutingContext ctx) {
    String idParam = ctx.pathParam("id");

    if (idParam == null) {
      ctx.fail(400);
      return;
    }
    int id;
    try {
      id = Integer.parseInt(ctx.pathParam("id"));
    } catch (NumberFormatException e) {
      ctx.response().setStatusCode(400).end("ID phải là số nguyên!");
      return;
    }
    userService.getById(id)
      .onSuccess(user -> {
        if (user.isEmpty()) {
          ctx.response().setStatusCode(400).end(new JsonObject().put("message", "User not found").encode());
        } else {
          ctx.json(user);
        }
      })
      .onFailure(err -> {
        err.printStackTrace(); // In lỗi ra Console IntelliJ
        ctx.response()
          .setStatusCode(500)
          .end("Lỗi chi tiết: " + err.getMessage());
      });
  }

  public void createUser(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();
    if (body == null) {
      ctx.response().setStatusCode(400).end("Invalid Json Body");
      return;
    }

    // Gửi message qua Event Bus thay vì gọi Service
    ctx.vertx().eventBus().<JsonObject>request("user.create", body, reply -> {
      if (reply.succeeded()) {
        ctx.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(201)
          .end(reply.result().body().encode());
      } else {
        ctx.response()
          .setStatusCode(500)
          .end(new JsonObject().put("error", reply.cause().getMessage()).encode());
      }
    });

  }

  public void updateUser(RoutingContext ctx){
    JsonObject body = ctx.body().asJsonObject();
    if(body == null){
      ctx.response().setStatusCode(400).end("Invalid Json Body");
      return;
    }
    // Gửi message sang Event Bus
    ctx.vertx().eventBus().<JsonObject>request("user.update" , body , reply ->{
      if(reply.succeeded()){
        ctx.response()
          .putHeader("content-type" , "application/json")
          .setStatusCode(201)
          .end(reply.result().body().encode());
      }else {
        ctx.response()
          .setStatusCode(500)
          .end(new JsonObject().put("error", reply.cause().getMessage()).encode());
      }
    });
  }

  public void getByIdByProcedure(RoutingContext ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));

      ctx.vertx().eventBus().<JsonObject>request("user.find.by.id.plsql", id, reply -> {
        if (reply.succeeded()) {
          JsonObject body = reply.result().body();
          if (body.isEmpty()) {
            ctx.response().setStatusCode(404).end("User not found");
          } else {
            ctx.response()
              .putHeader("content-type", "application/json")
              .end(body.encode());
          }
        } else {
          ctx.fail(500, reply.cause());
        }
      });
    } catch (NumberFormatException e) {
      ctx.response().setStatusCode(400).end("Invalid ID format");
    }
  }

  public void handleSaveUser(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();

    ctx.vertx().eventBus().<JsonObject>request("user.save.plsql", body, reply -> {
      if (reply.succeeded()) {
        ctx.response()
          .setStatusCode(200)
          .putHeader("content-type", "application/json")
          .end(reply.result().body().encode());
      } else {
        ctx.fail(500, reply.cause());
      }
    });
  }


}
