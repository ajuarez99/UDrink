package com.example.udrink.Models;

import android.location.Location;

import java.util.Date;
import java.util.List;

public class Party {

    private String pid;
    private List<User> users;
    private String partyName;
    private Location currentLocation;
    private boolean activeParty;
    private Date startTime;

    public Party(String partyName, boolean activeParty) {
        this.partyName = partyName;
        this.activeParty = activeParty;
        this.startTime = new Date(System.currentTimeMillis());
    }

    public Party(String partyName){
        this.partyName = partyName;
    }

    public Party(List<User> users, String name, Location location) {
        this.users = users;
        partyName = name;
        currentLocation = location;
        startTime = new Date(System.currentTimeMillis());
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isActiveParty() {
        return activeParty;
    }

    public void setActiveParty(boolean activeParty) {
        this.activeParty = activeParty;
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
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
