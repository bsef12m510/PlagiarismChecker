package com.prepostseo.plagchecker.plans.restInterface;

import com.prepostseo.plagchecker.plans.response.UpgradeUserResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface UpgradeService {

    @FormUrlEncoded
    @POST("app/upgradeuser")
    Call<UpgradeUserResponse> upgradeUser(@Field("key") String key, @Field("packageName") String packageName, @Field("subscriptionId") String subscriptionId, @Field("token") String token);
}
