package com.prepostseo.plagchecker.reports.response;

import java.util.ArrayList;

public class ReportsResponse
{
    private String error;
    private int response;

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }
    public int getResponse() { return this.response; }

    public void setResponse(int response) { this.response = response; }

    private ArrayList<Report> reports;

    public ArrayList<Report> getReports() { return this.reports; }

    public void setReports(ArrayList<Report> reports) { this.reports = reports; }

    public class Report
    {
        private int id;

        public int getId() { return this.id; }

        public void setId(int id) { this.id = id; }

        private String title;

        public String getTitle() { return this.title; }

        public void setTitle(String title) { this.title = title; }

        private int uniquePercent;

        public int getUniquePercent() { return this.uniquePercent; }

        public void setUniquePercent(int uniquePercent) { this.uniquePercent = uniquePercent; }

        private String date;

        public String getDate() { return this.date; }

        public void setDate(String date) { this.date = date; }

        private String time;

        public String getTime() { return this.time; }

        public void setTime(String time) { this.time = time; }
    }

}
