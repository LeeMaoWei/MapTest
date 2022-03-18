package com.example.maptest.myhome.ui.mymap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.maptest.MySQL.enity.Lock;

import java.util.ArrayList;
import java.util.List;

public class LockcardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Lock> mList = new ArrayList<>();


    public LockcardAdapter(Context context, List<Lock> list) {
        mContext = context;
        mList = list;
    }



    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
