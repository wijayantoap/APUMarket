package com.application.wijayantoap.apupre_loved.Model;

public class User {
    private String Email;
    private String Password;
    private String Status;
    private int Item;

    public User() {

    }

    public User(String email) {
        Email = email;
    }

    public User(String email, String password, String status, int item) {
        Email = email;
        Password = password;
        Status = status;
        Item = item;
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


    public int getItem() {
        return Item;
    }

    public void setItem(int item) {
        Item = item;
    }
}
