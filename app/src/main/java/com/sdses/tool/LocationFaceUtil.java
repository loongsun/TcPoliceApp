package com.sdses.tool;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lb.baidumapdemo.db.DBConstants;
import com.lb.baidumapdemo.db.ShareDB;
import com.lb.baidumapdemo.face.LocationFace;

import android.content.Context;


public class LocationFaceUtil implements BDLocationListener {
	private LocationFace locationFace;
	public LocationClient mLocationClient = null;
	private Context context;

	public LocationFaceUtil(Context context, LocationFace locationFace) {
		super();
		this.locationFace = locationFace;
		this.context=context;
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(LocationFaceUtil.this);
		startLocation();
	}

	private void startLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(0);
		option.setIsNeedAddress(true);
		option.setOpenGps(true);//
		option.setLocationNotify(true);
		option.setIsNeedLocationDescribe(true);
		option.setIsNeedLocationPoiList(true);
		option.setIgnoreKillProcess(false);
		option.SetIgnoreCacheException(false);
		option.setEnableSimulateGps(false);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		if (locationFace != null)
			if (arg0.getLocType() == 61 || arg0.getLocType() == 161 && arg0.getLatitude() != 0.0) {
				new ShareDB(context).save(DBConstants.CITY_NAME, arg0.getCity());
				locationFace.locationResult(arg0);
			}
	}



}
