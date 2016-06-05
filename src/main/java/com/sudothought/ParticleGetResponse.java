package com.sudothought;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticleGetResponse {
  @JsonProperty
  private String   cmd      = null;
  @JsonProperty
  private String   name     = null;
  @JsonProperty
  private String   result   = null;
  @JsonProperty
  private CoreInfo coreInfo = null;

  public String getResult() { return this.result; }

  public boolean isConnected() { return this.coreInfo.connected; }

  public class CoreInfo {
    @JsonProperty
    private String  last_app          = null;
    @JsonProperty
    private String  last_heard        = null;
    @JsonProperty
    private boolean connected         = true;
    @JsonProperty
    private String  last_handshake_at = null;
    @JsonProperty
    private String  deviceID          = null;
    @JsonProperty
    private String  product_id        = null;
  }
}
