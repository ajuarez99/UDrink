package com.example.udrink.Models;

import java.util.Date;

public class Drink {
    private String DrinkName;
    private int uid;
    private Date DrankAt;
    private int BAC;
    private int Ounces;
    public Drink(String drinkName, Date drankAt, int BAC, int ounces) {
        DrinkName = drinkName;
        this.uid = uid;
        DrankAt = drankAt;
        this.BAC = BAC;
        Ounces = ounces;
    }
    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getDrankAt() {
        return DrankAt;
    }

    public void setDrankAt(Date drankAt) {
        DrankAt = drankAt;
    }

    public int getBAC() {
        return BAC;
    }

    public void setBAC(int BAC) {
        this.BAC = BAC;
    }

    public int getOunces() {
        return Ounces;
    }

    public void setOunces(int ounces) {
        Ounces = ounces;
    }


}
