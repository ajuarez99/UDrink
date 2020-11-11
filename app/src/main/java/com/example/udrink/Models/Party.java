package com.example.udrink.Models;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Party {


    private String partyName;
    private Location currentLocation;
    private boolean activeParty;
    private Date startTime;
    private List<String> members;

    public Party() {

    }

    public Party(String partyName){
        this.partyName = partyName;
        this.activeParty = true;
        this.startTime = new Date(System.currentTimeMillis());
        this.members = new ArrayList<String>();
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

    public void addMember(String uid) {
        members.add(uid);
    }

    public List<String> getMembers() {
        return members;
    }
}
