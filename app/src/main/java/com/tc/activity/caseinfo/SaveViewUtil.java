package com.tc.activity.caseinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.sdses.tool.Values;

public class SaveViewUtil {
	
	private static final File rootDir = new File(Values.PATH_XCBL_XSAJ_PMT);

	/**保存截图的方法*/
	public static boolean saveScreen(View view,String fileName){
		boolean ret=false;
		//判断sdcard是否可用
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			return false;
		}
		if(!rootDir.exists()){
			Log.e("e", "文件夹不存在");
			rootDir.mkdirs();
		}else{
			Log.e("e", "存在");
		}
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		try {
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(new File(rootDir,fileName)));
			ret=true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ret=false;
		}
		finally
		{
			view.setDrawingCacheEnabled(false);
			bitmap = null;
			return ret;
		}
	}
}
