package com.example.maptest.item;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.maptest.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceAdapter extends BaseAdapter{

    private List<HashMap<String,String>>  mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

    /*
    定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    */
    public SpaceAdapter(LayoutInflater inflater,List<HashMap<String,String>> data){
        mInflater = inflater;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        //获得ListView中的view
        @SuppressLint({"ViewHolder", "InflateParams"})
        View view = mInflater.inflate(R.layout.space_layout,null);
        //获得学生对象
        HashMap<String, String> map  = mData.get(position);
        //获得自定义布局中每一个控件的对象。
        TextView id = (TextView) view.findViewById(R.id.space_id);
        TextView price = (TextView) view.findViewById(R.id.space_price);
        TextView freetime1 = (TextView) view.findViewById(R.id.space_freetime1);
        TextView freetime2 = (TextView) view.findViewById(R.id.space_freetime2);
        //将数据一一添加到自定义的布局中。
        id.setText(map.get("spaceid"));
        price.setText(map.get("price"));
        String freetime=map.get("freetime");
        assert freetime != null;
        freetime1.setText(freetime.substring(0,2)+":"+freetime.substring(2,4));
        freetime2.setText(freetime.substring(4,6)+":"+freetime.substring(6));
        return view;
    }
}