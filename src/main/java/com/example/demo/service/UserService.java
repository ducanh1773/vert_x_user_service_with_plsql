package com.example.demo.service;

import com.example.demo.repository.sql.UserRepository;
import io.vertx.core.json.JsonObject;

public class UserService {
  private final UserRepository repo = new UserRepository();

  public JsonObject create(JsonObject data) {
    return repo.insert(data);
  }

  public JsonObject getById(int id) {
    return repo.get(id);
  }

  public JsonObject update(int id, JsonObject data) {
    return repo.update(id, data);
  }

  public JsonObject delete(int id) {
    return repo.delete(id);
  }
}
