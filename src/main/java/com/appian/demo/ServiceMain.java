package com.appian.demo;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ServiceMain {
  final static Logger LOG = Logger.getLogger(ServiceMain.class);

  public static final void main(String args[]) throws Exception {
    BasicConfigurator.configure();

    Logger.getRootLogger().setLevel(Level.INFO);

    Vertx vertx = Vertx.vertx();

    DeploymentOptions options = new DeploymentOptions();

    vertx.deployVerticle(Vertical::new, options);
  }

}
