package com.application.wijayantoap.apupre_loved.Model;

public class Activity {
    private String Username, Details, Date;

    public Activity() {

    }

    public Activity(String username, String details, String date) {
        Username = username;
        Details = details;
        Date = date;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
