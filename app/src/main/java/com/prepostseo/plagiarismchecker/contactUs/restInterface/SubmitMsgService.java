package com.prepostseo.plagiarismchecker.contactUs.restInterface;

import com.prepostseo.plagiarismchecker.contactUs.response.MessageResponse;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SubmitMsgService {
    @FormUrlEncoded
    @POST("app/submitmsg")
    Call<MessageResponse> submitMsg(@Field("name") String name, @Field("email") String email, @Field("msg") String msg);
}
