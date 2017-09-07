package com.tc.util;

import android.content.Context;
import android.widget.Toast;

import com.huatuban.MainActivity;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zhao on 17-8-17.
 */

public class FileUtil {

    public static File getSaveFile(Context context){
        File file = new File(context.getFilesDir(),"pic.jpg");
        return file;
    }
    public static boolean getLog() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());
            if(format.compareTo("2017-09-11 00:00:00")>0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
