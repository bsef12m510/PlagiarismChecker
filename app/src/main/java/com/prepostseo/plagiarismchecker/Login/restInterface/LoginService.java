package com.prepostseo.plagiarismchecker.Login.restInterface;

import com.prepostseo.plagiarismchecker.Login.response.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface LoginService {

    @Multipart
    @POST("app/loginuser")
    Call<LoginResponse> login(@Part("email") RequestBody name, @Part("pass") RequestBody email);
}