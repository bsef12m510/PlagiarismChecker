package com.prepostseo.plagiarismchecker.register.response;

/**
 * Created by zeeshan on 9/15/2017.
 */
public class RegisterResponse {
    private Integer user_id;
    private String api_key;
    private Integer response;

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public String getApiKey() {
        return api_key;
    }

    public void setApiKey(String apiKey) {
        this.api_key = apiKey;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }
}
