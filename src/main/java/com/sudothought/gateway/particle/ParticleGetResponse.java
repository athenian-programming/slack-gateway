package com.sudothought.gateway.particle;

public class ParticleGetResponse {

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

  public ParticleGetResponse() { }

  public String getResult() { return this.result; }

  public boolean isConnected() { return this.coreInfo.connected; }
}
