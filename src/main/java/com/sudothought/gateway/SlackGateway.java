package com.sudothought.gateway;

import com.google.common.base.Splitter;
import com.sudothought.gateway.particle.ParticleDevice;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import spark.Route;
import spark.Service;

import java.util.concurrent.ConcurrentHashMap;

public class SlackGateway {

  public static void main(final String[] args) {
    Service service = Service.ignite();

    final SlackGateway slackGateway = new SlackGateway(service);

    final ParticleDevice particleDevice = new ParticleDevice(slackGateway);
  }

  private final ConcurrentHashMap<String, String> configVals = new ConcurrentHashMap<>();

  private final Config  config;
  private final Service spark;

  public SlackGateway(final Service spark) {
    this.config = ConfigFactory.load();
    this.spark = spark;

    final String port = new ProcessBuilder().environment().get("PORT");
    this.spark.port(port != null ? Integer.parseInt(port) : 8080);
  }

  public void addMapping(final String path, final Route route) {
    this.spark.get(path, route);
    this.spark.post(path, route);
  }

  public boolean isValid(final String requestToken) {
    final String slackToken = this.getConfigString("slack.token");
    return Splitter.on(",")
                   .trimResults()
                   .omitEmptyStrings()
                   .splitToList(slackToken)
                   .stream()
                   .anyMatch(val -> "*".equals(val) || requestToken.equals(val));
  }

  public String getConfigString(final String key) {
    return this.configVals
        .computeIfAbsent(key,
                         val -> {
                           final String tokenVal = new ProcessBuilder().environment().get(val);
                           return tokenVal != null ? tokenVal : config.getString(val);
                         });
  }
}
