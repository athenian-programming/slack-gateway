package com.sudothought.gateway.blynk;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.sudothought.gateway.ConfigInfo;
import com.sudothought.gateway.RouteSource;
import com.sudothought.gateway.SlackRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spark.Route;

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
                final Gson gson = new Gson();
                final String pinValue = arg.equals("on") ? gson.toJson(new String[] {"1"})
                                                         : gson.toJson(new String[] {"0"});

                final MediaType mediaType = MediaType.parse("application/json");
                final RequestBody body = RequestBody.create(mediaType, pinValue);
                final Request request = new Request.Builder().url(format("%s/%s/pin/%s", this.url, this.blynkToken, name))
                                                             .put(body)
                                                             .build();
                okhttp3.Response set = new OkHttpClient().newCall(request).execute();

                //final Response<Void> set = this.api.setPin(this.blynkToken, name, pinValue).execute();

                if (!set.isSuccessful())
                  return format("Blynk server error: %s", set.message());

                /*
                final BlynkSetResponse setResponse = set.body();
                return setResponse.isConnected() ? format("Turned %s Blynk LED", name)
                                                 : "Blynk device not connected";
                                                 */
                return format("Turned %s Blynk %s", arg, name);

              case "value":
                final Response<String[]> get = this.api.getPin(this.blynkToken, name).execute();
                if (!get.isSuccessful())
                  return format("Blynk server error: %s", get.message());

                final String[] getResponse = get.body();
                final boolean connected = true;
                return connected ? format("Blynk %s is %s", name, getResponse[0].equals("1") ? "on" : "off")
                                 : "Blynk device not connected";

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
}
