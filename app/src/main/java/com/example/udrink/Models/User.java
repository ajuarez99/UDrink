package com.example.udrink.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private int weight;
    private int feet;
    private int inches;
    private String pid;
    private String partyName;
    private String profilePicture;
    private List<Drink> drinks;

    public User() {

    }

    public User(int weight, int feet, int inches) {
        this.weight = weight;
        this.feet = feet;
        this.inches = inches;
        this.pid = null;
        this.partyName = null;
        this.profilePicture = null;
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
        this.pid = null;
        this.partyName = null;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String partyId) {
        this.pid = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
