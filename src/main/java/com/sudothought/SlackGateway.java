package com.sudothought;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class SlackGateway {

  private static final Logger LOGGER = LoggerFactory.getLogger(SlackGateway.class);

  public static void main(final String[] args) {
    port(getHerokuAssignedPort());
    get("/hello", (req, res) -> "Hello Heroku World");


    final Route ledRoute = (req, res) -> {
      final SlackRequest slackRequest = new SlackRequest(req);
      if ("debug".equalsIgnoreCase(slackRequest.getText())) {
        LOGGER.info("Values: " + slackRequest);
        return "Values: " + slackRequest;
      }

      return "Values: " + slackRequest;
    };
    get("/led", ledRoute);
    post("/led", ledRoute);
  }

  private static int getHerokuAssignedPort() {
    final ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 8080;
  }


}
