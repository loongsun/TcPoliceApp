package com.tc.app;

import java.io.File;

import loongsun.com.facetest.reconova.client.FcsApiClient;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.lb.baidumapdemo.face.LocationFace;
import com.tc.dbhandle.DatabaseContext;
import com.tc.dbhandle.Dota;

public class TcApp extends Application {
    private FcsApiClient client;
	//

	public static Context mContent;
	private String sdPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	// 数据库操作实例
	public static Dota mDota;

	public Dota getmDota() {
		return mDota;
	}

	public void onCreate() {
		super.onCreate();
		mContent = getApplicationContext();
		// 地图初始化
	 	SDKInitializer.initialize(getApplicationContext());
		new com.sdses.tool.LocationFaceUtil(getApplicationContext(),
				new LocationFace() {
					@Override
					public void locationResult(BDLocation location) {

					}
				});
		// 文件夹初始化
		createFileForSenceCheck();
		//数据库初始化
	}

	private void createFileForSenceCheck() {
		try {
			//tc 文件夹
			File filerc = new File(sdPath + "/TC");
			if (!filerc.exists()) {
				filerc.mkdir();
			}
			// 笔录文件夹
			File file = new File(sdPath + "/TC/wtxt");
			if (!file.exists()) {
				file.mkdir();
			}
			// 录音文件夹
			File file1 = new File(sdPath + "/TC/wrecord");
			if (!file1.exists()) {
				file1.mkdir();
			}
			// 录像文件夹
			File file2 = new File(sdPath + "/TC/wcamera");
			if (!file2.exists()) {
				file2.mkdir();
			}
			// 照片文件夹
			File file3 = new File(sdPath + "/TC/wphoto");
			if (!file3.exists()) {
				file3.mkdir();
			}
		} catch (Exception e) {
		}
	}

	public void initDatabase() {
		try {
			DatabaseContext dbContext = new DatabaseContext(mContent);
			mDota = new Dota(dbContext);
			// 根据数据库内数据初始化各参数
			mDota.LogDatabaseInfo();
		} catch (Exception e) {
			
		}
	}
	 public FcsApiClient getClient() {
	        return client;
	    }

	    public void setClient(FcsApiClient client) {
	        this.client = client;
	    }
}
