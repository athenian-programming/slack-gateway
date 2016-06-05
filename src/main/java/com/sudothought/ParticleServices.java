package com.sudothought;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ParticleServices {
  @FormUrlEncoded
  @POST("v1/devices/photon1/setLed")
  Call<ParticleSetResponse> setLed(@Field("access_token") String accessToken, @Field("params") String params);

  @GET("v1/devices/photon1/getLed")
  Call<ParticleGetResponse> getLed(@Query("access_token") String accessToken);
}
