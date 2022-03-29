package com.example.maptest.item;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;



import androidx.appcompat.app.AppCompatActivity;

import com.example.maptest.MySQL.dao.LockDao;

import com.example.maptest.R;

import com.example.maptest.myhome.ui.notifications.NotificationsFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;






public class AddLock extends AppCompatActivity {


    private static final String TAG = AddLock.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static MqttAndroidClient mqttAndroidClient;

    private EditText status;
    private EditText clientIdEt;
    private String username;
    private  Button connBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        username = (String) intent.getSerializableExtra("username");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.addlock);

        status = findViewById(R.id.status);
        clientIdEt = findViewById(R.id.clientId);
        connBtn = findViewById(R.id.connBtn);
        //timeText = findViewById(R.id.timeText);



        connBtn.setOnClickListener(v -> {
            String topic = clientIdEt.getText().toString();
            String lokename = status.getText().toString();
            Boolean flag;
            connect();
            flag=link(lokename,topic,username);


            if(flag){
                Intent intent1 =new Intent(AddLock.this, NotificationsFragment.class);
                intent1.putExtra("username",username);
                startActivity(intent1);
            }




        });




    }
    public Boolean link(String str1,String str2,String str3){
        LockDao lockDao = new LockDao();
        publishMessage(str1,"1"+username);
        return lockDao.Link(str1, str2, str3);
    }
    /**
     * String host = "tcp://58.16.134.114:48989";
     * String clientId = "android_mqtt_client";
     * String userName = "admin";
     * String passWord = "mf@123";
     */
    public void connect() {
        String host = "tcp://124.222.110.226:1883";
        String clientId = clientIdEt.getText().toString();
        String userName = "admin";
        String passWord = "public";
        if (clientId.isEmpty()) {
            clientId = "android_mqtt_client";
        }

        /* 创建MqttConnectOptions对象，并配置username和password。 */
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

        mqttConnectOptions.setUserName(userName);


        mqttConnectOptions.setPassword(passWord.toCharArray());


        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect(); //断开连接
            } catch (MqttException e) {
                e.printStackTrace();
            }
            mqttAndroidClient = null;
        }

        /* 创建MqttAndroidClient对象，并设置回调接口。 */
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), host, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                String msg = "";
                if (cause != null) {
                    msg = "," + cause.getMessage();
                }
                Log.i(TAG, "连接掉线>host：" + host + msg);
                status.setText("未连接互联网");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.i(TAG, "topic: " + topic + ", msg: " + new String(message.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "已发送");
            }
        });



        /* 建立MQTT连接。 */
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "连接成功：" + host);

                    status.setText("未绑定" );


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    String msg = "";
                    if (exception != null) {
                        msg = "," + exception.getMessage();
                    }
                    Log.i(TAG, "连接失败>host：" + host + msg);
                    status.setText("未连接至服务器" );
                    assert exception != null;
                    exception.printStackTrace();
                }
            });

        } catch (MqttException e) {
            Log.i(TAG, "连接失败：" + e.getMessage());
            status.setText("未连接" );
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String payload) {
        try {
            if (!mqttAndroidClient.isConnected()) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "发布成功>topic:" + topic + ",payload:" + payload);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "发布失败>topic:" + topic + ",payload:" + payload);
                    if (exception != null) {
                        exception.printStackTrace();
                    }

                }
            });
        } catch (MqttException e) {
            Log.i(TAG, "发布失败>topic:" + topic + ",payload:" + payload);
            e.printStackTrace();

        }
    }



    @Override
    public void onDestroy() {
        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect(); //断开连接
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }



}