package com.tc.activity.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
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
import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.MainTabActivity;
import com.tc.activity.SenceExcute;
import com.tc.app.TcApp;
import com.tc.application.R;

public class MarkerMapActivity extends Activity implements
		OnMarkerClickListener, OnMapClickListener {
	private MapView mBaiduMapView; // 地图界面
	private BaiduMap mBaiduMap; // 地图的管理类
	PoliceStateListBean plb;
	// private String[] titles = new String[] { "one", "two", "three", "four" };
	// private LatLng[] latlngs = new LatLng[] { new
	// LatLng(22.539895,114.058935), new LatLng(22.540729,114.066337),
	// new LatLng(22.543763,114.06458), new LatLng(22.538614,114.062811) };
	private Button btn_policeGo, btn_arriveSence;
	TcApp mApp;
	private String errorMessage="";
	// 初始化控件
	
	private void initWidgets() {
		mBaiduMapView = (MapView) findViewById(R.id.markermap);
		btn_policeGo = (Button) findViewById(R.id.btn_policeGo);
		btn_arriveSence = (Button) findViewById(R.id.btn_arriveSence);
		btn_policeGo.setOnClickListener(new OnClick());
		btn_arriveSence.setOnClickListener(new OnClick());
		mApp = (TcApp) getApplication();
		mApp.initDatabase();
		if(Values.JQ_STATESBTN==2){
			btn_policeGo.setText("已出警");
		}else{
			btn_policeGo.setText("确认出警");
		}
	}

	class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_policeGo:// 确认出警
				if(btn_policeGo.getText().toString().equals("确认出警")){
					  new Thread(polickGoRun).start();
				}else{
					UtilTc.myToast(getApplicationContext(), "已出警");
				}
			  
				break;
			case R.id.btn_arriveSence:
				//确认出警后才可到达现场
				mApp.getmDota().jq_queryTime(plb.getJqNum());
				if(Values.POLICE_GOTIME==null||Values.POLICE_GOTIME.equals("")){
					UtilTc.myToast(getApplicationContext(), "请先确认出警");
				}else{
					ContentValues mContentValue = new ContentValues();
					mContentValue.clear();
					UtilTc.showLog("UtilTc.btn_arriveSence()"+UtilTc.getCurrentTime());
					mContentValue.put("isArriveTime", UtilTc.getCurrentTime());
					mApp.getmDota().update(Values.DB_JQINFO, 
							mContentValue, "dbjqNum=?", new String[]{plb.getJqNum()});
					arriveSence();
				}
				break;
			default:
				break;
			}
		}
	}

	// 切换状态
	private void policeGo() {
			//转为待办警情
			UtilTc.myToast(getApplicationContext(), "出警成功,服务器已接到通知");
			ContentValues mContentValues=new ContentValues();
			mContentValues.put("isPoliceGo", 0);
			mContentValues.put("isArrive",1);
			mContentValues.put("flag", 0);
			mContentValues.put("dbjqname", plb.getJqName());
			mContentValues.put("dbjqtime", plb.getJqTime());
			mContentValues.put("dbjqNum", plb.getJqNum());
			mContentValues.put("dbjqwx", plb.getWx());
			mContentValues.put("dbjqwy", plb.getWy());
			mContentValues.put("dbjqaddress", plb.getJqPosition());
			mContentValues.put("dbjqbjrdh", plb.getBjrPhone());
			mContentValues.put("dbjqbjrname", plb.getBjrName());
			mContentValues.put("isPoliceGoTime", UtilTc.getCurrentTime());
			mApp.getmDota().insert(Values.DB_JQINFO, mContentValues);
	}

	private void arriveSence() {
		ContentValues mContentValues = new ContentValues();
		mContentValues.clear();
		mContentValues.put("isArrive", 0);
		mApp.getmDota().update(Values.DB_JQINFO, 
				mContentValues, "dbjqNum=?", new String[]{plb.getJqNum()});
		Intent intent = new Intent();
		Bundle bun = new Bundle();
		bun.putSerializable("jqInfo", plb);
		bun.putSerializable("dbjqsence", plb.getJqNum());
		intent.setClass(MarkerMapActivity.this, SenceExcute.class);
		intent.putExtras(bun);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_markermap);
		getjQInfo();
		initWidgets();
		mBaiduMap = mBaiduMapView.getMap();
		mBaiduMap.setOnMapClickListener(this);
		initMarker();
	}

	// 获取信息位置
	private void getjQInfo() {
		Intent intent = getIntent();
		plb = (PoliceStateListBean) intent.getSerializableExtra("ssjqInfo");

	}

	private void initMarker() {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		// 设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);
		MapStatusUpdate statusUpdate = MapStatusUpdateFactory.zoomTo(17);
		mBaiduMap.setMapStatus(statusUpdate);
		BitmapDescriptor descriptor = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 循环添加四个覆盖物到地图上
		// for (int i = 0; i < titles.length; i++) {
		// latLng=latlngs[i];
		// overlayOptions = new
		// MarkerOptions().position(latLng).icon(descriptor);
		// // 将覆盖物添加到地图上
		// Marker marker=(Marker) mBaiduMap.addOverlay(overlayOptions);
		// Bundle bundle = new Bundle();
		// bundle.putString("info",titles[i]+"个");
		// marker.setExtraInfo(bundle);
		// }

		latLng = new LatLng(Double.valueOf(plb.getWy()), Double.valueOf(plb
				.getWx()));
		overlayOptions = new MarkerOptions().position(latLng).icon(descriptor);
		Marker marker = (Marker) mBaiduMap.addOverlay(overlayOptions);
		Bundle bundle = new Bundle();
		marker.setExtraInfo(bundle);
		// 将最后一个坐标设置为地图中心
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(u);
		mBaiduMap.setOnMarkerClickListener(MarkerMapActivity.this);
		
		final String msg = marker.getExtraInfo().getString("info");
		InfoWindow mInfoWindow;
		
	}

	/**
	 * @Title: onMarkerClick
	 * @Description: 覆盖物点击事件,每次点击一个覆盖物则会在相应的覆盖物上显示一个InfoWindow
	 * @param marker
	 * @return
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		final String msg = marker.getExtraInfo().getString("info");
		InfoWindow mInfoWindow;
		// 生成一个TextView用户在地图中显示InfoWindow
		TextView location = new TextView(getApplicationContext());
		location.setBackgroundResource(R.drawable.shape_popup);
		location.setPadding(30, 20, 30, 20);
		location.setTextColor(getResources().getColor(R.color.Black));
		location.setText(plb.getJqPosition());
		final LatLng ll = marker.getPosition();
		Point p = mBaiduMap.getProjection().toScreenLocation(ll);
		p.y -= 47;
		LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
		mInfoWindow = new InfoWindow(location, llInfo, -47);
		mBaiduMap.showInfoWindow(mInfoWindow);
		location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MarkerMapActivity.this, plb.getJqPosition(),
						Toast.LENGTH_SHORT).show();
			}
		});
		return true;
	}

	/**
	 * @Title: onMapClick
	 * @Description: 地图点击事件，点击地图的时候要让InfoWindow消失
	 * @param arg0
	 */
	@Override
	public void onMapClick(LatLng arg0) {
		mBaiduMap.hideInfoWindow();

	}

	/**
	 * @Title: onMapPoiClick
	 * @Description: 兴趣点点击事件
	 * @param arg0
	 * @return
	 */
	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//出警
	Runnable polickGoRun=new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url_passenger ="http://61.176.222.166:8765/interface/wchange/";
			 HttpPost httpRequest =new HttpPost(url_passenger);  
			 List <NameValuePair> params=new ArrayList<NameValuePair>();  
			 UtilTc.showLog("改变状态案件号码："+plb.getJqNum());
			 params.add(new BasicNameValuePair("wnum", plb.getJqNum()));	
			 params.add(new BasicNameValuePair("wstate", "1"));	
			 
			 try{
				 UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
				 httpRequest.setEntity(formEntity);  
			     //取得HTTP response  
			     HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);  
			     Log.e("code", "code"+httpResponse.getStatusLine().getStatusCode());
			     if(httpResponse.getStatusLine().getStatusCode()==200){  
			    	 String strResult=EntityUtils.toString(httpResponse.getEntity()); 
			    	 Log.e("e", "传回来的值是："+strResult);
			    	 //json 解析
			    	  JSONTokener jsonParser = new JSONTokener(strResult); 
			    	  JSONObject person = (JSONObject) jsonParser.nextValue();    
			    	  String code=person.getString("error code"); 
			    	  if(code.trim().equals("0")){
			    		  //成功
			    		  UtilTc.showLog("出警成功");
			    		  mHandler.sendEmptyMessage(Values.SUCCESS_POLICEGO);
			    	  }else if(code.trim().equals("10003")){
			    		  //需要解析出message
			    		  JSONObject jb=person.getJSONObject("data");
			    		  errorMessage=jb.getString("message");
			    		  mHandler.sendEmptyMessage(Values.ERROR_OTHER);
			    	  }
			     }else{
			    	 mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
			     }
			 }catch(Exception e){
				 e.printStackTrace();
				 mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
			 }
		}
	};
	Handler  mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Values.SUCCESS_POLICEGO:
				btn_policeGo.setText("已出警");
				policeGo();
				break;
			case Values.ERROR_CONNECT:
				UtilTc.myToast(getApplicationContext(), "出警失败,请检查服务器网络连接");
				break;
			case Values.ERROR_OTHER:
				UtilTc.myToast(getApplicationContext(), "出警失败"+errorMessage);
				break;
			default:
				break;
			}
		};
	};
}
