package com.sudothought.gateway.blynk;

import com.google.common.base.Strings;
import com.sudothought.gateway.SlackGateway;
import com.sudothought.gateway.SlackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

public class BlynkDevice {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlynkDevice.class);
  private static final String USAGE  = "Usage: /blynk [on/off/value]";
  private static final String PIN    = "D0";

  public BlynkDevice(final SlackGateway gateway) {
    final String url = gateway.getConfigString("blynk.url");
    final Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
    final BlynkServices api = retrofit.create(BlynkServices.class);
    final String blynkToken = gateway.getConfigString("blynk.token");
    final String deviceName = gateway.getConfigString("blynk.device.name");

    gateway.addMapping("/blynk",
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
                               final int pinValue = arg.equals("on") ? 1 : 0;
                               final Response<BlynkSetResponse> set = api.setPin(blynkToken, PIN, pinValue).execute();
                               if (!set.isSuccessful())
                                 return format("Error: %s", set.message());

                               final BlynkSetResponse setResponse = set.body();
                               return setResponse.isConnected() ? format("Turned %s Blynk LED", PIN)
                                                                : "Blynk device not connected";

                             case "value":
                               final Response<BlynkGetResponse> get = api.getPin(deviceName, blynkToken).execute();
                               if (!get.isSuccessful())
                                 return format("Error: %s", get.message());

                               final BlynkGetResponse getResponse = get.body();
                               return getResponse.isConnected() ? format("Blynk LED is %s",
                                                                         getResponse.getResult().equals("1") ? "on" : "off")
                                                                : "Blynk device not connected";

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
