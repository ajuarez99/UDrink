package com.example.udrink.Models;

import java.util.Date;

public class PartyHistory {

    private String name;
    private Date date;

    public PartyHistory(String name, Date date){
        this.name = name;
        this.date = date;
    }

    public PartyHistory() {}

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }
}
