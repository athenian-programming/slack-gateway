package com.sudothought.gateway.particle;

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

import static java.lang.String.format;

public class ParticleDevice
    implements RouteSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParticleDevice.class);
  private static final String USAGE  = "Usage: /%s [on/off/value]";

  private final ConfigInfo       configInfo;
  private final String           particleToken;
  private final ParticleServices api;

  public ParticleDevice(final ConfigInfo configInfo) {
    this.configInfo = configInfo;
    this.particleToken = this.configInfo.getConfigString("particle.token");

    final String url = this.configInfo.getConfigString("particle.url");
    final Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
    this.api = retrofit.create(ParticleServices.class);
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
                final Response<ParticleSetResponse> set = this.api.setLed(name, particleToken, arg).execute();
                if (!set.isSuccessful())
                  return format("Particle server error: %s", set.message());

                final ParticleSetResponse setResponse = set.body();
                return setResponse.isConnected() ? format("Turned %s Particle LED", arg)
                                                 : "Particle device not connected";

              case "value":
                final Response<ParticleGetResponse> get = this.api.getLed(name, particleToken).execute();
                if (!get.isSuccessful())
                  return format("Particle server error: %s", get.message());

                final ParticleGetResponse getResponse = get.body();
                return getResponse.isConnected() ? format("Particle LED is %s",
                                                          getResponse.getResult().equals("1") ? "on" : "off")
                                                 : "Particle device not connected";

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
