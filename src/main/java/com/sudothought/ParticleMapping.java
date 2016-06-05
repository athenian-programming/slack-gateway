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
    final ParticleServices particle = retrofit.create(ParticleServices.class);
    final String particleToken = gateway.getConfigString("particle.token");
    final String deviceName = gateway.getConfigString("device.name");


    gateway.addMapping("/led",
                       (req, res) -> {
                         final SlackRequest slackRequest = new SlackRequest(req);

                         if (!gateway.isValid(slackRequest.getToken()))
                           return format("Invalid Slack token: %s", slackRequest.getToken());

                         final String arg = slackRequest.getText();

                         if (Strings.isNullOrEmpty(arg)) {
                           final Response<ParticleGetResponse> resp = particle.getLed(deviceName, particleToken).execute();
                           if (!resp.isSuccessful())
                             return format("Error: %s", resp.message());

                           final ParticleGetResponse getResponse = resp.body();
                           return getResponse.isConnected() ? format("LED is %s",
                                                                     getResponse.getResult().equals("1") ? "on" : "off")
                                                            : "Device not connected";
                         }

                         switch (arg) {
                           case "on":
                           case "off":
                             final Response<ParticleSetResponse> resp = particle.setLed(deviceName, particleToken, arg).execute();
                             if (!resp.isSuccessful())
                               return format("Error: %s", resp.message());

                             final ParticleSetResponse setResponse = resp.body();
                             return setResponse.isConnected() ? format("Turned %s LED", arg) : "Device not connected";

                           case "debug":
                             LOGGER.info("Values: " + slackRequest);
                             return "Values: " + slackRequest;

                           default:
                             return format("Invalid argument: '%s'. Usage: /led [on/off]", arg);
                         }
                       });
  }
}
