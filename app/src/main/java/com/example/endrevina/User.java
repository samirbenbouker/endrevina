package com.example.endrevina;

import android.graphics.Bitmap;

public class User {

    private String name;
    private int attempts;
    private int time;
    private Bitmap photo;

    public User(){

    }

    public User(String name, int attempts, int time, Bitmap photo){
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

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return this.time;
    }

    public void setPhoto(Bitmap photo){
        this.photo = photo;
    }

    public Bitmap getPhoto(){
        return this.photo;
    }

    public int compareRecords(User user) {
        int compareRecords = this.getAttempts() - user.getAttempts();

        if (compareRecords == 0) {
            int compareTime = this.getTime() - user.getTime();

            if (compareTime == 0) {
                return 0;
            } else {
                return compareTime;
            }
        } else {
            return compareRecords;
        }
    }
}
