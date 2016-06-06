package com.sudothought.gateway;

import com.sudothought.gateway.blynk.BlynkDevice;
import com.sudothought.gateway.particle.ParticleDevice;
import spark.Service;

public class SlackGateway {

  public static void main(final String[] args) {
    final ConfigInfo configInfo = new ConfigInfo();
    final ParticleDevice particleDevice = new ParticleDevice(configInfo);
    final BlynkDevice blynkDevice = new BlynkDevice(configInfo);

    final SlackGateway gateway = new SlackGateway();
    gateway.map("particle", particleDevice, "photon1")
           .map("blynk", blynkDevice, "D5");
  }

  private final Service spark;

  public SlackGateway() {
    this.spark = Service.ignite();

    final String port = new ProcessBuilder().environment().get("PORT");
    this.spark.port(port != null ? Integer.parseInt(port) : 8080);
  }

  public SlackGateway map(final String command, final RouteSource routeSource, final String name) {
    this.spark.get("/" + command, routeSource.getRoute(name, command));
    this.spark.post("/" + command, routeSource.getRoute(name, command));
    return this;
  }
}
