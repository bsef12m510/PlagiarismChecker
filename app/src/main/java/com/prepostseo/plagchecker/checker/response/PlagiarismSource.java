package com.prepostseo.plagchecker.checker.response;


import java.io.Serializable;

public class PlagiarismSource implements Serializable
{
    private String link;

    public String getLink() { return this.link; }

    public void setLink(String link) { this.link = link; }

    private int count;

    public int getCount() { return this.count; }

    public void setCount(int count) { this.count = count; }

    private int percent;

    public int getPercent() { return this.percent; }

    public void setPercent(int percent) { this.percent = percent; }
}

