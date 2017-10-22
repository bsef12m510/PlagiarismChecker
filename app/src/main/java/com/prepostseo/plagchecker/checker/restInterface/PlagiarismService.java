package com.prepostseo.plagchecker.checker.restInterface;

import com.prepostseo.plagchecker.checker.response.PlagiarismResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PlagiarismService {
    @Multipart
    @POST("apis/checkPlag")
    Call<PlagiarismResponse> checkPlagiarism( @Part("key") RequestBody key, @Part("data") RequestBody data, @Part("checksources") RequestBody checksources);
}
