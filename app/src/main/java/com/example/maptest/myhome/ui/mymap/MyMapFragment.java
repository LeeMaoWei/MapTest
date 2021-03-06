package com.example.maptest.myhome.ui.mymap;


import static com.baidu.location.LocationClient.setAgreePrivacy;
import static com.example.maptest.myhome.HomeActivity.mSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;

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
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.navisdk.adapter.struct.BNTTsInitConfig;

import com.example.maptest.MySQL.dao.ParkDao;
import com.example.maptest.R;
import com.example.maptest.baidumap.DemoGuideActivity;
import com.example.maptest.baidumap.MyOrientationListener;
import com.example.maptest.baidumap.PoiAdapter;
import com.example.maptest.databinding.FragmentMyMapBinding;
import com.example.maptest.item.Spacetable;
import com.example.maptest.myhome.HomeActivity;
import com.example.maptest.utils.BNDemoUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyMapFragment extends Fragment {

    private static final String APP_FOLDER_NAME ="MyBNDTSDK-Api" ;
    private String mSDCardPath = null;

    private TextureMapView myMapView = null;//????????????
    private BaiduMap myBaiduMap;//??????????????????
    private LocationClient mylocationClient;//????????????????????????
    private MylocationListener mylistener;//??????????????????
    private Context context;

    private String myCity;
    private String myAdCode;

    private double myLatitude;//????????????????????????????????????????????????
    private double myLongitude;//????????????????????????????????????????????????
    private float myCurrentX;

    private BitmapDescriptor myIconLocation1;//??????1??????????????????????????????


    private MyOrientationListener myOrientationListener;//????????????????????????

    private MyLocationConfiguration.LocationMode locationMode;//????????????????????????
    //    private MyLocationConfiguration.LocationMode locationMode2;//????????????????????????
    private PoiSearch mPoiSearch = null;


    private FragmentMyMapBinding binding;
    private AutoCompleteTextView texttemp;
    private String username;
    private View view = null ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        this.context = getActivity();
        setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(context.getApplicationContext(),true);
        SDKInitializer.initialize(context.getApplicationContext());
        binding = FragmentMyMapBinding.inflate(inflater, container, false);
        this.view=binding.getRoot();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toast.makeText(getActivity(),username,Toast.LENGTH_LONG).show();

        //??????poi????????????
        mPoiSearch = PoiSearch.newInstance();

        super.onCreate(savedInstanceState);
        initView();
        try {
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        AutoCompleteTextView myEditText_site = view.findViewById(R.id.editText_site);
        this.texttemp=myEditText_site;

        myEditText_site.setThreshold(1);

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
        if(initDirs()){
            initGuide();
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        myMapView =  view.findViewById(R.id.baiduMapView);
        myBaiduMap = myMapView.getMap();
        //????????????????????????????????????
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
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


    private void initGuide() {
        //???????????????

        BaiduNaviManagerFactory.getBaiduNaviManager().init(context, mSDCardPath, APP_FOLDER_NAME,
                new IBaiduNaviManager.INaviInitListener() {
                    @Override
                    public void onAuthResult(int i, String s) {
                        if (i == 0) {
                            Toast.makeText(context, "key????????????!", Toast.LENGTH_SHORT).show();
                        } else if (i == 1) {
                            Toast.makeText(context, "key????????????, " + s, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void initStart() {

                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(context, "?????????????????????????????????", Toast.LENGTH_SHORT).show();

                        initTTs();

                    }

                    @Override
                    public void initFailed(int i) {
                        Toast.makeText(getActivity(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    //?????????????????????
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initTTs() {

        BNTTsInitConfig config = new BNTTsInitConfig.Builder()
                .context(context.getApplicationContext())
                .sdcardRootPath(getSdcardDir())
                .appFolderName(APP_FOLDER_NAME)
                .appId(BNDemoUtils.getTTSAppID())
                .appKey(BNDemoUtils.getTTSAppKey())
                .secretKey(BNDemoUtils.getTTSsecretKey())
                .build();
        BaiduNaviManagerFactory.getTTSManager().initTTS(config);
        // ??????????????????tts????????????
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );
        // ????????????tts ??????????????????
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }










    /*
     *?????????????????????
     */
    public void getLocationByLL(double la, double lg) {
        //???????????????????????????
        LatLng latLng = new LatLng(la, lg);
        //???????????????????????????????????????,???????????????????????????????????????????????????
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.setMapStatus(msu);
    }


    /*BNCommonSettingParam.
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
            String[] info = {map.get("lat"), map.get("lng"), name, id, username};
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
                    Toast.makeText(context,
                            info[0]+info[1]+info[2], Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(getActivity(), Spacetable.class);
                    intent1.putExtra("info",bundle.getStringArray("info"));

                    startActivity(intent1);

                   //LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);


                   //startNavi(infoUtil);

                    return true;
                }
            });

    }

    //??????poi???????????????
    final OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            //??????????????????
            List<PoiInfo> poiList = poiResult.getAllPoi();
            PoiAdapter adapter = new PoiAdapter(context, R.layout.item_layout, poiList);
            ListView listView = view.findViewById(R.id.json_lv);
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
                getInfoFromLAL(myend);
                adapter.clear();

            });

           /* listView.setOnScrollListener(new AbsListView.OnScrollListener()
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
        });*/
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

    public void getInfoFromLAL(PoiInfo info) {
        LatLng point=new LatLng(info.getLocation().latitude,info.getLocation().longitude);

        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

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
    private void startNavi(PoiInfo mDestation) {
        if(mDestation==null)
            return;
        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                .latitude(myLatitude)
                .longitude(myLongitude)
                .name("????????????")
                .description("????????????")
                .build();
        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                .latitude(mDestation.getLocation().latitude)
                .longitude(mDestation.getLocation().longitude)
                .name(mDestation.name)
                .description(mDestation.name)
                .build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManagerFactory.getRoutePlanManager().routePlanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(context.getApplicationContext(),
                                        "??????????????????????????????", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context,
                                        DemoGuideActivity.class);
                                intent.putExtra("isRealNavi", true);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }

                });
    }


    private void startNavi(String[] mDestation) {
        if(mDestation==null)
            return;
        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                .latitude(myLatitude)
                .longitude(myLongitude)
                .name("????????????")
                .description("????????????")
                .build();
        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                .latitude(Double.parseDouble(mDestation[0]))
                .longitude(Double.parseDouble(mDestation[1]))
                .name(mDestation[2])
                .description(mDestation[2])
                .build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManagerFactory.getRoutePlanManager().routePlanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(context.getApplicationContext(),
                                        "????????????", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(context.getApplicationContext(),
                                        "??????????????????????????????", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context,
                                        DemoGuideActivity.class);
                                intent.putExtra("isRealNavi", true);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }

                });
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }
        username=((HomeActivity)context).getTitles();
    }

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
    public void onStop() {
        super.onStop();
        //????????????
        myBaiduMap.setMyLocationEnabled(false);
        mylocationClient.stop();
        myOrientationListener.stop();
    }
    @Override
    public void onResume() {
        myMapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        myMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        myMapView.onDestroy();
        myMapView = null;
    }




}







