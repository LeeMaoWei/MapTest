package com.example.maptest.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

public class Spacetable extends AppCompatActivity implements CardStackView.ItemExpendListener {


    private List<HashMap<String,String>> spacelist= new ArrayList<>();
    private final ParkidDao parkidDao=new ParkidDao();
   // private ListView listView;

    private CardStackView mStackView;

    private Cardstackview mTestStackAdapter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_list);
        //listView=findViewById(R.id.space_layout);

        mStackView=findViewById(R.id.stackview_main);
        mTestStackAdapter = new Cardstackview(this);
        mStackView.setAdapter(mTestStackAdapter);
        mStackView.setItemExpendListener(this);


        initlist();




    }

    @SuppressLint("SetTextI18n")
    private void initlist() {


        Intent intent = this.getIntent();
        String[] info = (String[]) intent.getSerializableExtra("info");
        Integer[] color=new Integer[]{
                R.color.nsdk_swap_holo_bule_bright,
                R.color.nsdk_swap_holo_pure_bright,
                R.color.purple_200,
                R.color.nsdk_rg_operable_notification_subtitle_shop_time_red};
        Integer[] TEST_DATAS = new Integer[]{};





    /*
        Toast.makeText(this,
                info[0] + "\n" + info[1] + "\n" + info[2] + "\n" + info[3], Toast.LENGTH_SHORT).show();
    ***/
        try {
            spacelist = parkidDao.getspaceinfo(info[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int i=0;
        for (HashMap<String, String> m : spacelist){

            int element=color[i%3];
            TEST_DATAS = Arrays.copyOf(TEST_DATAS, TEST_DATAS.length +1);
            TEST_DATAS[TEST_DATAS.length - 1] = element;
            i++;
        }

            LayoutInflater inflater =getLayoutInflater();

        Integer[] finalTEST_DATAS = TEST_DATAS;
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mTestStackAdapter.updateData(Arrays.asList(finalTEST_DATAS),spacelist);
                    }
                }
                , 200
        );


    }

    @Override
    public void onItemExpend(boolean expend) {

    }
}
