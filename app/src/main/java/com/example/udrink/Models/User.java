package com.example.udrink.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private int weight;
    private int feet;
    private int inches;
    private String pid;
    private String partyName;
    private String gender;
    private Date drinkStartTime;
    private String profilePicture;
    private List<Drink> drinks;

    public User() {

    }

    public User(int weight, int feet, int inches, String gender) {
        this.weight = weight;
        this.feet = feet;
        this.inches = inches;
        this.gender = gender;
        this.pid = null;
        this.partyName = null;
        this.profilePicture = null;
        this.drinkStartTime = null;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
