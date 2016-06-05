package com.sudothought;

import com.google.common.base.MoreObjects;
import spark.Request;

public class SlackRequest {

  private final String token;
  private final String teamId;
  private final String teamDomain;
  private final String channelId;
  private final String channelName;
  private final String userId;
  private final String userName;
  private final String command;
  private final String text;
  private final String responseUrl;

  public SlackRequest(final Request req) {
    this.token = req.queryParams("token");
    this.teamId = req.queryParams("team_id");
    this.teamDomain = req.queryParams("team_domain");
    this.channelId = req.queryParams("channel_id");
    this.channelName = req.queryParams("channel_name");
    this.userId = req.queryParams("user_id");
    this.userName = req.queryParams("user_name");
    this.command = req.queryParams("command");
    this.text = req.queryParams("text");
    this.responseUrl = req.queryParams("response_url");
  }

  public String getToken() { return this.token; }

  public String getTeamId() { return this.teamId; }

  public String getTeamDomain() { return this.teamDomain; }

  public String getChannelId() { return this.channelId; }

  public String getChannelName() { return this.channelName; }

  public String getUserId() { return this.userId; }

  public String getUserName() { return this.userName; }

  public String getCommand() { return this.command; }

  public String getText() { return this.text; }

  public String getResponseUrl() { return responseUrl; }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
                      .add("team_id", this.getTeamId())
                      .add("teamDomain", this.getTeamDomain())
                      .add("channel_id", this.getChannelId())
                      .add("channel_name", this.getChannelName())
                      .add("user_id", this.getUserId())
                      .add("user_name", this.getUserName())
                      .add("command", this.getCommand())
                      .add("text", this.getText())
                      .add("response_url", this.getResponseUrl())
                      .toString();
  }
}
