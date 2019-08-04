package com.danielprinz.udemy.ping_pong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class FuturesExample extends AbstractVerticle {

  @Override
  public void start(final Future<Void> startFuture) throws Exception {
    System.out.println("1 - Start");
    Future<Void> whenTimer1Fired = Future.future();
    vertx.setTimer(1000, id -> {
      System.out.println("3 - Timer fired");
      whenTimer1Fired.complete();
    });
    Future<Void> whenTimer2Fired = Future.future();
    vertx.setTimer(2000, id -> {
      System.out.println("4 - Second Timer fired");
      whenTimer2Fired.complete();
    });
    CompositeFuture.all(whenTimer1Fired, whenTimer2Fired).setHandler(ar -> {
      System.out.println("5 - Both timers fired");
      startFuture.complete();
    });
    System.out.println("2 - Timer Execution Called");
  }

  @Override
  public void stop(final Future<Void> stopFuture) throws Exception {
    System.out.println("Stop");
  }

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FuturesExample());
  }
}
