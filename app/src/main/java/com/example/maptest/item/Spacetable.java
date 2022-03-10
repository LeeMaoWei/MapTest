package com.example.maptest.item;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.R;

public class Spacetable extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_list);
        Intent intent=this.getIntent();
        String[] info= (String[]) intent.getSerializableExtra("info");
        Toast.makeText(this,
                info[0]+info[1]+info[2], Toast.LENGTH_SHORT).show();
    }
}
