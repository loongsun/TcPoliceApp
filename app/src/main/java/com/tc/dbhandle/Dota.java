package com.tc.dbhandle;

import java.io.File;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dota {
	private static final String TAG = Dota.class.getSimpleName();
	public static final String DB_NAME_BASIC = "jqInfo.db";
	static final int DB_VERSION_BASIC = 1;
	private final DBHelper mDBHelper;
	static final int DB_VERSION_RECORD = 1;
	private long mResult;
	Context mContext;
	public Dota(Context context) {
		mDBHelper = new DBHelper(context, DB_NAME_BASIC, null, DB_VERSION_BASIC);
		this.mContext = context;
		Log.w(TAG, "Dota Initialized Database");
	}

	public synchronized void close() {
		// TODO Auto-generated method stub
		if (mDBHelper != null)
			mDBHelper.close();
	}

	public void LogDatabaseInfo() {
		SQLiteDatabase db = null;
		Log.e("e", "LogDatabaseInfo");
		try {
			db = mDBHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(String database_name) {
		boolean isFileCreateSuccess = false;
		File dbFile = new File(database_name);
		if (dbFile.exists()) {
			isFileCreateSuccess = dbFile.delete();//
		} else
			isFileCreateSuccess = false;
		return isFileCreateSuccess;
	}

	public synchronized long insert(String tableName,
			ContentValues mContentValues) {
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			mResult = db.insertOrThrow(tableName, null, mContentValues);
			
		} finally {
			if (db != null)
				db.close();
		}
		return mResult;
	}

	public synchronized long update(String tableName,
			ContentValues mContentValues, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
				db = mDBHelper.getWritableDatabase();
			mResult = db.update(tableName, mContentValues, whereClause,
					whereArgs);
		} finally {
			if (db != null)
				db.close();
		}
		return mResult;
	}
	
	
	
	public synchronized boolean dbjq_getCount(String dbjqNum) {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from dbJqInfo where dbjqNum = ?;",
					new String[] {dbjqNum});
			
			boolean ss=cursor.moveToFirst();
			UtilTc.showLog("cursor"+ss);
			if (cursor != null && cursor.moveToFirst()){
				return true;
			}else{
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (mDBHelper != null)
				mDBHelper.close();
			
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return false;
	}
	public synchronized int dbJq_DeleteRecord(String dbjqNum) {
		int back = -1;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			back = db.delete(Values.DB_JQINFO, "dbjqNum=?",
					new String[] { dbjqNum });
//			db.delete(DBHelper.SQL_TABLE_Info,
//					"Time < date('now','-30 day')", null);
		} catch (Exception e) {
			e.printStackTrace();
			mDBHelper.close();
		} finally {
			if (db != null)
				db.close();
		}
		return back;
	}
	
	public synchronized boolean jq_query(String states) {
		if(states.equals("0"))
		Values.dbjqList.clear();
		else
		Values.lsjqList.clear();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from dbJqInfo where flag=?;",
					new String[] {states});
			if (cursor != null ){
				while(cursor.moveToNext()){
					PoliceStateListBean plb=new PoliceStateListBean();
					plb.setJqName(cursor.getString(1));
					plb.setJqTime(cursor.getString(2));
					plb.setJqNum(cursor.getString(3));
					plb.setWx(cursor.getString(4));
					plb.setWy(cursor.getString(5));
					plb.setJqPosition(cursor.getString(6));
					plb.setBjrPhone(cursor.getString(7));
					plb.setBjrName(cursor.getString(8));
					if(states.equals("0"))
					Values.dbjqList.add(plb);
					else
				    Values.lsjqList.add(plb);
				}
				return true;
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mDBHelper != null)
				mDBHelper.close();
			// deleteDatabase();
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return false;
	}
	
	
	public synchronized String jq_ToMapQuery(String jqNum) {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from dbJqInfo where dbjqNum=?;",
					new String[] {jqNum});
			if (cursor != null ){
				cursor.moveToFirst();
				return  cursor.getString(11);
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mDBHelper != null)
				mDBHelper.close();
			// deleteDatabase();
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return null;
	}
	
	public synchronized boolean jq_queryOne(String states,String jqNum) {
		if(states.equals("0"))
		Values.dbjqList.clear();
		else
		Values.lsjqList.clear();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from dbJqInfo where flag=? and dbjqNum=?;",
					new String[] {states,jqNum});
			if (cursor != null ){
				while(cursor.moveToNext()){
					PoliceStateListBean plb=new PoliceStateListBean();
					plb.setJqName(cursor.getString(1));
					plb.setJqTime(cursor.getString(2));
					plb.setJqNum(cursor.getString(3));
					plb.setWx(cursor.getString(4));
					plb.setWy(cursor.getString(5));
					plb.setJqPosition(cursor.getString(6));
					plb.setBjrPhone(cursor.getString(7));
					plb.setBjrName(cursor.getString(8));
					if(states.equals("0"))
					Values.dbjqList.add(plb);
					else
				    Values.lsjqList.add(plb);
				}
				return true;
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mDBHelper != null)
				mDBHelper.close();
			// deleteDatabase();
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return false;
	}
	
	public synchronized boolean jq_queryTime(String jqNum) {
		Values.POLICE_GOTIME="";
		Values.POLICE_ARRIVETIME="";
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from dbJqInfo where dbjqNum=?;",
					new String[] {jqNum});
			if (cursor != null ){
				cursor.moveToFirst();
				Values.POLICE_GOTIME=cursor.getString(12);
				Values.POLICE_ARRIVETIME=cursor.getString(13);
				UtilTc.showLog("Values.POLICE_GOTIME"+Values.POLICE_GOTIME);
				UtilTc.showLog("Values.POLICE_ARRIVETIME"+Values.POLICE_ARRIVETIME);
				return true;
				}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			if (mDBHelper != null)
				mDBHelper.close();
			// deleteDatabase();
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return false;
	}
}
