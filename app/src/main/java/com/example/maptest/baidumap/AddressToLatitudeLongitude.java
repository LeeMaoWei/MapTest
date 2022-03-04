package com.example.maptest.baidumap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class AddressToLatitudeLongitude {
    private final String address;//地址
    private double Latitude ;//纬度
    private double Longitude ;//经度

    public AddressToLatitudeLongitude(String addr_str) {
        this.address = addr_str;
    }
    /*
     *根据地址得到地理坐标
     */
    public void getLatAndLngByAddress(){
        String addr = "55.5";
        String lat = "55.5";
        String lng = "55.5";
        try {
            addr = java.net.URLEncoder.encode(address,"UTF-8");//编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoding/v3/?"
                +"address=%s&output=json&ak=BCdSIjzhIkGHKn8ePrY2AZslPNs2nsqZ&mcode=47:35:C4:C1:7D:F8:41:DB:62:FE:72:79:32:43:AF:09:9A:AD:05:CA;com.example.maptest",addr);
        URL myURL = null;
        URLConnection httpsConn;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert myURL != null;
            httpsConn = myURL.openConnection();//建立连接
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
                BufferedReader br = new BufferedReader(insr);
                String data;
                if ((data = br.readLine()) != null) {
                    lat = data.substring(data.indexOf("\"lat\":")+("\"lat\":").length(), data.indexOf("},\"precise\""));
                    lng = data.substring(data.indexOf("\"lng\":")+("\"lng\":").length(), data.indexOf(",\"lat\""));
                }
                insr.close();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Latitude = Double.parseDouble(lat);
        Longitude = Double.parseDouble(lng);
    }
    public Double getLatitude() {
        return Latitude;
    }
    public Double getLongitude() {
        return Longitude;
    }

    public static void main(String[] args) {
        AddressToLatitudeLongitude at = new AddressToLatitudeLongitude("西北工业大学");
        at.getLatAndLngByAddress();
        System.out.println(at.getLatitude() + " " + at.getLongitude());
    }
}
