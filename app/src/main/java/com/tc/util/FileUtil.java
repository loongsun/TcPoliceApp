package com.tc.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.huatuban.MainActivity;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhao on 17-8-17.
 */

public class FileUtil {

    public static File getSaveFile(Context context){
        File file = new File(context.getFilesDir(),"pic.jpg");
        return file;
    }
    public static boolean getLog() {





        URL url = null;//ȡ����Դ����
        try {
            url = new URL("http://www.ntsc.ac.cn");
            URLConnection uc = url.openConnection();//�������Ӷ���
            uc.connect(); //��������
            long ld = uc.getDate(); //ȡ����վ����ʱ��
            Date date=new Date(ld); //ת��Ϊ��׼ʱ�����
            //�ֱ�ȡ��ʱ���е�Сʱ�����Ӻ��룬�����
            int yyyy=date.getYear();
            System.out.print(date.getHours()+"ʱ"+date.getMinutes()+"��"+date.getSeconds()+"��");


            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());
            Log.e("timemmmmmm","="+format);
            if(format.compareTo("2017-09-08 00:00:00")>0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
