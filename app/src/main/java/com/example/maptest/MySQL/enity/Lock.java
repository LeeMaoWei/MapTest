package com.example.maptest.MySQL.enity;

public class Lock {
    private int lockid;
    private String username;
    private int lockstate;
    private String lockname;
    private String freetime;


    public Lock() {

    }

    public Lock(int id, String username, int state, String lockname) {
        this.lockid = id;
        this.username = username;
        this.lockstate=state;
        this.lockname=lockname;
    }

    public String getLockname() {
        return lockname;
    }

    public void setLockname(String lockname) {
        this.lockname = lockname;
    }


    public int getId() {
        return lockid;
    }

    public void setId(int lockid) {
        this.lockid = lockid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getState(){return  lockstate;}

    public void setState(int state) {
        this.lockstate = state;
    }

    public void setFreetime(String freetime) {
        this.freetime = freetime;
    }

    public String getFreetime() {
        return freetime;
    }
}
