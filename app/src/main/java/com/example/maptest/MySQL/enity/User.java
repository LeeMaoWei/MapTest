package com.example.maptest.MySQL.enity;

import java.sql.Timestamp;


public class User {
    private int id;
    private String username;
    private String password;
    private int power;
    private String salt;
    private Timestamp timestamp;


    public User() {

    }

    public User(int id, String username, String password, int power,String salt ,Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.power = power;
        this.salt = salt;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getSalt(){
        return salt;
    }

    public void setSalt(String salt){
        this.salt=salt;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
