package com.example.maptest;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.maptest.MySQL.dao.UserDao;
import com.example.maptest.myhome.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        setContentView(R.layout.activity_main);

    }

    public void reg(View view){

        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

    }


    public void login(View view){



        new Thread(() -> {

            UserDao userDao = new UserDao();
            EditText EditTextname = findViewById(R.id.username);
            EditText EditTextpassword = findViewById(R.id.password);
            boolean aa = userDao.login(EditTextname.getText().toString(),EditTextpassword.getText().toString());
            int msg = 0;
            if(aa){
                msg = 1;
            }

            hand1.sendEmptyMessage(msg);


        }).start();


    }
    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1)
            {
                EditText EditTextname = findViewById(R.id.username);
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("username",EditTextname.getText().toString());
                startActivity(intent);

            }
            else if(msg.what == 0)
            {
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"管理员登录成功",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }
    };
}