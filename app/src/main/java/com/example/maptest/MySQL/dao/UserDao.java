package com.example.maptest.MySQL.dao;

import  com.example.maptest.MySQL.enity.User;
import com.example.maptest.utils.JDBCUtils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDao {


    public int login(String username,String password){

        String sql = "select * from user where username = ? and password = ?";

        Connection  con = JDBCUtils.getConn();

        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){

                return rs.getInt(3);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }

        return 3;
    }

    public boolean register(User user){

        String sql = "INSERT INTO `user` (`username`, `password`, `power`, `salt`, `timestamp`) VALUES (?,?,?, '', CURRENT_TIMESTAMP)";

        Connection  con = JDBCUtils.getConn();

        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1,user.getUsername());
            pst.setString(2,user.getPassword());
            pst.setInt(3,user.getPower());


            int value = pst.executeUpdate();

            if(value>0){
                return true;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }
        return false;
    }

    public User findUser(String username){

        String sql = "select * from user where username = ?";

        Connection  con = JDBCUtils.getConn();
        User user = null;
        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1,username);

            ResultSet rs = pst.executeQuery();

            while (rs.next()){

                int id = rs.getInt(0);
                String usernamedb = rs.getString(1);
                String passworddb = rs.getString(2);
                int power  = rs.getInt(3);
                String salt = rs.getString(4);
                Timestamp timestamp = rs.getTimestamp(5);
                user = new User(id,usernamedb,passworddb,power,salt,timestamp);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }

        return user;
    }


}