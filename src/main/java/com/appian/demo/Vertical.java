package com.appian.demo;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import com.google.common.net.MediaType;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Vertical extends AbstractVerticle implements AutoCloseable {
  private static final Logger LOG = Logger.getLogger(Vertical.class);

  @Override
  public void start(Future<Void> future) {

    Router router = Router.router(vertx);
    router.get("/metric").handler(this::metric);

    int port = 6001;
    vertx.createHttpServer().requestHandler(router::accept).listen(
      port,
      result -> {
        if (result.succeeded()) {
          LOG.info("Demo service is now listening on :" + port);
          future.complete();
        } else {
          future.fail(result.cause());
        }
      }
    );
  }

  @Override
  public void close() {
    this.vertx.close();
  }

  private void metric(RoutingContext routingContext) {
    LOG.info("Got metric request");

    HttpServerResponse response = routingContext.response();
    try {
      long metric = 2;
      LOG.info("Metric response: " + metric);
      String jsonStr = Json.mapper.writeValueAsString(metric);
      sendJsonResponse(response, jsonStr);
    } catch (Throwable t) {
      LOG.error("Failed to send response", t);
      sendError(response, t);
    }
  }

  private static void sendTextResponse(HttpServerResponse response, String payload) {
    response.setStatusCode(HttpStatus.SC_OK)
      .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.PLAIN_TEXT_UTF_8.toString())
      .end(payload);
  }

  private static void sendJsonResponse(HttpServerResponse response, String payload) {
    response.setStatusCode(HttpStatus.SC_OK)
      .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
      .end(payload);
  }

  private static void sendError(HttpServerResponse response, Throwable t) {
    response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
      .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.PLAIN_TEXT_UTF_8.toString())
      .end(t.toString());
  }
}
