package com.example.demo.EventBus;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class UserEventBus extends AbstractVerticle {
  private final UserService userService;

  public UserEventBus(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void start() {
    vertx.eventBus().consumer("user.create", message -> {
      JsonObject body = (JsonObject) message.body();


      // Chuyển Json sang Model Users
      Users user = new Users(
        body.getInteger("id"),
        body.getString("username"),
        body.getString("email")
      );

      // Gọi Service xử lý
      userService.createUser(user)
        .onSuccess(message::reply) // Trả kết quả về cho Handler
        .onFailure(err -> message.fail(500, err.getMessage()));
    });

    // THÊM MỚI: Consumer cho Update
    vertx.eventBus().consumer("user.update", message -> {
      JsonObject body = (JsonObject) message.body();

      // Chuyển Json sang Model Users (giả sử bạn update dựa trên ID)
      Users user = new Users(
        body.getInteger("id"),
        body.getString("username"),
        body.getString("email")
      );

      // Gọi Service xử lý update
      userService.updateUser(user)
        .onSuccess(result -> message.reply(result))
        .onFailure(err -> message.fail(500, err.getMessage()));
    });


    vertx.eventBus().consumer("user.find.all.by.plsql", message -> {
      userService.getAllByProcedure()
        .onSuccess(result -> {
          // Bắt buộc phải chuyển List thành JsonArray trước khi reply
          message.reply(new JsonArray(result));
        })
        .onFailure(err -> message.fail(500, err.getMessage()));
    });


  }
}
