package com.tc.fragment;


import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tc.application.R;
import com.tc.bean.EventInfo;
import com.tc.bean.PowerBean;
import com.tc.bean.PowerInfo;
import com.tc.util.HttpUtil;
import com.tc.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMarkFragment extends Fragment {


    private static final String TAG = MyMarkFragment.class.getSimpleName();
    private static final int MSG_GET_DATA = 0x001;
    private static final String EVENT_INFO = "event_info";
    private static final String POWER_INFO = "power_info";
    private MapView mBaiduMapView;
    private Button mEventBtn;
    private Button mPoliceBtn;
    private BaiduMap mBaiduMap;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_DATA:
                    dismissDialog();
                    if(mPowerBean!=null){
                        Log.i(TAG,"get data success"+mPowerBean);
                        initEventOverlay();
                    }else{
                        Log.i(TAG,"get data error");
                    }
                    break;
            }
        }
    };
    private PowerBean mPowerBean;
    private CustomProgressDialog mProgressDialog;
    private ArrayList<Marker> mEvetMarkerList;
    private ArrayList<Marker> mPowerMarkerList;

    public MyMarkFragment() {
        // Required empty public constructor
    }

    private void showProgress(){
        if(mProgressDialog==null){
            mProgressDialog =  CustomProgressDialog.createDialog(getActivity());
        }
        mProgressDialog.show();
    }

    private void  dismissDialog(){
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"OnActivity create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"onCreateview");
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.fragment_my_mark, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreated");
        showProgress();
        new Thread(new GetPowerTask()).start();
    }

    public class GetPowerTask implements Runnable{

        @Override
        public void run() {
            String url = "http://61.176.222.166:8765/interface/OneMapLable/GetLables.asp";
            String data = HttpUtil.getDataFromURL(url);
            Log.i(TAG,"get data = "+data);
            mPowerBean = HttpUtil.parseData(data);
            mHandler.obtainMessage(MSG_GET_DATA).sendToTarget();

        }
    }

    private void initView(View view) {
        mBaiduMapView = (MapView)view.findViewById(R.id.markermap);
        mEventBtn = (Button)view.findViewById(R.id.btn_event);
        mPoliceBtn = (Button)view.findViewById(R.id.btn_police);
        mEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),MapFragmentDemoActivity.class);
//                getActivity().startActivity(intent);
                initEventOverlay();

            }
        });
        mPoliceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPowerOverlay();
            }
        });
        mBaiduMap = mBaiduMapView.getMap();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        initMarker();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
        if(mBaiduMapView!=null){
            mBaiduMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResueme");
        super.onResume();
        if(mBaiduMapView!=null){
            mBaiduMapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestory");
        if(mBaiduMapView!=null){
            mBaiduMapView.onDestroy();
        }
    }

    private void initPowerOverlay(){
        clearOverlay();
        if(mPowerBean==null){
            return ;
        }
        clearOverlay();
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
        List<PowerInfo> powerInfoList = mPowerBean.powerInfoList;
        mPowerMarkerList = new ArrayList<>();
        Random random = new Random(SystemClock.currentThreadTimeMillis());

        for(int i=0;powerInfoList!=null && i<powerInfoList.size();i++){
            PowerInfo powerInfo = powerInfoList.get(i);
            boolean bool = random.nextBoolean();
            if(i%2==0){
                bool = true;
            }else{
                bool = false;
            }
            double temp = bool?i*0.0001:-i*0.0001;
            double latitude = Double.valueOf(powerInfo.powerY)+temp;//纬度
            double longitude = Double.valueOf(powerInfo.powerX)-temp;//经度
            LatLng latlng = new LatLng(latitude,longitude);
            Log.i(TAG,"add power"+i);
            MarkerOptions markOption = new MarkerOptions().position(latlng).icon(descriptor).zIndex(9).draggable(false);
            Marker marker = (Marker)mBaiduMap.addOverlay(markOption);
            mEvetMarkerList.add(marker);
            Bundle bundle = new Bundle();
            bundle.putSerializable(POWER_INFO,powerInfo);
            marker.setExtraInfo(bundle);
        }

    }


    private void initEventOverlay(){
        if(mPowerBean==null){
            return;
        }
        clearOverlay();
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

        List<EventInfo> eventInfoList = mPowerBean.eventInfoList;
        if(mEvetMarkerList ==null){
            mEvetMarkerList = new ArrayList<Marker>();
        }
        Random random = new Random(SystemClock.currentThreadTimeMillis());

        for(int i=0;i<eventInfoList.size();i++){
            EventInfo eventInfo = eventInfoList.get(i);
            boolean bool = random.nextBoolean();
            if(i%2==0){
                bool = true;
            }else{
                bool = false;
            }
            double temp = bool?i*0.0001:-i*0.0001;
            double latitude = Double.valueOf(eventInfo.wY)+temp;//纬度
            double longitude = Double.valueOf(eventInfo.wX)-temp;//经度
            LatLng latlng = new LatLng(latitude,longitude);
            Log.i(TAG,"add event"+i+","+bool);
            MarkerOptions markOption = new MarkerOptions().position(latlng).icon(descriptor).zIndex(9).draggable(false);
            Marker marker = (Marker)mBaiduMap.addOverlay(markOption);
            mEvetMarkerList.add(marker);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EVENT_INFO,eventInfo);
            marker.setExtraInfo(bundle);
//            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latlng);
//            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
    }



    private void clearOverlay(){
        if(mBaiduMap!=null){
            mBaiduMap.clear();
        }
        if(mEvetMarkerList!=null){
            mEvetMarkerList.clear();
        }
        if(mPowerMarkerList!=null){
            mPowerMarkerList.clear();
        }
    }

    private void initMarker() {
        mBaiduMap.clear();
        //设置普通图的模式
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setTrafficEnabled(true);
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.zoomTo(17);
        mBaiduMap.setMapStatus(statusUpdate);
        //设置标注图
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        double wx= 123.329414;
        double wy=41.817323;
//        LatLng latLng = new LatLng(39.915071, 116.403907); // 默认 天安门
        LatLng latLng = new LatLng(wy,wx);
//        LatLng latLng = new LatLng(wx,wy);
        OverlayOptions overlayOptions = new MarkerOptions().position(latLng).icon(descriptor);

        Marker marker = (Marker)mBaiduMap.addOverlay(overlayOptions);

        Bundle bundle = new Bundle();
        marker.setExtraInfo(bundle);

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(mapStatusUpdate);


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                InfoWindow infoWindow ;
                TextView locationTx = new TextView(getActivity());
                locationTx.setBackgroundResource(R.drawable.shape_popup);
                locationTx.setPadding(30,20,30,20);
                locationTx.setTextColor(getResources().getColor(R.color.Black));
//                locationTx.setText("text");

                Bundle extraInfo = marker.getExtraInfo();
                if(extraInfo.containsKey(EVENT_INFO)){
                    EventInfo eventInfo = (EventInfo)extraInfo.getSerializable(EVENT_INFO);
                    locationTx.setText(eventInfo.wName+":"+eventInfo.wAddress);
                }else if(extraInfo.containsKey(POWER_INFO)){
                    PowerInfo powerInfo = (PowerInfo)extraInfo.getSerializable(POWER_INFO);
                    locationTx.setText(powerInfo.powerType);
                }

                LatLng latLng = marker.getPosition();
                Point point = mBaiduMap.getProjection().toScreenLocation(latLng);
                point.y -= 47;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(point);
                infoWindow = new InfoWindow(locationTx,llInfo,-47);
                mBaiduMap.showInfoWindow(infoWindow);
                locationTx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "handle", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });


    }
}
