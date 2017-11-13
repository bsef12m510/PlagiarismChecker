package com.prepostseo.plagchecker.register.restInterface;

import com.prepostseo.plagchecker.register.response.RegisterResponse;
import com.prepostseo.plagchecker.register.response.ResendActivationResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RegisterService
{
    @FormUrlEncoded
    @POST("app/register")
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email, @Field("pass") String pass, @Field("GoogleAuth") String googleAuth);

    @Multipart
    @POST("app/verifyEmail")
    Call<RegisterResponse> verify( @Part("code") RequestBody code, @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("app/resendActivation")
    Call<ResendActivationResponse> resendActivation(@Part("user_id") RequestBody user_id,@Part("resend") RequestBody resend);
}
