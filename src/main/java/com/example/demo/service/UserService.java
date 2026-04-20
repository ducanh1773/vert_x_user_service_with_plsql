package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.sql.UserRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Future<JsonArray> getAllUsers() {
    return userRepository.findAll().map(JsonArray::new);
  }

  public Future<JsonObject> getById(int id){
    return userRepository.findById(id);
  }

  public Future<JsonObject> createUser(Users users){
    return userRepository.createUser(users);
  }
}
