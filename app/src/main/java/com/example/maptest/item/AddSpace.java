package com.example.maptest.item;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Lock> list= new ArrayList<>();
    private Button button;
    private String lock_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.add_space);
        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");
        String[] ctype = (String[]) intent.getSerializableExtra("ctype");
        String[] lockid = (String[]) intent.getSerializableExtra("lockid");
        button = findViewById(R.id.add_a_space);
        spinner = findViewById(R.id.spinner);
        inputtime = findViewById(R.id.time_input);
        inputprice = findViewById(R.id.price_input);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                lock_id = lockid[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkidDao parkidDao=new ParkidDao();
                Parkid parkid = new Parkid();
               parkid.setLockid(lock_id);
                parkid.setFreetime(inputtime.getText().toString());
                parkid.setPrice(Float.parseFloat(inputprice.getText().toString()));
                parkidDao.register(parkid,info[3]);

                finish();

            }
        });
    }

}