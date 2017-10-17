package com.prepostseo.plagiarismchecker.Login.response;

public class LoginResponse {

    private String user_id;
    private String api_key;
    private String user_name;
    private String user_email;
    private String queries_limit;
    private String expire_date;

    public String getQueries_used() {
        return queries_used;
    }

    public void setQueries_used(String queries_used) {
        this.queries_used = queries_used;
    }

    public String getQueries_limit() {
        return queries_limit;
    }

    public void setQueries_limit(String queries_limit) {
        this.queries_limit = queries_limit;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    private String queries_used;
    private String premium;
    private String verified;
    private Integer response;
    public String getExpireDate() {
        return expire_date;
    }

    public void setExpireDate(String expireDate) {
        this.expire_date = expireDate;
    }



    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

}