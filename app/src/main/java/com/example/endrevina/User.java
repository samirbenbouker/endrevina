package com.example.endrevina;

import android.graphics.Bitmap;

public class User {

    private String name;
    private int attempts;
    private String time;
    private Bitmap photo;

    public User(){

    }

    public User(String name, int attempts, String time, Bitmap photo){
        this.name = name;
        this.attempts = attempts;
        this.time = time;
        this.photo = photo;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setAttempts(int attempts){
        this.attempts = attempts;
    }

    public int getAttempts(){
        return this.attempts;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return this.time;
    }

    public void setPhoto(Bitmap photo){
        this.photo = photo;
    }

    public Bitmap getPhoto(){
        return this.photo;
    }

    public int compareRecords(User user){
        int compareAttempts = this.getAttempts() - user.getAttempts();

        if(compareAttempts == 0){
            int compareTime = totalTime(this.time) - totalTime(user.getTime());

            if(compareTime == 0){
                return 0;
            }
            else{
                return compareTime;
            }
        }
        else{
            return compareAttempts;
        }
    }

    public int totalTime(String time){
        String[] timeSplit = time.split(":");

        int min = Integer.valueOf(timeSplit[0]);
        int sec = Integer.valueOf(timeSplit[1]);

        if(min == 0){
            return sec;
        }
        else{
            int minToSec = min*60;
            int totalTime = minToSec+sec;

            return totalTime;
        }
    }

}
