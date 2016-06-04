package com.sudothought;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import spark.Route;
import spark.Service;

public class SlackGateway {

  public static void main(final String[] args) {
    Service service = Service.ignite();
    service.port(getHerokuAssignedPort());
    final SlackGateway slackGateway = new SlackGateway(service);
    final ParticleMapping particleMapping = new ParticleMapping(slackGateway);
  }

  private final Config  config;
  private final Service spark;

  public SlackGateway(final Service spark) {
    this.config = ConfigFactory.load();
    this.spark = spark;
  }

  public Config getConfig() { return this.config; }

  public void addMapping(final String path, final Route route) {
    this.spark.get(path, route);
    this.spark.post(path, route);
  }

  private static int getHerokuAssignedPort() {
    final String port = new ProcessBuilder().environment().get("PORT");
    return port != null ? Integer.parseInt(port) : 8080;
  }
}
