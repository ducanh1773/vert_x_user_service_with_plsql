package com.example.demo.repository.sql;

import com.example.demo.model.Users;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Tuple;
import io.vertx.core.Future;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
  private final MySQLPool client;

  public UserRepository(MySQLPool client) {
    this.client = client;
  }

  public Future<List<JsonObject>> findAll() {
    return client.query("SELECT * FROM user_vertx").execute()
      .map(rows -> {
        List<JsonObject> list = new LinkedList<>();
        rows.forEach(row -> list.add(row.toJson()));
        return list;
      });
  }

  public Future<JsonObject> findById(int id) {
    return client
      .preparedQuery("SELECT * FROM user_vertx WHERE id = ?")
      .execute(Tuple.of(id))
      .map(rows -> {
        if (rows.size() > 0) {
          return rows.iterator().next().toJson();
        }
        return new JsonObject();
      });
  }

  public Future<JsonObject> createUser(Users users) {
    return client
      .preparedQuery("INSERT INTO user_vertx (id , username , email) VALUES (? , ? , ?)")
      .execute(Tuple.of(users.getId(), users.getUserName(), users.getEmail()))
      .map(rows -> {
        return new JsonObject()
          .put("id", users.getId())
          .put("username", users.getUserName())
          .put("email", users.getEmail())
          .put("message", "User created successfully");
      });
  }

  public Future<JsonObject> updateUser(Users users) {
    return client
      .preparedQuery("UPDATE user_vertx SET username = ? , email = ? WHERE id = ?")
      .execute(Tuple.of(users.getUserName(), users.getEmail(), users.getId()))
      .map(rows -> {
        return new JsonObject()
          .put("id", users.getId())
          .put("username", users.getUserName())
          .put("email", users.getEmail())
          .put("message", "User update successfully");
      });
  }

  public Future<List<JsonObject>> getAllByProcedure(){
    return client.query("CALL get_all_users()")
      .execute()
      .map(rows -> {
        List<JsonObject> list = new LinkedList<>();
        rows.forEach(row -> list.add(row.toJson()));
        return list;
      });
  }



}
