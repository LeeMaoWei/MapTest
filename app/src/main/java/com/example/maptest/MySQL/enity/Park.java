package com.example.maptest.MySQL.enity;


public class Park{
    private int parkid;
    private int admincode;
    private String parkname;
    private String lng;
    private String lat;
    private int num;

    public Park() {

    }

    public Park(int parkid,int admincode, String parkname, String lng, String lat , int num) {
        this.parkid=parkid;
        this.admincode=admincode;
        this.parkname = parkname;
        this.lng = lng;
        this.lat = lat;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public int getParkid() {
        return parkid;
    }

    public int getAdmincode() {
        return admincode;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getParkname() {
        return parkname;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setParkid(int parkid) {
        this.parkid = parkid;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public void setAdmincode(int admincode) {
        this.admincode = admincode;
    }
}
