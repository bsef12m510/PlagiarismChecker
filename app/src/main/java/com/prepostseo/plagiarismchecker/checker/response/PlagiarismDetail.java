package com.prepostseo.plagiarismchecker.checker.response;

import java.util.List;

/**
 * Created by zeeshan on 9/24/2017.
 */
public class PlagiarismDetail {

    private String query;
    private Integer version;
    private String totalMatches;
    private String unique;
    private List<String> matchedUrls = null;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(String totalMatches) {
        this.totalMatches = totalMatches;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public List<String> getMatchedUrls() {
        return matchedUrls;
    }

    public void setMatchedUrls(List<String> matchedUrls) {
        this.matchedUrls = matchedUrls;
    }

}
