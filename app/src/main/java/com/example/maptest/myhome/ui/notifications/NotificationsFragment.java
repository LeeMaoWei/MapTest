package com.example.maptest.myhome.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.maptest.MySQL.dao.LockDao;
import com.example.maptest.MySQL.enity.Lock;
import com.example.maptest.R;
import com.example.maptest.databinding.FragmentNotificationsBinding;
import com.example.maptest.myhome.HomeActivity;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        this.view=binding.getRoot();
        Toast.makeText(getActivity(),username,Toast.LENGTH_LONG).show();
        try {
            list=lockDao.getinfo(username,"1");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        lockcardAdapter=new LockcardAdapter(getActivity(),list);
        listView=view.findViewById(R.id.xx);
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