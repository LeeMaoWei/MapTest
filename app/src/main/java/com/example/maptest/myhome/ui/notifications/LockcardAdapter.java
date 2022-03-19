package com.example.maptest.myhome.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.R;

import java.util.ArrayList;
import java.util.List;

public class LockcardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Lock> mList = new ArrayList<>();

    class ViewHolder {
        TextView Lockname,start_time,end_time;
        ImageView lockpic,edit_lock,delete_lock;
        Switch power;

    }


    public LockcardAdapter(Context context, List<Lock> list) {
        mContext = context;
        mList = list;
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent) {
       ViewHolder viewHolder=null;
       LockDao lockDao=new LockDao();
       if(view==null) {
        viewHolder = new ViewHolder();
        view = LayoutInflater.from(mContext).inflate(R.layout.lock_layout,null);
        viewHolder.delete_lock=view.findViewById(R.id.delete_lock);
        viewHolder.edit_lock=view.findViewById(R.id.edit_lock);
        viewHolder.Lockname=view.findViewById(R.id.lockname);
        viewHolder.lockpic=view.findViewById(R.id.lockpic);
        viewHolder.end_time=view.findViewById(R.id.end_time);
        viewHolder.start_time=view.findViewById(R.id.start_time);
        viewHolder.end_time=view.findViewById(R.id.end_time);
        viewHolder.power=view.findViewById(R.id.power);
        view.setTag(viewHolder);
       }else {
           viewHolder = (ViewHolder) view.getTag();
       }
        viewHolder.Lockname.setText(mList.get(i).getLockname());
        viewHolder.start_time.setText(mList.get(i).getFreetime().substring(0, 2) + ":" + mList.get(i).getFreetime().substring(2, 4));
        viewHolder.end_time.setText(mList.get(i).getFreetime().substring(4, 6) + ":" + mList.get(i).getFreetime().substring(6));
        if(mList.get(i).getState()==1)
        {
            viewHolder.lockpic.setImageResource(R.drawable.ic_lock__1_);
            viewHolder.power.setChecked(true);
        }
        else
        {
            viewHolder.lockpic.setImageResource(R.drawable.ic_unlock);
            viewHolder.power.setChecked(false);
        }
    ;
        ViewHolder finalViewHolder = viewHolder;
        viewHolder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    finalViewHolder.lockpic.setImageResource(R.drawable.ic_lock__1_);
                }else{
                    finalViewHolder.lockpic.setImageResource(R.drawable.ic_unlock);
                }
            }
        });
        return view;
    }

}
