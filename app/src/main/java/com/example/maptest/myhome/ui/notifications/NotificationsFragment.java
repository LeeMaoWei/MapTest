package com.example.maptest.myhome.ui.notifications;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.dao.ParkidDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.R;
import com.example.maptest.databinding.FragmentNotificationsBinding;
import com.example.maptest.item.AddLock;
import com.example.maptest.item.EditLockActivity;
import com.example.maptest.myhome.HomeActivity;
import com.example.maptest.utils.Mqtt;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private Context context;
    private View view = null ;
    private FragmentNotificationsBinding binding;
    private List<Lock> list = new ArrayList<>();
    private LockDao lockDao=new LockDao();
    private String username;
    private ListView listView=null;
    private LockcardAdapter lockcardAdapter;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.context = getActivity();
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        this.view = inflater.inflate(R.layout.fragment_notifications, container, false);
        //Toast.makeText(getActivity(),username,Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {


                      }
        }).start();

        try {
            list=lockDao.getinfo(username);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        lockcardAdapter=new LockcardAdapter(this.getActivity(),list);
        listView=view.findViewById(R.id.xx);
        button = view.findViewById(R.id.addlock);
        button.setOnClickListener(v -> {
            Intent intent1 = new Intent(getActivity(), AddLock.class);
            intent1.putExtra("username",username);

            startActivity(intent1);
        });
        listView.setAdapter(lockcardAdapter);
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }
        username=((HomeActivity)context).getTitles();
    }


}

class LockcardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Lock> mList = new ArrayList<>();

    class ViewHolder {
        TextView Lockname, start_time, end_time;
        ImageView lockpic, edit_lock;
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
        ViewHolder viewHolder = null;
        LockDao lockDao = new LockDao();
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lock_layout, null);

            viewHolder.edit_lock = view.findViewById(R.id.edit_lock);
            viewHolder.Lockname = view.findViewById(R.id.lockname);
            viewHolder.lockpic = view.findViewById(R.id.lockpic);
            viewHolder.end_time = view.findViewById(R.id.end_time);
            viewHolder.start_time = view.findViewById(R.id.start_time);
            viewHolder.end_time = view.findViewById(R.id.end_time);
            viewHolder.power = view.findViewById(R.id.power);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.Lockname.setText(mList.get(i).getLockname());
        viewHolder.start_time.setText(mList.get(i).getFreetime().substring(0, 2) + ":" + mList.get(i).getFreetime().substring(2, 4));
        viewHolder.end_time.setText(mList.get(i).getFreetime().substring(4, 6) + ":" + mList.get(i).getFreetime().substring(6));

        if (mList.get(i).getLockstate()== 1) {
            viewHolder.lockpic.setImageResource(R.drawable.ic_lock__1_);
            viewHolder.power.setChecked(true);
        } else {
            viewHolder.lockpic.setImageResource(R.drawable.ic_unlock);
            viewHolder.power.setChecked(false);
        }

        ViewHolder finalViewHolder = viewHolder;

        //viewHolder.power.setClickable(false);

        viewHolder.power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    finalViewHolder.lockpic.setImageResource(R.drawable.ic_lock__1_);
                    Dao dao = new Dao();
                    dao.Lock(mList.get(i));
                } else {
                    finalViewHolder.lockpic.setImageResource(R.drawable.ic_unlock);
                    Dao dao = new Dao();
                    dao.Unlock(mList.get(i));
                }
            }
        });
        viewHolder.edit_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(view.getContext(), EditLockActivity.class);
                String[] lockinfo = new String[]{mList.get(i).getLockname(), mList.get(i).getFreetime(), String.valueOf(mList.get(i).getLockid()), mList.get(i).getParkid()};
                intent2.putExtra("lockinfo", lockinfo);
                view.getContext().startActivity(intent2);
            }
        });
        return view;
    }


    public class Dao {
        Mqtt mqtt = new Mqtt();
        private MqttAndroidClient mqttAndroidClient;

        public void Unlock(Lock lock) {

            lock.setState(0);
            LockDao lockDao = new LockDao();
            ParkidDao parkidDao = new ParkidDao();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockDao.lockstate(lock);
                    parkidDao.parkidstate(lock);
                }
            }).start();

        }

        public void Lock(Lock lock) {

            lock.setState(1);
            LockDao lockDao = new LockDao();
            ParkidDao parkidDao = new ParkidDao();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockDao.lockstate(lock);
                    parkidDao.parkidstate(lock);
                }
            }).start();
        }




}
}
