package com.example.maptest.item;

import static com.baidu.location.LocationClient.setAgreePrivacy;

import android.annotation.SuppressLint;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;

import android.widget.AbsListView;

import android.widget.AutoCompleteTextView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;

import com.example.maptest.MySQL.dao.ParkDao;
import com.example.maptest.R;
import com.example.maptest.baidumap.MyOrientationListener;
import com.example.maptest.baidumap.PoiAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class FindPark extends AppCompatActivity {

    private MapView myMapView = null;//地图控件
    private BaiduMap myBaiduMap;//百度地图对象
    private LocationClient mylocationClient;//定位服务客户对象
    private MylocationListener mylistener;//重写的监听类
    private Context context;

    private String myCity;

    private double myLatitude;//纬度，用于存储自己所在位置的纬度
    private double myLongitude;//经度，用于存储自己所在位置的经度
    private float myCurrentX;

    private BitmapDescriptor myIconLocation1;//图标1，当前位置的箭头图标
    private BitmapDescriptor myIconLocation2;//图表2,前往位置的中心图标

    private MyOrientationListener myOrientationListener;//方向感应器类对象

    private LocationMode locationMode;//定位图层显示方式
//    private MyLocationConfiguration.LocationMode locationMode2;//定位图层显示方式

    private LinearLayout myLinearLayout1; //经纬度搜索区域1
    private LinearLayout myLinearLayout2; //地址搜索区域2

    private RoutePlanSearch mSearch = null;

    private SuggestionSearch mSuggestionSearch = null;
    // 搜索关键字输入窗口
    private EditText mEditCity = null;
    private AutoCompleteTextView mKeyWordsView = null;
    private ListView mSugListView;

    private PoiSearch mPoiSearch=null;
    private AutoCompleteTextView texttemp;
    private String myAdCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.find_park);
        this.context = this;
        initView();
        try {
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AutoCompleteTextView myEditText_site = findViewById(R.id.editText_site);
        this.texttemp=myEditText_site;

        myEditText_site.setThreshold(1);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);



        myEditText_site.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {

                if (cs.length() <= 0) {
                    return;
                }
                try {
                    mPoiSearch.searchInCity(new PoiCitySearchOption()
                            .city(myCity)
                            .keyword(cs.toString())
                            .pageNum(0));
                }catch (Exception ignored){

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void initView() {
        myMapView = (MapView) findViewById(R.id.baiduMapView);

        myBaiduMap = myMapView.getMap();
        //根据给定增量缩放地图级别
        MapStatusUpdate msu= MapStatusUpdateFactory.zoomTo(18.0f);
        myBaiduMap.setMapStatus(msu);
    }


    private void initLocation() throws Exception {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mylocationClient = new LocationClient(context);
        mylistener = new MylocationListener();

        //注册监听器
        mylocationClient.registerLocationListener(mylistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mylocationClient.setLocOption(mOption);

        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        //myIconLocation1 = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
        //myIconLocation2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);

        //配置定位图层显示方式,三个参数的构造器
        MyLocationConfiguration configuration
                = new MyLocationConfiguration(locationMode, true, myIconLocation1);
        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
        myBaiduMap.setMyLocationConfigeration(configuration);

        myOrientationListener = new MyOrientationListener(context);
        //通过接口回调来实现实时方向的改变
        myOrientationListener.setOnOrientationListener(x -> myCurrentX = x);

    }



    /*
     *创建菜单操作
     */
    @SuppressLint("NonConstantResourceId")



    /*
     *根据经纬度前往
     */
    public void getLocationByLL(double la, double lg)
    {
        //地理坐标的数据结构
        LatLng latLng = new LatLng(la, lg);
        //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.setMapStatus(msu);
    }

    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    /*
     *定位请求回调接口
     */
    public class MylocationListener implements BDLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /*
             * 可以通过BDLocation配置如下参数
             * 1.accuracy 定位精度
             * 2.latitude 百度纬度坐标
             * 3.longitude 百度经度坐标
             * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
             * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
             * 6.direction GPS定位时方向角度
             * */
            myLatitude = bdLocation.getLatitude();
            myLongitude = bdLocation.getLongitude();
            myAdCode= bdLocation.getAdCode();
            myCity = bdLocation.getCity();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(myCurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(myLatitude)//百度纬度坐标
                    .longitude(myLongitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            myBaiduMap.setMyLocationData(data);

            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                //根据当前所在位置经纬度前往
                getLocationByLL(myLatitude, myLongitude);
                isFirstIn = false;
                //提示当前所在地址信息
//                Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                myMark(myAdCode);
            }




        }
    }

    /*
     *定位服务的生命周期，达到节省
     */






    final OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            //显示搜索结果
            List<PoiInfo> poiList = poiResult.getAllPoi();
            PoiAdapter adapter = new PoiAdapter(context, R.layout.item_layout, poiList);
            ListView listView = findViewById(R.id.json_lv);
            try {
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            listView.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener((parent, view, position, id) -> {

                PoiInfo myend = (PoiInfo) listView.getItemAtPosition(position);

                //startNavi(myend);
                LatLng point = new LatLng(myend.getLocation().latitude, myend.getLocation().longitude);
                getLocationByLL(myend.getLocation().latitude, myend.getLocation().longitude);

                adapter.clear();

            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged (AbsListView view,int scrollState) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //加载更多
                            int curPage = poiResult.getCurrentPageNum();
                            int totalPage = poiResult.getTotalPageNum();
                            if (curPage < totalPage) {
                                poiResult.setCurrentPageNum(curPage + 1);
                                String city = myCity;


                                String keyWord = texttemp.getText().toString();
                                mPoiSearch.searchInCity(new PoiCitySearchOption()
                                        .city(city)
                                        .keyword(keyWord)
                                        .pageNum(curPage + 1));

                            }
                        }
                    }
                }
                @Override
                public void onScroll (AbsListView absListView,int i, int i1, int i2){

                }
            });
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }

        //废弃

        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };

    public void myMark(String adCode){
        List<HashMap<String,String>> list= new ArrayList<>();
        ParkDao parkDao = new ParkDao();
        try {
            list=parkDao.getinfo(adCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(int i=0;i<list.size();i++){
            HashMap<String,String> map= list.get(i);
            double lat = Double.parseDouble(Objects.requireNonNull(map.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(map.get("lng")));
            String name = map.get("name");
            String id = map.get("id");
            LatLng point = new LatLng(lat, lng);
            Bundle bundle = new Bundle();
            String[] info={map.get("lat"),map.get("lng"),name,id};
            bundle.putStringArray("info",info);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_markb);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .extraInfo(bundle);
            //info必须实现序列化接口



            myBaiduMap.addOverlay(option).setExtraInfo(bundle);

            //在地图上添加Marker，并显示
            //myBaiduMap.addOverlay(option);

            myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //从marker中获取info信息
                    Bundle bundle = marker.getExtraInfo();

                    String[] info=bundle.getStringArray("info");
                    Toast.makeText(context,
                            info[0]+info[1]+info[2], Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(FindPark.this, Spacetable.class);
                    intent1.putExtra("info",bundle.getStringArray("info"));

                    startActivity(intent1);



                    return true;
                }
            });
        }
    }
    public void onStart() {
        super.onStart();
        //开启定位，显示位置图标
        myBaiduMap.setMyLocationEnabled(true);
        if(!mylocationClient.isStarted())
        {
            mylocationClient.start();
        }
        myOrientationListener.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        myBaiduMap.setMyLocationEnabled(false);
        mylocationClient.stop();
        myOrientationListener.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        myMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        myMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMapView.onDestroy();
    }



}

