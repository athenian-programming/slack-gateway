package com.sudothought.gateway.blynk;

import com.google.common.base.Strings;
import com.sudothought.gateway.ConfigInfo;
import com.sudothought.gateway.RouteSource;
import com.sudothought.gateway.SlackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spark.Route;

import java.io.IOException;

import static java.lang.String.format;

public class BlynkDevice
    implements RouteSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlynkDevice.class);
  private static final String USAGE  = "Usage: /%s [on/off/value]";

  private final ConfigInfo    configInfo;
  private final String        blynkToken;
  private final String        url;
  private final BlynkServices api;

  public BlynkDevice(final ConfigInfo configInfo) {
    this.configInfo = configInfo;
    this.blynkToken = this.configInfo.getConfigString("blynk.token");

    this.url = this.configInfo.getConfigString("blynk.url");
    final Retrofit retrofit = new Retrofit.Builder().baseUrl(this.url)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
    this.api = retrofit.create(BlynkServices.class);
  }

  @Override
  public Route getRoute(final String name, final String command) {
    return
        (req, res) -> {
          try {
            final SlackRequest slackRequest = new SlackRequest(req);

            if (!this.configInfo.isValid(slackRequest.getToken()))
              return format("Invalid Slack token: %s", slackRequest.getToken());

            final String arg = slackRequest.getText();

            if (Strings.isNullOrEmpty(arg))
              return format("Missing argument. %s", format(USAGE, command));

            switch (arg) {
              case "on":
              case "off":
                this.verifyDeviceIsConnected();

                final String[] pinValue = new String[] {arg.equals("on") ? "1" : "0"};
                final Response<Void> set = this.api.setPin(this.blynkToken, name, pinValue).execute();

                if (!set.isSuccessful())
                  throw new BlynkException(format("Blynk server error: %s", set.message()));

                return format("Turned %s Blynk %s", arg, name);

              case "value":
                this.verifyDeviceIsConnected();

                final Response<String[]> get = this.api.getPin(this.blynkToken, name).execute();
                if (!get.isSuccessful())
                  throw new BlynkException(format("Blynk server error: %s", get.message()));

                final String[] getResponse = get.body();
                return format("Blynk %s is %s", name, getResponse[0].equals("1") ? "on" : "off");

              case "debug":
                final String msg = format("Request values: %s", slackRequest);
                LOGGER.info(msg);
                return msg;

              default:
                return format("Invalid argument: '%s'. %s", arg, format(USAGE, command));
            }
          }
          catch (Throwable e) {
            final String msg = format("%s - %s", e.getClass().getSimpleName(), e.getMessage());
            LOGGER.warn(msg);
            return msg;
          }
        };
  }

  private void verifyDeviceIsConnected()
      throws IOException, BlynkException {
    final Response<Boolean> connected = this.api.isHardwareConnected(this.blynkToken).execute();
    if (!connected.isSuccessful())
      throw new BlynkException(format("Blynk server error: %s", connected.message()));
    if (!connected.body())
      throw new BlynkException("Blynk device not connected");
  }
}
