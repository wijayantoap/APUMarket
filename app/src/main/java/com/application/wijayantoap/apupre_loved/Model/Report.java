package com.application.wijayantoap.apupre_loved.Model;

import android.widget.ToggleButton;

public class Report {
    private String Title;
    private String Email;
    private String Report;
    private String Date;

    private ToggleButton toggleButton;

    public Report(){

    }

    public Report(String title, String email, String report, String date) {
        Title = title;
        Email = email;
        Report = report;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
