package com.example.maptest.baidumap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.maptest.R;

import java.util.List;

public class PoiAdapter extends ArrayAdapter<PoiInfo> {
    private int resourceId;
    public PoiAdapter(@NonNull Context context, int resource, @NonNull List<PoiInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PoiInfo poi=getItem(position);
        @SuppressLint("ViewHolder")
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView name=view.findViewById(R.id.poiname);
        TextView address=view.findViewById(R.id.poiaddress);
        name.setText(poi.name);
        address.setText(poi.address);
        return view;
    }
}