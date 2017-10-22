package com.prepostseo.plagchecker.register.response;

public class RegisterResponse {
    private Integer user_id;
    private String api_key;
    private Integer response;
    private String error;

    public Integer getUserId() {
        return user_id;
    }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
