package com.tc.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sdses.tool.Values;
import com.tc.app.TcApp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


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
            //??I???????
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
        // ???ListView?????Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()??????????????
            View listItem = listAdapter.getView(i, null, listView);
            // ????????Vie????w
            listItem.measure(0, 0);
            // ????????????????
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()??????????????????
        // params.height?????????ListView??????????????
        listView.setLayoutParams(params);
    }

    public static void openWordFile(File file,Context context){
        if(context ==null || file == null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(file),fileMimeType);
        try{
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "未安装打开doc的apk", Toast.LENGTH_LONG).show();

        }
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
            Toast.makeText(context, "未安装打开doc的apk", Toast.LENGTH_LONG).show();

        }
    }

    public static void doOpenPhoto(String newPath,Context context){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "image/*";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            context.startActivity(intent);
        } catch(ActivityNotFoundException e) {
            //???????δ???OliveOffice??apk????
            Toast.makeText(context, "δ??????", Toast.LENGTH_LONG).show();

        }
    }


    public static boolean isNetConnent(Context context){
        if(context ==null){
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null && activeNetworkInfo.isAvailable()){
            return true;
        }
        return false;
    }

    public static void startUploadFile(String fileName,String ftpPath,String id,Handler handler){
//        boolean netConnent = isNetConnent(TcApp.mContent);
//        if(!netConnent){
//            Toast.makeText(TcApp.mContent,"网络不通",Toast.LENGTH_SHORT).show();
//            return;
//        }
        new SendFileTask(fileName,ftpPath,id,handler).start();
    }


    public static class SendFileTask extends  Thread{

        private final String mLocalFilePath;
        private final String mFtpPath;
        private final Handler mHandler;
        private String mFileName;
        private String mId;

        public SendFileTask(String fileName,String ftpPath,String id,Handler handler){
            this.mLocalFilePath = fileName;
            this.mFtpPath = ftpPath;
            this.mId = id;
            this.mHandler = handler;
        }


        @Override
        public void run() {
            super.run();
            try{
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect("61.176.222.166", 21);
                ftpClient.login("admin","1234");
                ftpClient.changeDirectory("../");
                ftpClient.changeDirectory(mFtpPath);
                File file = new File(mLocalFilePath);
                if(file!=null){
                    mFileName = file.getName();
                }

                ftpClient.upload(file,new FTPDataTransferListener(){

                    @Override
                    public void started() {
                        Log.i(TAG,"started ");
                    }

                    @Override
                    public void transferred(int i) {
                        Log.i(TAG,"transferred "+i);
                    }

                    @Override
                    public void completed() {
                        Log.i(TAG," completed");
                        String mediaType = "???";
                        String formate = "doc";
                        String ftpFilePath = "/"+mFtpPath+"/"+ mFileName;
                        String result = uploadPostInfo(mId,mediaType,formate,ftpFilePath);
                        if(!TextUtils.isEmpty(result)){
                            JSONTokener jsonTokener = new JSONTokener(result);
                            try {
                                JSONObject jsonObject = (JSONObject)jsonTokener.nextValue();
                                if(jsonObject.has("error code")){
                                    String code = jsonObject.getString("error code");
                                    if("0".equals(code)){
                                        mHandler.sendEmptyMessage(Values.SUCCESS_UPLOAD);
                                    }else{
                                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                        mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                    }

                    @Override
                    public void aborted() {
                        Log.i(TAG,"aborted ");
                    }

                    @Override
                    public void failed() {
                        Log.i(TAG," fail ");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }
                });



            }catch (Exception e){
                Log.e(TAG,"SendFileTask ",e);
            }
        }
    }

    public static String uploadPostInfo(String id,String mediaType,String mediaFormat,String ftpFilePath){
        String url = "http://61.176.222.166:8765/interface/addmeiti/";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("A_ID",id));
        params.add(new BasicNameValuePair("A_type",mediaType));
        params.add(new BasicNameValuePair("A_Format",mediaFormat));
        params.add(new BasicNameValuePair("A_MM",ftpFilePath));

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
            httpPost.setEntity(formEntity);
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Log.i(TAG,"statusCode = "+statusCode);
            if(statusCode == 200){
                String result = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG,"result = "+result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
