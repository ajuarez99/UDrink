package com.example.udrink.Models;

import android.location.Location;

import java.util.List;

public class Party {

    private int pid;
    private List<User> users;
    private String partyName;
    private Location currentLocation;

    public Party(String partyName){
        this.partyName = partyName;
    }

    public Party(List<User> users, String name, Location location) {
        this.users = users;
        partyName = name;
        currentLocation = location;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }


    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void addUser(User u){
        users.add(u);
    }
}
