package com.example.maptest.utils;


public class Mqtt {
    public String host = "tcp://124.222.110.226:1883";
    public String clientId = "android";
    public String userName = "admin";
    public String passWord = "public";
    public void Mqtt(String host , String clientId,String userName , String passWord) {
        this.clientId = clientId;
        this.host = host;
      this.userName= userName;
        this.passWord=passWord;
        if (clientId.isEmpty()) {
            this.clientId = "android_mqtt_client";
        }

    }

    public String getClientId() {
        return clientId;
    }

    public String getHost() {
        return host;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
