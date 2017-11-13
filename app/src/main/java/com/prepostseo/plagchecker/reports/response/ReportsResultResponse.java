package com.prepostseo.plagchecker.reports.response;

public class ReportsResultResponse
{
    private String details;

    public String getDetails() { return this.details; }

    public void setDetails(String details) { this.details = details; }

    private int response;

    public int getResponse() { return this.response; }

    public void setResponse(int response) { this.response = response; }

    private String error;

    public String getError() { return this.error; }

    public void setError(String error) { this.error = error; }
}