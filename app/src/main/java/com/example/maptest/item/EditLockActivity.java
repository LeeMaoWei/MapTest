package com.example.maptest.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.MySQL.enity.Parkid;
import com.example.maptest.R;

public class EditLockActivity extends AppCompatActivity {
    EditText Lockname,start_time,end_time;
    Button button;
    String[] lockinfo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_lock);
        Intent intent = this.getIntent();
        lockinfo = (String[]) intent.getSerializableExtra("lockinfo");
        Lockname=findViewById(R.id.lockname);

       end_time=findViewById(R.id.end_time);
       start_time=findViewById(R.id.start_time);
       button = findViewById(R.id.edit_the_lock);
        Lockname.setText(lockinfo[0]);
      start_time.setText(lockinfo[1].substring(0, 2) + ":" + lockinfo[1].substring(2, 4));
      end_time.setText(lockinfo[1].substring(4, 6) + ":" + lockinfo[1].substring(6));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lock lock =new Lock();
                lock.setLockid(Integer.parseInt(lockinfo[2]));
                lock.setParkid(lockinfo[3]);
                lock.setLockname(Lockname.getText().toString());
                String starttime = start_time.getText().toString();
                String endtime = end_time.getText().toString();
                String time = starttime+endtime;
                lock.setFreetime(time.replace(":",""));
                LockDao lockDao=new LockDao();
                ParkidDao parkidDao = new ParkidDao();
                lockDao.update(lock);
                parkidDao.update(lock);
                finish();

            }
        });
    }
}
