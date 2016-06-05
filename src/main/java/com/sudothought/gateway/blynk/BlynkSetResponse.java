package com.sudothought.gateway.blynk;

public class BlynkSetResponse {
  private String  id           = null;
  private String  last_app     = null;
  private boolean connected    = true;
  private String  return_value = null;

  public BlynkSetResponse() { }

  public boolean isConnected() { return this.connected; }
}
