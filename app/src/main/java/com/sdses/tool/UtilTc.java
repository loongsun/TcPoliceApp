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
		Toast.makeText(context, "连接失败,请检查网络和服务器", Toast.LENGTH_LONG).show();
	}
	public static void showLog(String log){
		if(isLog)
		Log.e("tc", log);
	}
	
	//获取当前时间
	public static String getCurrentTime(){
	 SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");     
	 Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间     
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
			return  "未知"+gender;
		}else{
			return genderint==1?"男":"女";
		}
	}

	public static  boolean createDirectory() {
		boolean flag = false;

		// 创建FTP客户端
		FTPClient ftpClient = new FTPClient();

		try {
			// 建立FTP连接
			ftpClient.connect("61.176.222.166",21);
			// 如果登录成功
			ftpClient.login("admin", "1234");

		 		// 切换文件路径, 到FTP上的"xxx"文件夹下
			  //   ftpClient.changeDirectory("../");
			     ftpClient.createDirectory("planPhoto");
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				// 关闭连接
				ftpClient.disconnect(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return flag;
	}


}
