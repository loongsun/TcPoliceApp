package com.tc.dbhandle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	static final String TAG = DBHelper.class.getSimpleName();
	public static final String TABLE_Basic = "Basic";
	public static final String TABLE_Info = "Info";
	public static final String TABLE_Err="Error";
	
	static final String SQL_TABLE_Info = "CREATE TABLE IF NOT EXISTS dbJqInfo(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "dbjqname VARCHAR(50),dbjqtime VARCHAR(30),"
			+ "dbjqNum VARCHAR(50),dbjqwx VARCHAR(20),dbjqwy VARCHAR(20),"
			+ "dbjqaddress VARCHAR(200),dbjqbjrdh VARCHAR(20),"
			+ "dbjqbjrname VARCHAR(50),flag VARCHAR(5),"
			+ "isPoliceGo VARCHAR(5),isArrive VARCHAR(5),"
			+ "isPoliceGoTime VARCHAR(30),isArriveTime VARCHAR(30))";
	
	
	static final String SQL_TABLE_LsInfo = "CREATE TABLE IF NOT EXISTS lsJqInfo(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "lsjqname VARCHAR(50),lsjqtime VARCHAR(30),"
			+ "lsjqNum VARCHAR(50),lsjqwx VARCHAR(20),lsjqwy VARCHAR(20),"
			+ "lsjqaddress VARCHAR(200),lsjqbjrdh VARCHAR(20),lsjqbjrname VARCHAR(50))";
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// super(context, name, factory, version);
		super(context, name, null, version);
		//this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.w(TAG, "DBHelper::onCreate()");
		// db.execSQL("create table if not exists Basic(name varchar(20))");
		db.execSQL(SQL_TABLE_Info);
		db.execSQL(SQL_TABLE_LsInfo);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "DBHelper::onUpgrade()");
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "DBHelper::onDowngrade()");
		//super.onDowngrade(db, oldVersion, newVersion);
	}

	
}
