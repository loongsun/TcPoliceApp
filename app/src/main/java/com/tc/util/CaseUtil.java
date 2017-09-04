package com.tc.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tc.app.TcApp;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;


public class CaseUtil {

    private static final String TAG = CaseUtil.class.getSimpleName();

    public static void writeDoc(String fileName, File newFile, Map<String, String> map) {
        if (newFile == null || map == null) {
            return;
        }
        try {
            InputStream inputStream = TcApp.mContent.getAssets().open(fileName);
            HWPFDocument hdt = new HWPFDocument(inputStream);
            Range range = hdt.getRange();
            //替换文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile, true);

            hdt.write(byteOutputStream);
            fileOutputStream.write(byteOutputStream.toByteArray());
            byteOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG, " write doc", e);
        } catch (Exception e) {
            Log.e(TAG, " writeDoc", e);
        }
    }

    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项Vie的宽高w
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    public static void doOpenWord(String path,Context context) {
        if(context ==null || TextUtils.isEmpty(path)){
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(path)),fileMimeType);
        try{
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "未找到doc软件,请安装查看", Toast.LENGTH_LONG).show();

        }

    }

    public class SendFileTask extends  Thread{

        @Override
        public void run() {
            super.run();
            try{
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect("61.176.222.166", 21);
                ftpClient.login("admin","1234");

            }catch (Exception e){
                Log.e(TAG,"SendFileTask ",e);
            }
        }
    }


}
