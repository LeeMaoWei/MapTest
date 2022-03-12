package com.example.maptest.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.model.LatLng;
import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Spacetable extends AppCompatActivity {

    private List<HashMap<String,String>> spacelist= new ArrayList<>();
    private final ParkidDao parkidDao=new ParkidDao();
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_list);
        listView=findViewById(R.id.space_layout);
        initlist();




    }

    @SuppressLint("SetTextI18n")
    private void initlist() {

        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");



        Toast.makeText(this,
                info[0] + "\n" + info[1] + "\n" + info[2] + "\n" + info[3], Toast.LENGTH_SHORT).show();
        try {
            spacelist = parkidDao.getspaceinfo(info[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        LayoutInflater inflater =getLayoutInflater();
        SpaceAdapter adapter = new SpaceAdapter(inflater,spacelist);
        listView.setAdapter(adapter);


    }
}
