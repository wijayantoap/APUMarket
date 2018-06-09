package com.application.wijayantoap.apupre_loved.Model;

public class User {
    private String Email;
    private String Password;
    private String Report;
    private String Status;
    private String Item;

    public User() {

    }

    public User(String email) {
        Email = email;
    }

    public User(String email, String password, String report, String status, String item) {
        Email = email;
        Password = password;
        Report = report;
        Status = status;
        Item = item;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
