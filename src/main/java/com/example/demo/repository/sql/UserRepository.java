package com.example.demo.repository.sql;

import io.vertx.core.json.JsonObject;

public class UserRepository {
  public JsonObject insert(JsonObject data) {
    // insert DB
    return new JsonObject().put("msg", "created");
  }

  public JsonObject get(int id) {
    return new JsonObject().put("id", id).put("username", "test");
  }

  public JsonObject update(int id, JsonObject data) {
    return new JsonObject().put("msg", "updated");
  }

  public JsonObject delete(int id) {
    return new JsonObject().put("msg", "deleted");
  }
}
