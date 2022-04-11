package com.example.maptest.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.ParkDao;
import com.example.maptest.MySQL.enity.Park;
import com.example.maptest.R;

public class AddaSpace extends AppCompatActivity {
    TextView Lat,Lon,Adcode;
    EditText name;
    Button mbotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");

        setContentView(R.layout.add_a_space);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Lat=findViewById(R.id.lat);
        Lon=findViewById(R.id.lon);
        Adcode=findViewById(R.id.adcode);
        name=findViewById(R.id.name);
        mbotton=findViewById(R.id.button);



        Adcode.setText(info[0]);
        Lat.setText(info[1]);
        Lon.setText(info[2]);

        mbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkDao parkDao=new ParkDao();
                int Adcode= Integer.parseInt(info[0]);
                Park park = new Park(0,Adcode,name.getText().toString(),info[1],info[2],0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        parkDao.register(parkDao.register(park));
                    }
                }).start();

            }

        });

    }
}
