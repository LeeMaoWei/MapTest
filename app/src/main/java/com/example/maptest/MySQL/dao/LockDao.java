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

//       先定义一个List<HashMap<String,String>>类型的数据并实例化
        List<Lock> list = new ArrayList<>();

//        调用连接函数，传入数据库名的形参，获得conn对象，因为getConnection的返回类型就是Connection及conn
        Connection conn=JDBCUtils.getConn();

//        由conn对象创建执行sql语句的对象（Statement类型),调用方法createStatement()
        Statement sta=conn.createStatement();

//        定义sql语句
        String sql="select * from locklist where username=\'"+username+"\'";

//        调用Statement对象执行sql语句,返回结果result是ResultSet类型，就是结果集，具体百度
            ResultSet result = sta.executeQuery(sql);

//        条件是当结果集是否有下一行，这是一个相当于指针的东西，第一次调用时会把第一行设置为当前行，第二次回吧第二行设置为当前行，以此类推，直到没有下一行，循环结束
            while (result.next()) {
//            每次循环都会新实例化一个HashMap对象，用于将遍历到的数据填进去
                Lock lock = new Lock();
//            往map中填数据，map的数据类型相当于键值对
//            键是name，值是result.getString("empname"),意思是结果集指针所在行的字段名中的数据
                lock.setLockid(Integer.parseInt(result.getString("lockid")));
                lock.setLockname(result.getString("lockname"));
                lock.setLockstate(Integer.parseInt(result.getString("lockstate")));
                lock.setFreetime(result.getString("freetime"));
                lock.setUsername(username);
                lock.setParkid(result.getString("parkid"));
//            每次循环完就添加到list中，最终list的样子是：[{name=xx},{name=aaa},.......]
                list.add(lock);


        }
        JDBCUtils.close(conn);

//        最后记得把list返回出去，不然拿不到这个list
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
