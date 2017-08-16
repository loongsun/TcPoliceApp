package com.lb.baidumapdemo.db;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareDB {
	private  SharedPreferences shareDB;
	

	public ShareDB(Context context) {
		super();
		if (shareDB == null) {
			shareDB = context.getSharedPreferences(DBConstants.SHARE_NAME, 0);
		}
	}



	public String getValue(String key) {
		return shareDB.getString(key, "");
	}

	public void save(String key, String value) {
		shareDB.edit().putString(key, value).commit();
	}
}
