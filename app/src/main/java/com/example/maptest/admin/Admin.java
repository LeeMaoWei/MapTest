package com.example.maptest.admin;
import static com.baidu.location.LocationClient.setAgreePrivacy;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.EditText;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.RoutePlanSearch;

import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.example.maptest.MySQL.dao.ParkDao;
import com.example.maptest.R;
import com.example.maptest.baidumap.AddressToLatitudeLongitude;
import com.example.maptest.baidumap.MyOrientationListener;
import com.example.maptest.baidumap.PoiAdapter;

import com.example.maptest.item.Spacetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Objects;

public class Admin extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Spinner spiProvince,spiCity,spiArea;
    private ArrayAdapter<String> adaProvince,adaCity,adaArea;
    private List<String> proList,cityList,areaList;
    private int position;
    private MapView myMapView = null;//????????????
    private BaiduMap myBaiduMap;//??????????????????
    private LocationClient mylocationClient;//????????????????????????
    private MylocationListener mylistener;//??????????????????
    private Context context;
    private String the_name;
    private String myCity;
    LatLng currentPt;
    TextView info;
    Marker flag;

    private double myLatitude;//????????????????????????????????????????????????
    private double myLongitude;//????????????????????????????????????????????????
    private float myCurrentX;

    private BitmapDescriptor myIconLocation1;//??????1??????????????????????????????
    private BitmapDescriptor myIconLocation2;//??????2,???????????????????????????

    private MyOrientationListener myOrientationListener;//????????????????????????

    private MyLocationConfiguration.LocationMode locationMode;//????????????????????????


    private RoutePlanSearch mSearch = null;

    private SuggestionSearch mSuggestionSearch = null;
    // ???????????????????????????
    private EditText mEditCity = null;
    private AutoCompleteTextView mKeyWordsView = null;
    private ListView mSugListView;

    private PoiSearch mPoiSearch=null;
    private AutoCompleteTextView texttemp;
    private String myAdCode;

    GeoCoder mySearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.admin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        this.context = this;
        initView();
        try {
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(this.context,username,Toast.LENGTH_LONG).show();
        AutoCompleteTextView myEditText_site = findViewById(R.id.editText_site);
        this.texttemp=myEditText_site;

        myEditText_site.setThreshold(1);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
        mySearch = GeoCoder.newInstance();
        mySearch.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);



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
                    String cityname=myCity;
                    if(the_name!=null)
                    {
                        cityname=the_name;
                    }
                    mPoiSearch.searchInCity(new PoiCitySearchOption()
                            .city(cityname)
                            .keyword(cs.toString())
                            .pageNum(0));
                }catch (Exception ignored){

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        spiProvince = findViewById(R.id.sheng);
        spiCity = findViewById(R.id.shi);
        spiArea = findViewById(R.id.qu);

        proList = new ArrayList<String>();
        cityList = new ArrayList<String>();
        areaList = new ArrayList<String>();

        initData();
        adaProvince = new ArrayAdapter<String>(this,R.layout.spinner_item,proList);
        //adaCity = new ArrayAdapter<String>(this,R.layout.spinner_item,cityList);
        //adaArea = new ArrayAdapter<String>(this,R.layout.spinner_item,areaList);

        adaProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adaCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spiProvince.setAdapter(adaProvince);
        //spiCity.setAdapter(adaCity);
        //spiArea.setAdapter(adaArea);

        spiProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setWherebyName(String[] name,int n ){
        String x="";
        for(int i=0;i<n;i++){
            x=x+name[i];
        }
        AddressToLatitudeLongitude at = new AddressToLatitudeLongitude(x);
        at.getLatAndLngByAddress();
        getLocationByLL(at.getLatitude(),at.getLongitude());


    }


    public void loadSpinner(){

            String selectedItem = (String) spiProvince.getSelectedItem();

            cityList.clear();
            cityList=initData(selectedItem);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin.this,R.layout.spinner_item,cityList);
                spiCity.setAdapter(adapter);
                spiCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        areaList.clear();
                        String chensg = (String) spiCity.getSelectedItem();
//                            System.out.println("??????");
                       areaList=initData(selectedItem,chensg);

                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(Admin.this,R.layout.spinner_item,areaList);
                        spiArea.setAdapter(adapters);
                        spiArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String x=(String) spiArea.getSelectedItem();
                                the_name=chensg;
                                String name[]={selectedItem,chensg,x};
                                setWherebyName(name,3);


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        }








    final OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener () {
        /**
         * ???????????????????????????????????????
         *
         * @param result
         */
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(Admin.this, "???????????????????????????", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            //myBaiduMap.clear();
            flag = (Marker) myBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_markb)));
            myBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
            @SuppressLint("DefaultLocale") String strInfo = String.format("?????????%f ?????????%f",
                    result.getLocation().latitude, result.getLocation().longitude);
            Toast.makeText(Admin.this, strInfo, Toast.LENGTH_LONG).show();





            dialog();


        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(Admin.this, "???????????????????????????", Toast.LENGTH_LONG)
                        .show();
                return;
            }
           // myBaiduMap.clear();
            flag = (Marker)myBaiduMap.addOverlay(
                    new MarkerOptions()
                            .position(result.getLocation())                                     //????????????
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markb))  //??????
                            .title(result.getAddress())                                         //??????

            );
            myBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
            Toast.makeText(Admin.this, result.getAddress(),
                    Toast.LENGTH_LONG).show();
            myAdCode=String.valueOf(result.getAdcode());
            myMark(myAdCode);

            /**
             * ??????InfoWindow???????????????
             */
            dialog();

        }
    };

    /**
     * ????????????
     * @param latLng
     */
    public void reverseSearch(LatLng latLng)
    {
        mySearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }




    public List<String> initData(){
        try {
            InputStream inputStream = getResources().getAssets().open("province.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuffer.toString());
            for(int i =0; i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String proName = jsonObject.getString("name");
                //???????????????
                Log.e(TAG,"proName = " + proName);
                proList.add(proName);


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return proList;
    }

    public List<String> initData(String proname){
        try {
            InputStream inputStream = getResources().getAssets().open("province.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuffer.toString());
            for(int i =0; i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String proName = jsonObject.getString("name");
                if(!proName.equals(proname)){
                    continue;
                }
                Log.e(TAG,"proName = " + proName);
                JSONArray cityArray = jsonObject.getJSONArray("city");
                for (int j=0; j<cityArray.length(); j++){
                    JSONObject cityObject = cityArray.getJSONObject(j);
                    String cityName = cityObject.getString("name");
                    //???????????????
                    Log.e(TAG,"cityName = " + cityName);
                    cityList.add(cityName);
                }
                break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public List<String> initData(String proname,String cityname){
        try {
            InputStream inputStream = getResources().getAssets().open("province.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuffer.toString());
            for(int i =0; i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String proName = jsonObject.getString("name");
                if(!proName.equals(proname)){
                    continue;
                }
                JSONArray cityArray = jsonObject.getJSONArray("city");
                for (int j=0; j<cityArray.length(); j++){
                    JSONObject cityObject = cityArray.getJSONObject(j);
                    String cityName = cityObject.getString("name");
                    //???????????????
                    if(!cityName.equals(cityname)){
                        continue;
                    }
                    Log.e(TAG,"cityName = " + cityName);
                    JSONArray areaArray = cityObject.getJSONArray("area");
                    for (int k=0 ; k<areaArray.length(); k++){
                        String areaName = areaArray.getString(k);
                        //??????????????????
                        Log.e(TAG,"areaName = " + areaName);
                        areaList.add(areaName);
                    }
                    break;

                }
                break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return areaList;
    }



    private void initView() {
        myMapView = (MapView) findViewById(R.id.baiduMapView);

        myBaiduMap = myMapView.getMap();
        //????????????????????????????????????
        MapStatusUpdate msu= MapStatusUpdateFactory.zoomTo(18.0f);
        myBaiduMap.setMapStatus(msu);
    }


    private void initLocation() throws Exception {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        mylocationClient = new LocationClient(context);
        mylistener = new MylocationListener();

        //???????????????
        mylocationClient.registerLocationListener(mylistener);
        //????????????SDK??????????????????????????????????????????????????????????????????????????????
        LocationClientOption mOption = new LocationClientOption();
        //??????????????????
        mOption.setCoorType("bd09ll");
        //???????????????????????????????????????????????????
        mOption.setIsNeedAddress(true);
        //??????????????????gps????????????
        mOption.setOpenGps(true);
        //???????????????????????????????????? ???<1000(1s)????????????????????????
        int span = 1000;
        mOption.setScanSpan(span);
        //?????? LocationClientOption
        mylocationClient.setLocOption(mOption);

        //???????????????,BitmapDescriptorFactory???bitmap ?????????????????????.
        //myIconLocation1 = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
        //myIconLocation2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);

        //??????????????????????????????,????????????????????????
        MyLocationConfiguration configuration
                = new MyLocationConfiguration(locationMode, true, myIconLocation1);
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? setMyLocationEnabled(boolean)
        myBaiduMap.setMyLocationConfigeration(configuration);

        myOrientationListener = new MyOrientationListener(context);
        //????????????????????????????????????????????????
        myOrientationListener.setOnOrientationListener(x -> myCurrentX = x);

    }



    /*
     *??????????????????
     */
    @SuppressLint("NonConstantResourceId")



    /*
     *?????????????????????
     */
    public void getLocationByLL(double la, double lg)
    {
        //???????????????????????????
        LatLng latLng = new LatLng(la, lg);
        //???????????????????????????????????????,???????????????????????????????????????????????????
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.setMapStatus(msu);
    }



    /*
     *????????????????????????
     */
    public class MylocationListener implements BDLocationListener {
        //????????????????????????
        private boolean isFirstIn = true;

        //????????????????????????,??????????????????????????????
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation ??????????????????????????????????????????????????????????????????????????????
            //MyLocationData ????????????,?????????????????????
            /*
             * ????????????BDLocation??????????????????
             * 1.accuracy ????????????
             * 2.latitude ??????????????????
             * 3.longitude ??????????????????
             * 4.satellitesNum GPS????????????????????? getSatelliteNumber() gps????????????????????????gps?????????????????????
             * 5.speed GPS??????????????? getSpeed()??????????????????gps?????????????????????????????????????????????/??????????????????0.0f
             * 6.direction GPS?????????????????????
             * */
            myLatitude = bdLocation.getLatitude();
            myLongitude = bdLocation.getLongitude();
            myAdCode= bdLocation.getAdCode();
            myCity = bdLocation.getCity();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(myCurrentX)//??????????????????
                    .accuracy(bdLocation.getRadius())//getRadius ??????????????????,?????????0.0f
                    .latitude(myLatitude)//??????????????????
                    .longitude(myLongitude)//??????????????????
                    .build();
            //??????????????????, ??????????????????????????????????????????????????????????????? setMyLocationEnabled(boolean)
            myBaiduMap.setMyLocationData(data);

            //??????????????????????????????,??????????????????????????????????????????
            if (isFirstIn) {
                //???????????????????????????????????????
                getLocationByLL(myLatitude, myLongitude);
                isFirstIn = false;
                //??????????????????????????????
//                Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                myMark(myAdCode);
            }




        }
    }

    /*
     *??????????????????????????????????????????
     */






    final OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            //??????????????????
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
                        // ???????????????????????????
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //????????????
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

        //??????

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
        for(int i=0;i<list.size();i++) {
            HashMap<String, String> map = list.get(i);
            double lat = Double.parseDouble(Objects.requireNonNull(map.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(map.get("lng")));
            String name = map.get("name");
            String id = map.get("id");
            LatLng point = new LatLng(lat, lng);
            Bundle bundle = new Bundle();
            String[] info = {map.get("lat"), map.get("lng"), name, id};
            bundle.putStringArray("info", info);
            //??????Marker??????
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_markb);
            //??????MarkerOption???????????????????????????Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .extraInfo(bundle);
            //info???????????????????????????


            myBaiduMap.addOverlay(option).setExtraInfo(bundle);

            //??????????????????Marker????????????
            //myBaiduMap.addOverlay(option);
        }
            myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //???marker?????????info??????
                    Bundle bundle = marker.getExtraInfo();

                    String[] info=bundle.getStringArray("info");


                    Intent intent1 = new Intent(Admin.this, Spacetable.class);
                    intent1.putExtra("info",bundle.getStringArray("info"));

                    startActivity(intent1);



                    return true;
                }
            });
            myBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    /**
                     * ?????????????????????
                     */
                    currentPt = latLng;
                    /**
                     * ??????????????????
                     */
                    reverseSearch(latLng);
                   //0 dialog();
                }
            });




    }
    /**
     * ??????InfoWindow???????????????
     */
    protected void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Admin.this);

        builder.setMessage("?????????????"+myAdCode);

        builder.setCancelable(false);//?????????????????????????????????
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent1 = new Intent(Admin.this, AddaSpace.class);
                String info[]={myAdCode, String.valueOf(flag.getPosition().latitude),String.valueOf(flag.getPosition().longitude)};
                intent1.putExtra("info",info);
                startActivity(intent1);
            }

        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                flag.remove();
            }

        });
        builder.create().show();
    }
  /*
    public void getInfoFromLAL(PoiInfo info) {
        LatLng point=new LatLng(info.getLocation().latitude,info.getLocation().longitude);

        mySearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
        mySearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.e("???????????????????????????", "??????????????????");
                } else {

                    Toast.makeText(context,(String.valueOf(result.getAdcode())),Toast.LENGTH_LONG).show();
                    myMark(String.valueOf(result.getAdcode()));
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

            }
        });

    }

    public void getInfoFromLAL(LatLng info) {
        LatLng point=new LatLng(info.latitude,info.longitude);

        mySearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
        mySearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.e("???????????????????????????", "??????????????????");
                } else {

                    Toast.makeText(context,(String.valueOf(result.getAdcode())),Toast.LENGTH_LONG).show();
                    myMark(String.valueOf(result.getAdcode()));

                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

            }
        });

    }
*/


    public void onStart() {
        super.onStart();
        //?????????????????????????????????
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
        //????????????
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





