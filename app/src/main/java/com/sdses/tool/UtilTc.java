package com.sdses.tool;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import it.sauronsoftware.ftp4j.FTPClient;

public class UtilTc {
	private static boolean isLog=true;
	public static String sdPath=Environment.getExternalStorageDirectory().getAbsolutePath();
	public static void myToast(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	public static void myToastForContent(Context context){
		Toast.makeText(context, "����ʧ��,��������ͷ�����", Toast.LENGTH_LONG).show();
	}
	public static void showLog(String log){
		if(isLog)
		Log.e("tc", log);
	}
	
	//��ȡ��ǰʱ��
	public static String getCurrentTime(){
	 SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");     
	 Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
	 return   formatter.format(curDate);    
	 
 }
	public static List<Size> getResolutionList(Camera camera) {
		Parameters parameters = camera.getParameters();
		List<Size> previewSizes = parameters.getSupportedPreviewSizes();
		return previewSizes;
	}

	public static class ResolutionComparator implements Comparator<Size> {

		@Override
		public int compare(Size lhs, Size rhs) {
			if (lhs.height != rhs.height)
				return lhs.height - rhs.height;
			else
				return lhs.width - rhs.width;
		}
	}
	   public static boolean isExitsSdcard() {
	        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	            return true;
	        else
	            return false;
	    }

	public static String filterForObject(Object Obj) {
		String str = "";
		if (Obj == null) {
			str = "";
		} else if (Obj.toString().trim().equalsIgnoreCase("null")) {
			str = "";
		} else {
			str = Obj.toString();
		}
		return str.trim();
	}

	public static boolean checkConditionOk(String con){
		if(con!=null&&!con.equals("")){
			return true;
		}
		return false;
	}
	public static String getGender(String gender){
		int genderint=Integer.parseInt(gender);
		if(genderint!=1&&genderint!=2){
			return  "δ֪"+gender;
		}else{
			return genderint==1?"��":"Ů";
		}
	}

	public static  boolean createDirectory() {
		boolean flag = false;

		// ����FTP�ͻ���
		FTPClient ftpClient = new FTPClient();

		try {
			// ����FTP����
			ftpClient.connect("61.176.222.166",21);
			// �����¼�ɹ�
			ftpClient.login("admin", "1234");

		 		// �л��ļ�·��, ��FTP�ϵ�"xxx"�ļ�����
			  //   ftpClient.changeDirectory("../");
			     ftpClient.createDirectory("planPhoto");
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				// �ر�����
				ftpClient.disconnect(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return flag;
	}


}
