package com.sudothought;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.lang.String.format;

public class ParticleMapping {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParticleMapping.class);

  public ParticleMapping(final SlackGateway gateway) {
    final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.particle.io/")
                                                    .addConverterFactory(JacksonConverterFactory.create())
                                                    .build();
    final ParticleServices particleServices = retrofit.create(ParticleServices.class);
    final String tokenVal = new ProcessBuilder().environment().get("particle.token");
    final String particleToken = tokenVal != null ? tokenVal : gateway.getConfig().getString("particle.token");

    gateway.addMapping("/led",
                       (req, res) -> {
                         final SlackRequest slackRequest = new SlackRequest(req);
                         final String arg = slackRequest.getText();

                         if (Strings.isNullOrEmpty(arg))
                           return "Missing argument. Usage: /led [on/off]";

                         switch (arg) {
                           case "on":
                           case "off":
                             final Response<ParticleResponse> response = particleServices.setLed(particleToken, arg).execute();
                             return response.isSuccessful() ? format("Turned %s LED", arg) : "Error: " + response.message();
                           case "debug":
                             LOGGER.info("Values: " + slackRequest);
                             return "Values: " + slackRequest;
                         }

                         return format("Invalid argument: '%s'. Usage: /led [on/off]", arg);
                       });
  }
}
