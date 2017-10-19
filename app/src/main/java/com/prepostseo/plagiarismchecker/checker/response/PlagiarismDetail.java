package com.prepostseo.plagiarismchecker.checker.response;

import java.io.Serializable;
import java.util.List;

public class PlagiarismDetail  implements Serializable {

    private String query;
    private Integer version;
    private String totalMatches;
    private boolean unique;
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

    public boolean getUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public List<String> getMatchedUrls() {
        return matchedUrls;
    }

    public void setMatchedUrls(List<String> matchedUrls) {
        this.matchedUrls = matchedUrls;
    }

}
