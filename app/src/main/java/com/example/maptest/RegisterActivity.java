package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maptest.MySQL.dao.UserDao;
import com.example.maptest.MySQL.enity.User;

public class RegisterActivity extends AppCompatActivity {

    EditText username = null;
    EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = findViewById(R.id.username);

        password = findViewById(R.id.password);
    }


    public void register(View view){




        String cusername = username.getText().toString();
        String cpassword = password.getText().toString();





        if( cusername.length() < 2 || cpassword.length() < 2 ){
            Toast.makeText(getApplicationContext(),"输入信息不符合要求请重新输入",Toast.LENGTH_LONG).show();
            return;

        }


        User user = new User();


        user.setUsername(cusername);
        user.setPassword(cpassword);


        new Thread(() -> {

            int msg = 0;

            UserDao userDao = new UserDao();

            User uu = userDao.findUser(user.getUsername());

            if(uu != null){
                msg = 1;
            }

            boolean flag = userDao.register(user);
            if(flag){
                msg = 2;
            }
            hand.sendEmptyMessage(msg);

        }).start();


    }
    final Handler hand = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0)
            {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();

            }
            if(msg.what == 1)
            {
                Toast.makeText(getApplicationContext(),"该账号已经存在，请换一个账号",Toast.LENGTH_LONG).show();

            }
            if(msg.what == 2)
            {
                //startActivity(new Intent(getApplication(),MainActivity.class));

                Intent intent = new Intent();
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a","註冊");
                setResult(RESULT_CANCELED,intent);
                finish();
            }

        }
    };
}