package com.prepostseo.plagiarismchecker.checker.response;

import java.util.List;

/**
 * Created by zeeshan on 9/24/2017.
 */
public class PlagiarismResponse {
    private Integer totalQueries;
    private Double plagPercent;
    private Double uniquePercent;
    private Object excludeURL;
    private List<PlagiarismDetail> details = null;

    public Integer getTotalQueries() {
        return totalQueries;
    }

    public void setTotalQueries(Integer totalQueries) {
        this.totalQueries = totalQueries;
    }

    public Double getPlagPercent() {
        return plagPercent;
    }

    public void setPlagPercent(Double plagPercent) {
        this.plagPercent = plagPercent;
    }

    public Double getUniquePercent() {
        return uniquePercent;
    }

    public void setUniquePercent(Double uniquePercent) {
        this.uniquePercent = uniquePercent;
    }

    public Object getExcludeURL() {
        return excludeURL;
    }

    public void setExcludeURL(Object excludeURL) {
        this.excludeURL = excludeURL;
    }

    public List<PlagiarismDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PlagiarismDetail> details) {
        this.details = details;
    }
}
