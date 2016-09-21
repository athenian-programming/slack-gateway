package com.sudothought.gateway;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ConfigInfo {

  private final ConcurrentHashMap<String, String> configVals = new ConcurrentHashMap<>();

  private final Config config;

  public ConfigInfo() {
    this.config = ConfigFactory.load();
  }

  public String getConfigString(final String key) {
    return this.configVals.computeIfAbsent(key,
                                           val -> {
                                             final String tokenVal = new ProcessBuilder().environment().get(val);
                                             return tokenVal != null ? tokenVal : config.getString(val);
                                           });
  }

  public boolean isValid(final String requestToken) {
    if (Strings.isNullOrEmpty(requestToken))
      throw new IllegalArgumentException("Request missing Slack token");

    final String slackToken = this.getConfigString("slack.token");
    return Splitter.on(",")
                   .trimResults()
                   .omitEmptyStrings()
                   .splitToList(slackToken)
                   .stream()
                   .anyMatch(val -> "*".equals(val) || requestToken.equals(val));
  }
}
