package com.example.udrink.Models;

import java.util.List;

public class User {
    private String uid;
    private String name;
    private int weight;
    private int feet;
    private int inches;
    private List<User> friends;
    private List<Drink> drinks;
    public User() {

    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    public User(User user){
        this.uid = user.uid;
        this.name = user.name;
        this.weight = user.weight;
        this.feet = user.feet;
        this.inches = user.inches;
        this.drinks = user.drinks;
        this.friends = user.friends;
    }
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

    public void copyUser(User copy){
        if(copy != null){
            this.uid = copy.getUid();
            this.name = copy.getName();
            this.weight = copy.getWeight();
            this.feet = copy.getFeet();
            this.inches = copy.getInches();
        }

    }
}
