package com.application.wijayantoap.apupre_loved.Model;

public class Flag {

    private String Title, Username, Date;

    public Flag() {
    }

    public Flag(String username, String title, String date) {
        Username = username;
        Title = title;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
