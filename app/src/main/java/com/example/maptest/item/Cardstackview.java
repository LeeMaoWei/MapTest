package com.example.maptest.item;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.maptest.R;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

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


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root=mInflater.inflate(R.layout.space_layout,null);;

        TextView id;
        TextView price;
        TextView freetime1;
        TextView freetime2;


        HashMap<String,String> map=mData.get(i);



        id = root.findViewById(R.id.space_id);
        price = root.findViewById(R.id.space_price);
        freetime1 = root.findViewById(R.id.space_freetime1);
        freetime2 = root.findViewById(R.id.space_freetime2);
        System.out.println("CardViewHolder constructor");



        id.setText(map.get("spaceid"));
        price.setText(map.get("price"));
        String freetime = map.get("freetime");

        assert freetime != null;
        freetime1.setText(freetime.substring(0, 2) + ":" + freetime.substring(2, 4));
        freetime2.setText(freetime.substring(4, 6) + ":" + freetime.substring(6));

        System.out.println("holder onBind");
        return root;
    }
}
