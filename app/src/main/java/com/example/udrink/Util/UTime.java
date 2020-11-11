package com.example.udrink.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class UTime {
    public UTime (){

    }

    public String getTimeAgo(Date dateAt){
        Date now = new Date(System.currentTimeMillis());
        Long difference = now.getTime() - dateAt.getTime();
        Long days  = (difference / (1000*60*60*24));

        if (days < 7 && days >= 1){
            return days.toString() + "d";
        }
        else if(days >= 7 ){
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateAt);
                return (cal.get(Calendar.MONTH) + 1) +"/" + (cal.get(Calendar.DAY_OF_MONTH)) +"/" + (cal.get(Calendar.YEAR));
            }catch ( Exception e) {
                return "";
            }
        }
        else {
            long minute = (difference / (1000 * 60)) % 60;
            long hour = (difference / (1000 * 60 * 60)) % 24;
            if (hour < 24 && hour > 1) {
                return hour + "h";
            } else if (hour < 1 && minute >= 1) {
                return minute + "m";
            } else if (minute < 1) {

                long second = (difference / 1000) % 60;
                return second + "s";
            }
            return "now";
        }
    }
}
