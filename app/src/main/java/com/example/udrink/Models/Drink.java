package com.example.udrink.Models;

import java.util.Date;

public class Drink {
    private String DrinkName;
    private String uid;
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
    public Drink(){

    }
    public Drink(Drink drink, String uid){
        this.DrinkName = drink.DrinkName;
        this.uid = uid;
        this.DrankAt = drink.DrankAt;
        this.BAC = drink.BAC;
        this.Ounces = drink.Ounces;
    }
    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDrankAt() {
        return DrankAt;
    }

    public void setDrankAt(Date drankAt) {
        DrankAt = drankAt;
    }

    public int getABV() {
        return BAC;
    }

    public void setABV(int BAC) {
        this.BAC = BAC;
    }

    public int getOunces() {
        return Ounces;
    }

    public void setOunces(int ounces) {
        Ounces = ounces;
    }


}
