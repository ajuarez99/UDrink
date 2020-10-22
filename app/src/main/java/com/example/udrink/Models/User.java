package com.example.udrink.Models;

public class User {
    private int uid;
    private String name;

    public User(int id, String name){
        this.name = name;
        this.uid = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
