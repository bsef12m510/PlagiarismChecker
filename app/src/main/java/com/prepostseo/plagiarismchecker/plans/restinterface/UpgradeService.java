package com.prepostseo.plagiarismchecker.plans.restInterface;

import com.prepostseo.plagiarismchecker.plans.response.UpgradeUserResponse;
import com.prepostseo.plagiarismchecker.register.response.RegisterResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UpgradeService {

    @FormUrlEncoded
    @POST("app/upgradeuser")
    Call<UpgradeUserResponse> upgradeUser(@Field("key") String key, @Field("packageName") String packageName, @Field("subscriptionId") String subscriptionId, @Field("token") String token);




}
