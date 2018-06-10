package com.application.wijayantoap.apupre_loved.Model;

public class Item {
    private String Name, Username, Description, Price, Picture, Quality, Phone , Date, CategoryId;
    public Item(){

    }

    public Item(String name, String username, String description, String price, String picture, String quality, String phone, String date, String categoryId) {
        Name = name;
        Username = username;
        Description = description;
        Price = price;
        Picture = picture;
        Quality = quality;
        Phone = phone;
        Date = date;
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

}
