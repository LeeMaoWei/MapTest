package com.example.maptest.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.R;
import com.loopeer.cardstack.CardStackView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Spacetable extends AppCompatActivity  {


    private List<HashMap<String,String>> spacelist= new ArrayList<>();
    private final ParkidDao parkidDao=new ParkidDao();
   private ListView listView;

    private CardStackView mStackView;

    private Cardstackview mTestStackAdapter;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_list);
        //listView=findViewById(R.id.space_layout);


       listView=findViewById(R.id.stackview_main);



        initlist();




    }

    @SuppressLint("SetTextI18n")
    private void initlist() {


        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");






    /*
        Toast.makeText(this,
                info[0] + "\n" + info[1] + "\n" + info[2] + "\n" + info[3], Toast.LENGTH_SHORT).show();
    ***/
        try {
            spacelist = parkidDao.getspaceinfo(info[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Cardstackview adapter=new Cardstackview(Spacetable.this,spacelist);
        listView.setAdapter(adapter);





    }

}
