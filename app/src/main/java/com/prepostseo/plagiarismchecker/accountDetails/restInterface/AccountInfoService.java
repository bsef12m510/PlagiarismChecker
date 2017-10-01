package com.prepostseo.plagiarismchecker.accountDetails.restInterface;

import com.prepostseo.plagiarismchecker.Login.response.LoginResponse;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zeeshan on 10/1/2017.
 */
public interface AccountInfoService {
    @Multipart
    @POST("app/getAccountDetails")
    Call<LoginResponse> getAccountDetails( @Part("key") RequestBody key);
}
