//package com.example.demo.verticle;
//
//import io.vertx.core.AbstractVerticle;
//
//public class UserConsumerVerticle extends AbstractVerticle {
//  @Override
//  public void start() {
//    UserEventHandler handler = new UserEventHandler();
//
//    vertx.eventBus().consumer(EventBusAddress.CREATE_USER, handler::handleCreate);
//    vertx.eventBus().consumer(EventBusAddress.Get_User, handler::handleGet);
//  }
//}
