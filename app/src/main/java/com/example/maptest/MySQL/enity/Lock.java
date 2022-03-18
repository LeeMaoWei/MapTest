package com.example.maptest.MySQL.enity;

public class Lock {
    private int id;
    private String username;
    private int state;
    private String lockname;


    public Lock() {

    }

    public Lock(int id, String username, int state, String lockname) {
        this.id = id;
        this.username = username;
        this.state=state;
        this.lockname=lockname;
    }

    public String getLockname() {
        return lockname;
    }

    public void setLockname(String lockname) {
        this.lockname = lockname;
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

    public int getState(){return  state;}

    public void setState(int state) {
        this.state = state;
    }
}
