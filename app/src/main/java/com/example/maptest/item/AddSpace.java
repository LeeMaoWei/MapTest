package com.example.maptest.item;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.ims.RcsUceAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.MySQL.enity.Parkid;
import com.example.maptest.R;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


public class AddSpace extends AppCompatActivity {
    private Spinner spinner;
    private EditText inputtime, inputprice;
    private List<Lock> list= new ArrayList<>();;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");
        setContentView(R.layout.add_space);
        Button button = findViewById(R.id.add_a_space);
        spinner = findViewById(R.id.spinner);
        inputtime = findViewById(R.id.time_input);
        inputprice = findViewById(R.id.price_input);
        LockDao lockDao = new LockDao();
        try {
            list = lockDao.getinfo(info[4], info[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String[] ctype = new String[0];
        for (Lock lock:list){
            insert(ctype,lock.getLockname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkidDao parkidDao=new ParkidDao();
                Parkid parkid = new Parkid();
                parkid.setFreetime(inputtime.getText().toString());
                parkid.setPrice(Float.parseFloat(inputprice.getText().toString()));
                parkidDao.register(parkid,info[3]);
            }
        });
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