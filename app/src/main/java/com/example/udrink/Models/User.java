package com.example.udrink.Models;

public class User {
    private String uid;
    private String name;
    private int weight;
    private int feet;
    private int inches;

    public User(String uid, String name, int weight, int feet, int inches) {
        this.uid = uid;
        this.name = name;
        this.weight = weight;
        this.feet = feet;
        this.inches = inches;
    }

    public User(String uid, String name, int weight) {
        this.uid = uid;
        this.name = name;
        this.weight = weight;
    }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFeet() {
        return feet;
    }

    public void setFeet(int feet) {
        this.feet = feet;
    }

    public int getInches() {
        return inches;
    }

    public void setInches(int inches) {
        this.inches = inches;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
