package com.sudothought.gateway.blynk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BlynkServices {
  @Multipart
  @PUT("{auth_token}/pin/{pin}")
  Call<BlynkSetResponse> setPin(@Path("auth_token") String authToken,
                                @Path("pin") String pin,
                                @Part("value") int value);

  @GET("{auth_token}/pin/{pin}")
  Call<BlynkGetResponse> getPin(@Path("auth_token") String authToken,
                                @Path("pin") String pin);
}
