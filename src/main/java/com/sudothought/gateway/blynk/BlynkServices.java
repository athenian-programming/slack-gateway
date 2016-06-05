package com.sudothought.gateway.blynk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BlynkServices {
  @Headers("Content-Type: application/json")
  @PUT("{auth_token}/pin/{pin}")
  Call<Void> setPin(@Path("auth_token") String authToken,
                    @Path("pin") String pin,
                    @Body() String value);

  @GET("{auth_token}/pin/{pin}")
  Call<String[]> getPin(@Path("auth_token") String authToken,
                        @Path("pin") String pin);

  @GET("{auth_token}/isHardwareConnected")
  Call<Boolean> isHardwareConnected(@Path("auth_token") String authToken);
}
