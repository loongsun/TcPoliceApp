package com.tc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


	public class HomeCrashHandler implements UncaughtExceptionHandler {

		public static final String TAG = "crash";
	    private UncaughtExceptionHandler mDefaultHandler;
	    
	    private static HomeCrashHandler INSTANCE = new HomeCrashHandler();
	    private Context mContext;
	    private Map<String, String> infos = new HashMap<String, String>();

	    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	  
	    private HomeCrashHandler() {
	    	 
	    }

	    public static HomeCrashHandler getInstance() {
	    
	        return INSTANCE;
	    }

	    public void init(Context context) {
	        mContext = context;
	        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	       
	        Thread.setDefaultUncaughtExceptionHandler(this);
	    }

	    @Override
	    public void uncaughtException(Thread thread, Throwable ex) {
	    	if (!handleException(ex) && mDefaultHandler != null) {
	            mDefaultHandler.uncaughtException(thread, ex);
	        } else 
	    	{
	            try {
	                Thread.sleep(300);
	            } catch (InterruptedException e) {
	                Log.e(TAG, "error : ", e);
	            }
	            android.os.Process.killProcess(android.os.Process.myPid());  
	            System.exit(1);  
	          }
	    	
	    }

	    private boolean handleException(Throwable ex) {
	        if (ex == null) {
	            return false;
	        }
	       
	        new Thread() {
	            @Override
	            public void run() {
	                Looper.prepare();
	                Toast.makeText(mContext, "程序出错啦,我们会尽快修改...", Toast.LENGTH_SHORT).show(); 
	                Looper.loop();
	            }
	        }.start();
	      
	        collectDeviceInfo(mContext);
	        Log.i("crash", "ex:"+ex.toString()+"--"+ex.getLocalizedMessage());
	        saveCrashInfo2File(ex);
	        return true;
	    }
	    
	    public void collectDeviceInfo(Context ctx) {
	        try {
	            PackageManager pm = ctx.getPackageManager();
	            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
	            if (pi != null) {
	                String versionName = pi.versionName == null ? "null" : pi.versionName;
	                String versionCode = pi.versionCode + "";
	                infos.put("versionName", versionName);
	                infos.put("versionCode", versionCode);
	            }
	        } catch (NameNotFoundException e) {
	            Log.e(TAG, "an error occured when collect package info", e);
	        }
	        Field[] fields = Build.class.getDeclaredFields();
	        for (Field field : fields) {
	            try {
	                field.setAccessible(true);
	                infos.put(field.getName(), field.get(null).toString());
	            } catch (Exception e) {
	                Log.e(TAG, "an error occured when collect crash info", e);
	            }
	        }
	    }
//log信息存放位置
	    private String saveCrashInfo2File(Throwable ex) {
	        
	        StringBuffer sb = new StringBuffer();

	        Writer writer = new StringWriter();
	        PrintWriter printWriter = new PrintWriter(writer);
	        ex.printStackTrace(printWriter);
	        Throwable cause = ex.getCause();
	        while (cause != null) {
	        	Log.i("HomeHotelShowPro", "cause:"+cause.toString()+"--");
	            cause.printStackTrace(printWriter);
	            cause = cause.getCause();
	        }
	        printWriter.close();
	        String result = writer.toString();
	        Log.i("XXXXXXX", "result:"+result);
	        sb.append(result);
	        try {
	            long timestamp = System.currentTimeMillis();
	            String time = formatter.format(new Date());
	            String fileName = "crash-" + time + "-" + timestamp + ".log";
	            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	                String path = "/sdcard/HomeHotelShowPro/crash/";
	                File dir = new File(path);
	                if (!dir.exists()) {
	                    dir.mkdirs();
	                }
	                FileOutputStream fos = new FileOutputStream(path + fileName);
	                fos.write(sb.toString().getBytes());
	                fos.close();
	            }
	            return fileName;
	        } catch (Exception e) {
	            Log.e(TAG, "an error occured while writing file...", e);
	        }
	        return null;
	    }
	}