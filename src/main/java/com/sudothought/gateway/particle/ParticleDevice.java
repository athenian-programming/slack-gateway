package com.sudothought.gateway.particle;

import com.google.common.base.Strings;
import com.sudothought.gateway.SlackGateway;
import com.sudothought.gateway.SlackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

public class ParticleDevice {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParticleDevice.class);
  private static final String USAGE  = "Usage: /particle [on/off/value]";

  public ParticleDevice(final SlackGateway gateway) {
    final String url = gateway.getConfigString("particle.url");
    final Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
    final ParticleServices api = retrofit.create(ParticleServices.class);
    final String particleToken = gateway.getConfigString("particle.token");
    final String deviceName = gateway.getConfigString("particle.device.name");

    gateway.addMapping("/particle",
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
                               final Response<ParticleSetResponse> set = api.setLed(deviceName, particleToken, arg).execute();
                               if (!set.isSuccessful())
                                 return format("Error: %s", set.message());

                               final ParticleSetResponse setResponse = set.body();
                               return setResponse.isConnected() ? format("Turned %s Particle LED", arg)
                                                                : "Particle device not connected";

                             case "value":
                               final Response<ParticleGetResponse> get = api.getLed(deviceName, particleToken).execute();
                               if (!get.isSuccessful())
                                 return format("Error: %s", get.message());

                               final ParticleGetResponse getResponse = get.body();
                               return getResponse.isConnected() ? format("Particle LED is %s",
                                                                         getResponse.getResult().equals("1") ? "on" : "off")
                                                                : "Particle device not connected";

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
