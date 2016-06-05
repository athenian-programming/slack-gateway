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
  private static final String USAGE  = "Usage: /led [on/off/value]";

  public ParticleMapping(final SlackGateway gateway) {
    final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.particle.io/")
                                                    .addConverterFactory(JacksonConverterFactory.create())
                                                    .build();
    final ParticleServices particle = retrofit.create(ParticleServices.class);
    final String particleToken = gateway.getConfigString("particle.token");
    final String deviceName = gateway.getConfigString("device.name");


    gateway.addMapping("/led",
                       (req, res) -> {
                         try {
                           final SlackRequest slackRequest = new SlackRequest(req);

                           if (!gateway.isValid(slackRequest.getToken()))
                             return format("Invalid Slack token: %s", slackRequest.getToken());

                           final String arg = slackRequest.getText();

                           if (Strings.isNullOrEmpty(arg))
                             return format("Missing argument. %s", USAGE);

                           switch (arg) {
                             case "on":
                             case "off":
                               final Response<ParticleSetResponse> set = particle.setLed(deviceName, particleToken, arg).execute();
                               if (!set.isSuccessful())
                                 return format("Error: %s", set.message());

                               final ParticleSetResponse setResponse = set.body();
                               return setResponse.isConnected() ? format("Turned %s LED", arg) : "Device not connected";

                             case "value":
                               final Response<ParticleGetResponse> get = particle.getLed(deviceName, particleToken).execute();
                               if (!get.isSuccessful())
                                 return format("Error: %s", get.message());

                               final ParticleGetResponse getResponse = get.body();
                               return getResponse.isConnected() ? format("LED is %s",
                                                                         getResponse.getResult().equals("1") ? "on" : "off")
                                                                : "Device not connected";

                             case "debug":
                               final String msg = format("Request values: %s", slackRequest);
                               LOGGER.info(msg);
                               return msg;

                             default:
                               return format("Invalid argument: '%s'. %s", arg, USAGE);
                           }
                         }
                         catch (Throwable e) {
                           final String msg = format("%s - %s", e.getClass().getSimpleName(), e.getMessage());
                           LOGGER.warn(msg);
                           return msg;
                         }
                       });
  }
}
