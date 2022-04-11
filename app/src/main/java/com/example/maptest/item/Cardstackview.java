package com.example.maptest.item;



import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.TextView;


import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.R;


import java.util.HashMap;
import java.util.List;

public class Cardstackview extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final List<HashMap<String,String>> mData;

    public Cardstackview(Context context,List<HashMap<String,String>> data){
        this.mData=data;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View root=mInflater.inflate(R.layout.space_layout,null);;

        TextView id;
       Button botton;
       TextView price;
        TextView freetime1;
        TextView freetime2;


        HashMap<String,String> map=mData.get(i);



        id = root.findViewById(R.id.space_id);
        price = root.findViewById(R.id.space_price);
        freetime1 = root.findViewById(R.id.space_freetime1);
        freetime2 = root.findViewById(R.id.space_freetime2);
        botton=root.findViewById(R.id.buy);




        id.setText(map.get("spaceid"));
        price.setText(map.get("price"));
        String freetime = map.get("freetime");

        assert freetime != null;
        freetime1.setText(freetime.substring(0, 2) + ":" + freetime.substring(2, 4));
        freetime2.setText(freetime.substring(4, 6) + ":" + freetime.substring(6));
        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(root.getContext(),LendActivity.class);
                String[] info=new String[]{map.get("spaceid"),map.get("freetime"),map.get("price")};
                intent.putExtra("info",info);
                root.getContext().startActivity(intent);
            }
        });

        return root;
    }
}
