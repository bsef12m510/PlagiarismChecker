package com.prepostseo.plagiarismchecker.checker.restInterface;

import com.prepostseo.plagiarismchecker.checker.response.PlagiarismResponse;
import com.prepostseo.plagiarismchecker.register.response.RegisterResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PlagiarismService {
    @Multipart
    @POST("apis/checkPlag")
    Call<PlagiarismResponse> checkPlagiarism( @Part("key") RequestBody key, @Part("data") RequestBody data);
}
