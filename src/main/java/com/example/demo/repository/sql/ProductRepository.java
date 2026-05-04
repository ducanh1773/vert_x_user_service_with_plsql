package com.example.demo.repository.sql;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductRepository {
  private final MySQLPool client;

  public ProductRepository(MySQLPool client) {
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

  public Future<List<JsonObject>> getById(Integer id){
    return client.preparedQuery("SELECT * FROM product").execute()
      .map(rows -> {
        List<JsonObject> list = new LinkedList<>();
        rows.forEach(row -> list.add(row.toJson()));
        return list;
      });
  }

  public Future<List<JsonObject>> create(){
    return client.preparedQuery("INSERT INTO product values (? , ? ,?)").execute()
      .map(rows -> {
        List<JsonObject> list = new LinkedList<>();
        rows.forEach(row -> list.add(row.toJson()));
        return list;
      });
  }



}
