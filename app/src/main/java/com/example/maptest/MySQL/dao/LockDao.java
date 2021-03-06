package com.example.maptest.MySQL.dao;

import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.MySQL.enity.User;
import com.example.maptest.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LockDao {



    public boolean Link(String lockid,String lockname,String username){
        String sql = "update locklist set username = ? , lockname = ?  where lockid =" +lockid;

        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, lockname);
            pst.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }

        return true;
    }
public void update(Lock lock){
    String sql = "update locklist set lockname = ? , freetime = ?  where lockid =" +lock.getLockid();

    Connection con = JDBCUtils.getConn();

    try {
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, lock.getLockname());
        pst.setString(2, lock.getFreetime());
        System.out.println(pst);
        pst.execute();


    } catch (SQLException throwables) {
        throwables.printStackTrace();
    } finally {
        JDBCUtils.close(con);
    }

    return ;
}
    public void lockstate(Lock lock){
        String sql = "update locklist set lockstate = ? where lockid =" +lock.getLockid();

        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, String.valueOf(lock.getLockstate()));
            System.out.println(pst);
            pst.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }

        return ;
    }


    public List<Lock> getinfo(String username) throws SQLException {

//       ???????????????List<HashMap<String,String>>???????????????????????????
        List<Lock> list = new ArrayList<>();

//        ?????????????????????????????????????????????????????????conn???????????????getConnection?????????????????????Connection???conn
        Connection conn=JDBCUtils.getConn();

//        ???conn??????????????????sql??????????????????Statement??????),????????????createStatement()
        Statement sta=conn.createStatement();

//        ??????sql??????
        String sql="select * from locklist where username=\'"+username+"\'";

//        ??????Statement????????????sql??????,????????????result???ResultSet???????????????????????????????????????
            ResultSet result = sta.executeQuery(sql);

//        ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            while (result.next()) {
//            ????????????????????????????????????HashMap?????????????????????????????????????????????
                Lock lock = new Lock();
//            ???map???????????????map?????????????????????????????????
//            ??????name?????????result.getString("empname"),?????????????????????????????????????????????????????????
                lock.setLockid(Integer.parseInt(result.getString("lockid")));
                lock.setLockname(result.getString("lockname"));
                lock.setLockstate(Integer.parseInt(result.getString("lockstate")));
                lock.setFreetime(result.getString("freetime"));
                lock.setUsername(username);
                lock.setParkid(result.getString("parkid"));
//            ???????????????????????????list????????????list???????????????[{name=xx},{name=aaa},.......]
                list.add(lock);


        }
        JDBCUtils.close(conn);

//        ???????????????list????????????????????????????????????list
        return list;
    }

}
/***
    public boolean register(Lock lock){

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

***/
