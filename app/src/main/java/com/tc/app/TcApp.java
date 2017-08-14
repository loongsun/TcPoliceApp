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
	// ���ݿ����ʵ��
	public static Dota mDota;

	public Dota getmDota() {
		return mDota;
	}

	public void onCreate() {
		super.onCreate();
		mContent = getApplicationContext();
		// ��ͼ��ʼ��
	 	SDKInitializer.initialize(getApplicationContext());
		new com.sdses.tool.LocationFaceUtil(getApplicationContext(),
				new LocationFace() {
					@Override
					public void locationResult(BDLocation location) {

					}
				});
		// �ļ��г�ʼ��
		createFileForSenceCheck();
		//���ݿ��ʼ��
	}

	private void createFileForSenceCheck() {
		try {
			//tc �ļ���
			File filerc = new File(sdPath + "/TC");
			if (!filerc.exists()) {
				filerc.mkdir();
			}
			// ��¼�ļ���
			File file = new File(sdPath + "/TC/wtxt");
			if (!file.exists()) {
				file.mkdir();
			}
			// ¼���ļ���
			File file1 = new File(sdPath + "/TC/wrecord");
			if (!file1.exists()) {
				file1.mkdir();
			}
			// ¼���ļ���
			File file2 = new File(sdPath + "/TC/wcamera");
			if (!file2.exists()) {
				file2.mkdir();
			}
			// ��Ƭ�ļ���
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
			// �������ݿ������ݳ�ʼ��������
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
