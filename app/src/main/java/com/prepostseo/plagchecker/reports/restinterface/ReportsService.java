package com.prepostseo.plagchecker.reports.restinterface;


import com.prepostseo.plagchecker.checker.response.PlagiarismResponse;
import com.prepostseo.plagchecker.reports.response.ReportsResponse;
import com.prepostseo.plagchecker.reports.response.ReportsResultResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ReportsService {

    @Multipart
    @POST("apis/getReportsList")
    Call<ReportsResponse> getReportList(@Part("key") RequestBody key);

    @Multipart
    @POST("apis/getReportDetails")
    Call<ReportsResultResponse> getReportDetails(@Part("key") RequestBody key, @Part("id") RequestBody id);
}
