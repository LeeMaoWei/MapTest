package com.example.maptest.MySQL.dao;

import com.example.maptest.MySQL.enity.Park;

import com.example.maptest.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParkDao {
/***未完成***/
    public String register(Park park){

        String sql = "INSERT INTO `park` (`AdminCode`, `parkname`, `lat`, `lng` ,`num`) VALUES (?,?,?,?,0)";

        Connection con = JDBCUtils.getConn();
        String no=null;


        try {
            PreparedStatement pst=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, String.valueOf(park.getAdmincode()));
            pst.setString(2,park.getParkname());
            pst.setString(3,park.getLng());
            pst.setString(4,park.getLat());


            int value = pst.executeUpdate();
            if(value > 0 ){
                /**
                 * 获取刚刚插入进去的记录中关注的那几列的值
                 */
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    //获取deptno的值(int)--现在只关注此列
                     no = rs.getString(1);//此处建议使用数字,getInt("deptno")对于不同的数据库版本可能不支持

                }
                rs.close();//释放资源

                return no;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }
        return null;
    }

    public void register(String a) {

        Connection con = JDBCUtils.getConn();
        String sql = "CREATE TABLE `"+a+"` (\n" +
                "  `spaceid` int primary key not null auto_increment,\n" +
                "  `lockid` int NOT NULL UNIQUE,\n" +
                "  `state` int NOT NULL DEFAULT '1',\n" +
                "  `price` float NOT NULL,\n" +
                "  `freetime` varchar(11) COLLATE utf8mb4_general_ci NOT NULL\n" +
                ") ";

        System.out.println(sql);
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




    public  List<HashMap<String,String>> getinfo(String admincode) throws SQLException {

//       先定义一个List<HashMap<String,String>>类型的数据并实例化
        List<HashMap<String,String>> list= new ArrayList<>();

//        调用连接函数，传入数据库名的形参，获得conn对象，因为getConnection的返回类型就是Connection及conn
        Connection conn=JDBCUtils.getConn();

//        由conn对象创建执行sql语句的对象（Statement类型),调用方法createStatement()
        Statement sta=conn.createStatement();

//        定义sql语句
        String sql="select * from park where AdminCode="+admincode;

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
            map.put("lng",result.getString("lng"));
            map.put("lat",result.getString("lat"));
            map.put("name",result.getString("parkname"));
            map.put("id",result.getString("parkid"));
//            每次循环完就添加到list中，最终list的样子是：[{name=xx},{name=aaa},.......]
            list.add(map);
        }
        JDBCUtils.close(conn);
//        最后记得把list返回出去，不然拿不到这个list
        return list;
    }

}
