package com.example.maptest.MySQL.enity;


public class Parkid{
    private int spaceid;
    private int state;
    private float price;
    private String freetime;

    public Parkid() {

    }

    public Parkid(int spaceid,int state, float price, String freetime) {
        this.spaceid=spaceid;
        this.state = state;
        this.price = price;
        this.freetime = freetime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getPrice() {
        return price;
    }

    public int getSpaceid() {
        return spaceid;
    }

    public int getState() {
        return state;
    }

    public String getFreetime() {
        return freetime;
    }

    public void setFreetime(String freetime) {
        this.freetime = freetime;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSpaceid(int spaceid) {
        this.spaceid = spaceid;
    }

}
