package com.tc.util;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.tc.bean.EventInfo;
import com.tc.bean.LabelInfo;
import com.tc.bean.PowerBean;
import com.tc.bean.PowerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by zhao on 17-8-19.
 */

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getSimpleName();
    private static final int DEFAULT_HTTP_READ_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_HTTP_OPEN_TIMEOUT = 10 * 1000;


    public static PowerBean parseData(String result){
        if(TextUtils.isEmpty(result)){
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("data")){
                PowerBean powerBean= new PowerBean();
                JSONObject data = jsonObject.getJSONObject("data");
                if(data.has("winfo")){
                    JSONArray winfoArray = data.getJSONArray("winfo");
                    ArrayList<EventInfo> eventInfos = new ArrayList<>();
                    for(int i=0;i<winfoArray.length();i++){
                        JSONObject wInfo = winfoArray.getJSONObject(i);
                        EventInfo eventInfo = new EventInfo();
                        eventInfo.wName = wInfo.optString("wname");
                        eventInfo.wNum = wInfo.optString("wnum");
                        eventInfo.wType= wInfo.optString("wtype");
                        eventInfo.wTime = wInfo.optString("wtime");
                        eventInfo.wX = wInfo.optString("wx");
                        eventInfo.wY = wInfo.optString("wy");
                        eventInfo.wAddress = wInfo.optString("wadress");
                        eventInfo.wTel = wInfo.optString("wtel");
                        eventInfo.wPerson = wInfo.optString("wperson");
                        eventInfos.add(eventInfo);
                    }
                    powerBean.eventInfoList = eventInfos;
                }

                if(data.has("wpower")){
                    JSONArray wpowerArray = data.getJSONArray("wpower");
                    ArrayList<PowerInfo> powerInfos = new ArrayList<>();
                    for(int i=0;i<wpowerArray.length();i++){
                        PowerInfo powerInfo = new PowerInfo();
                        JSONObject wpower = wpowerArray.getJSONObject(i);
                        powerInfo.powerType = wpower.optString("powertype");
                        powerInfo.powerX = wpower.optString("powerx");
                        powerInfo.powerY= wpower.optString("powery");
                        powerInfos.add(powerInfo);
                    }
                    powerBean.powerInfoList = powerInfos;
                }
                if(data.has("lables")){
                    JSONArray labelArray = data.getJSONArray("lables");
                    ArrayList<LabelInfo> labelInfos = new ArrayList<>();
                    for(int i=0;i<labelArray.length();i++){
                        JSONObject labelObj = labelArray.getJSONObject(i);
                        LabelInfo labelInfo = new LabelInfo();
                        labelInfo.name=labelObj.optString("name");
                        labelInfo.note=labelObj.optString("note");
                        labelInfo.labelX = labelObj.optString("lablex");
                        labelInfo.labelY= labelObj.optString("labley");
                    }
                    powerBean.labelInfoList = labelInfos;
                }
                return powerBean;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDataFromURL(String url) {
        try {
            URL lastUrl = new URL(url);
            Log.i(TAG, "get url=" + url);
            HttpURLConnection connection = (HttpURLConnection) lastUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
            connection.setConnectTimeout(DEFAULT_HTTP_OPEN_TIMEOUT);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept-Encoding", "gzip");
            int response = connection.getResponseCode();
            if (response == 200) {
                Log.i(TAG, "Encoding=" + connection.getContentEncoding());
                InputStream is = null;
                if (("gzip").equals(connection.getContentEncoding())) {
                    is = new GZIPInputStream(connection.getInputStream());
                } else {
                    is = connection.getInputStream();
                }

                String temp = getStringFromInputSream(is);
                return temp;
            }
        } catch (Exception e) {
            Log.e(TAG, "get data from url exception" + ",url=" + url, e);
        }
        return null;
    }

    private static String getStringFromInputSream(InputStream is) {
        String result = "";
        try {
            if (is == null) {
                Log.e(TAG, "Empty input stream,return null.");
                return null;
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int count = -1;
            while ((count = is.read(data, 0, 4096)) != -1) {
                outStream.write(data, 0, count);
            }
            data = null;
            try {
                result = new String(outStream.toByteArray(), "UTF-8"/* "ISO-8859-1" */);
            } catch (UnsupportedEncodingException e1) {
                Log.e(TAG, "UnsupportedEncodingException UTF-8 ");
            }

            try {
                if (is != null) {
                    is.close();
                    is = null;
                }

                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException while close operation.");
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException while close operation1.");
        }
        return result;
    }
}
