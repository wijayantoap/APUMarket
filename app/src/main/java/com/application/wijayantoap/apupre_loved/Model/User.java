package com.application.wijayantoap.apupre_loved.Model;

public class User {
    private String Email;
    private String Password;
    private int Report;
    private String Status;

    public User() {

    }

    public User(String email) {
        Email = email;
    }

    public User(String email, String password, int report, String status) {
        Email = email;
        Password = password;
        Report = report;
        Status = status;
    }

    public int getReport() {
        return Report;
    }

    public void setReport(int report) {
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

    public void setemail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
