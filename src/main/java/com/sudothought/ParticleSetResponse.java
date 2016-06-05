package com.sudothought;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticleSetResponse {
  @JsonProperty
  private String  id           = null;
  @JsonProperty
  private String  last_app     = null;
  @JsonProperty
  private boolean connected    = true;
  @JsonProperty
  private String  return_value = null;

  public String getId() { return this.id; }

  public String getLastApp() { return this.last_app; }

  public boolean isConnected() { return this.connected; }

  public String getReturnValue() { return this.return_value; }
}
