package com.sudothought;

import static spark.Spark.get;
import static spark.Spark.port;

public class SlackGateway {

  public static void main(String[] args) {
    port(getHerokuAssignedPort());
    get("/hello", (req, res) -> "Hello Heroku World");
  }

  private static int getHerokuAssignedPort() {
    final ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 8080;
  }
}
