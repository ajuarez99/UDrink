package com.example.udrink.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UTime {
    public UTime (){

    }

    public String getTimeAgo(Date dateAt){
        Date now = new Date(System.currentTimeMillis());
        Long difference = now.getTime() - dateAt.getTime();
        Long days  = (difference / (1000*60*60*24));
        LocalDate stuff = dateAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (days < 7 && days > 1){
            return days.toString() + "d";
        }
        else if(days >= 7 ){
            try {
                return (dateAt.getMonth() +1) +"/" + stuff.getDayOfMonth() +"/" + (dateAt.getYear() -100);
            }catch ( Exception e) {
                return "";
            }
        }

        long minute = (difference / (1000 * 60)) % 60;
        long hour = (difference / (1000 * 60 * 60)) % 24;
        if(hour < 24 && hour > 1){
            long day  = hour /24;
            return String.valueOf(hour)+"h";
        }
        else if( hour < 1 && minute >= 1){
            return String.valueOf(minute) +"m";
        }
        else if (minute < 1){

            long second = (difference / 1000) % 60;
            return String.valueOf(second) + "s";
        }
        return "now";
    }
}
