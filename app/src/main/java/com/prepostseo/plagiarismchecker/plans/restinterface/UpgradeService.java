package com.prepostseo.plagiarismchecker.plans.restInterface;

import com.prepostseo.plagiarismchecker.plans.response.UpgradeUserResponse;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Hassan on 10/9/17.
 */

public interface UpgradeService {
    @Multipart
    @POST("app/upgradeuser")
    Call<UpgradeUserResponse> upgradeUser(@Part("key") String key, @Part("packageName") String packageName, @Part("subscriptionId") String subscriptionId, @Part("token") String token);
}
