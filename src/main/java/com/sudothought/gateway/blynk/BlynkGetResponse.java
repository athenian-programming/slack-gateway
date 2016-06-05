package com.sudothought.gateway.blynk;

public class BlynkGetResponse {
  public class CoreInfo {
    private String  last_app          = null;
    private String  last_heard        = null;
    private boolean connected         = true;
    private String  last_handshake_at = null;
    private String  deviceID          = null;
    private String  product_id        = null;

    public CoreInfo() { }
  }

  private String   cmd      = null;
  private String   name     = null;
  private String   result   = null;
  private CoreInfo coreInfo = null;

  public BlynkGetResponse() { }

  public String getResult() { return this.result; }

  public boolean isConnected() { return this.coreInfo.connected; }
}
