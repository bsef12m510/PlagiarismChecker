package com.prepostseo.plagchecker.Login.restInterface;

import com.prepostseo.plagchecker.Login.response.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface LoginService {

    @Multipart
    @POST("app/loginuser")
    Call<LoginResponse> login(@Part("email") RequestBody name, @Part("pass") RequestBody email);
}