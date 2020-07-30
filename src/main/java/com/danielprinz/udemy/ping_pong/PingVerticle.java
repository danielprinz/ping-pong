package com.danielprinz.udemy.ping_pong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * In Vert.x 4 the first event loop is pinned to the non Vert.x thread.
 * https://github.com/vert-x3/wiki/wiki/4.0.0-Deprecations-and-breaking-changes#%EF%B8%8F-context-affinity-for-non-vertx-thread
 */
public class PingVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println("Ping - Running on event loop: " + Thread.currentThread().getName());
    vertx.setPeriodic(1000, id -> {
      sendPing();
      //publishPing();
    });
  }

  private void publishPing() {
    vertx.eventBus().publish("ping-pong", new JsonObject().put("msg", "published-ping"));
  }

  private void sendPing() {
    vertx.eventBus().request("ping-pong", new JsonObject().put("msg", "ping"), ar -> {
      if (ar.failed()) {
        System.err.println("Did not receive a response: " + ar.cause());
        return;
      }
      System.out.println(ar.result().body());
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle());
    vertx.deployVerticle(PongVerticle.class, new DeploymentOptions().setInstances(4));
  }
}
