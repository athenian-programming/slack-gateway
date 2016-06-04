package com.sudothought;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ParticleServices {
  @FormUrlEncoded
  @POST("v1/devices/photon1/led")
  Call<ParticleResponse> setLed(@Field("access_token") String accessToken, @Field("params") String params);
}
