package com.prepostseo.plagiarismchecker.api;

/**
 * Created by zeeshan on 9/15/2017.
 */
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "https://www.prepostseo.com/";
//    public static final String PLAG_BASE_URL = "https://www.prepostseo.com/apis/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

 /*   public static Retrofit getPlagServiceClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(PLAG_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }*/
}