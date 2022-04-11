package com.example.maptest.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Spacetable extends AppCompatActivity  {


    private List<HashMap<String,String>> spacelist= new ArrayList<>();
    private final ParkidDao parkidDao=new ParkidDao();
   private ListView listView;
   private Button mButton;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_list);

       listView=findViewById(R.id.stackview_main);
        mButton=findViewById(R.id.addspace);


        initlist();




    }

    @SuppressLint("SetTextI18n")
    private void initlist() {


        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List <Lock> list = new ArrayList<>();
                LockDao lockDao = new LockDao();
                try {
                    list = lockDao.getinfo(info[4]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String[] ctype = new String[]{};
                String[] lockid = new String[]{};
                for (Lock lock:list){
                    ctype =  insert(ctype,lock.getLockname());
                    lockid = insert(lockid, String.valueOf(lock.getId()));
                }
                Intent intent1 = new Intent(Spacetable.this, AddtheSpace.class);
                intent1.putExtra("info",info);
                intent1.putExtra("lockid",lockid) ;
                intent1.putExtra("ctype",ctype);
                startActivity(intent1);
            }
        });

        try {
            spacelist = parkidDao.getspaceinfo(info[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Cardstackview adapter=new Cardstackview(Spacetable.this,spacelist);
        listView.setAdapter(adapter);


    }
    private static String[] insert(String[] arr, String str) {
        int size = arr.length;  //获取数组长度
        String[] tmp = new String[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = str;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }

}
