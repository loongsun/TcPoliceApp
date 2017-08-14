package com.tc.dbhandle;

import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseContext extends ContextWrapper {

	private static final String TAG = DatabaseContext.class.getSimpleName();

	public DatabaseContext(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
	}
	/**
	 * ������ݿ�·������������ڣ��򴴽��������
	 * 
	 * @param name
	 * @param mode
	 * @param factory
	 */
	@Override
	public File getDatabasePath(String name) {
		// �ж��Ƿ����sd��
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// ���������,
			Log.w("SD������", "SD�������ڣ������SD��");
			return null;
		} else {// �������
				// ��ȡsd��·��
			String dbDir = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			dbDir += "/TC";// ���ݿ�����Ŀ¼
			String dbPath = dbDir + "/" + name;// ���ݿ�·��
			// �ж�Ŀ¼�Ƿ���ڣ��������򴴽���Ŀ¼
			File dirFile = new File(dbDir);
			if (!dirFile.exists()){
				dirFile.mkdirs();
				Log.e("e", "�������ݿ�");
			}
			//Log.w("meng","dbPath="+dbPath);
			// ���ݿ��ļ��Ƿ񴴽��ɹ�
			boolean isFileCreateSuccess = false;
			// �ж��ļ��Ƿ���ڣ��������򴴽����ļ�
			File dbFile = new File(dbPath);
			if (!dbFile.exists()) {
				try {
					isFileCreateSuccess = dbFile.createNewFile();// �����ļ�
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{
				//Log.w(TAG,"�ļ�����");
				isFileCreateSuccess = true;
			}

			// �������ݿ��ļ�����
			if (isFileCreateSuccess)
				return dbFile;
			else
				return null;
		}
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = null;
		try{
			File tmp = getDatabasePath(name);
			result = SQLiteDatabase.openOrCreateDatabase(tmp, factory);
		}catch(Exception e){
			if(result!=null){
				Log.w(TAG,"���ݿ�ر�");
				result.close();
			}
			else{
				//errorHandler.onCorruption(result);
				Log.w(TAG,"���ݿ�δ��");
			}
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
	
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(name), factory);
		return result;
		// return super.openOrCreateDatabase(name, mode, factory);
	}

}
