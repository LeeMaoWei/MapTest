package com.example.maptest.MySQL.dao;


import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.MySQL.enity.Parkid;

import com.example.maptest.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParkidDao {
    public boolean register(Parkid parkid,String tablename){

        String sql = "INSERT INTO `"+tablename+"` (lockid,price,freetime) VALUES (?,?,?)";

        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1, parkid.getLockid());
            pst.setString(2, String.valueOf(parkid.getPrice()));
            pst.setString(3, parkid.getFreetime());


            int value = pst.executeUpdate();
            System.out.println(pst);
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


    public  List<HashMap<String,String>> getspaceinfo(String tablename) throws SQLException {

//       先定义一个List<HashMap<String,String>>类型的数据并实例化
        List<HashMap<String,String>> list= new ArrayList<>();

//        调用连接函数，传入数据库名的形参，获得conn对象，因为getConnection的返回类型就是Connection及conn
        Connection conn=JDBCUtils.getConn();

//        由conn对象创建执行sql语句的对象（Statement类型),调用方法createStatement()
        Statement sta=conn.createStatement();

//        定义sql语句
        String sql="select * from `"+tablename+"`";

//        调用Statement对象执行sql语句,返回结果result是ResultSet类型，就是结果集，具体百度
        ResultSet result=sta.executeQuery(sql);

//        判断一下是否为空
        if (result==null){
            return null;
        }

//        条件是当结果集是否有下一行，这是一个相当于指针的东西，第一次调用时会把第一行设置为当前行，第二次回吧第二行设置为当前行，以此类推，直到没有下一行，循环结束
        while (result.next()) {
//            每次循环都会新实例化一个HashMap对象，用于将遍历到的数据填进去
            HashMap<String,String> map=new HashMap<>();
//            往map中填数据，map的数据类型相当于键值对
//            键是name，值是result.getString("empname"),意思是结果集指针所在行的字段名中的数据
            if(result.getString("state").equals("0"))
            {
                continue;
            }
            map.put("spaceid",result.getString("spaceid"));
            map.put("price",result.getString("price"));
            map.put("freetime",result.getString("freetime"));
//            每次循环完就添加到list中，最终list的样子是：[{name=xx},{name=aaa},.......]
            list.add(map);
        }
        JDBCUtils.close(conn);
//        最后记得把list返回出去，不然拿不到这个list
        return list;
    }
    public void update(Lock lock ){
        String sql = "update `"+lock.getParkid()+"` freetime = ?  where lockid =" +lock.getLockid();

        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, lock.getFreetime());
            System.out.println(pst);
            pst.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }

    }
    public void parkidstate(Lock lock){
        String sql = "update `"+lock.getParkid()+"` set state = ?  where lockid =" +lock.getLockid();

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
}
