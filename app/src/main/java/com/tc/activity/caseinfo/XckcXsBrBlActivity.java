package com.tc.activity.caseinfo;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.util.CaseUtil;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialogN;
import com.tc.view.LineEditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class XckcXsBrBlActivity extends Activity {
    private ImageView btn_brblReturn;
    DateWheelDialogN startDateChooseDialog,endDateChooseDialog;
    private LineEditText et_title;
    private LineEditText sTime,eTime;
    private LineEditText ct1, ct2,ct3,ct4,ct5,ct6,ct7,ct8,ct9;
    private LineEditText bot1,bot2,bot3,bot4,bot5;

    private String ajNum;
    private  String filePath;
    private String fileName;

    private void initWidgets(){
        btn_brblReturn=(ImageView)findViewById(R.id.btn_brblReturn);
        btn_brblReturn.setOnClickListener(new OnClick());


        et_title=(LineEditText)findViewById(R.id.et_title);

        sTime=(LineEditText)findViewById(R.id.et_time_s);
        sTime.setOnClickListener(new OnClick());
        eTime=(LineEditText)findViewById(R.id.et_time_e);
        eTime.setOnClickListener(new OnClick());
        startDateChooseDialog = new DateWheelDialogN(XckcXsBrBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                sTime.setText(time);

            }
        });
        startDateChooseDialog.setDateDialogTitle("开始时间");

        endDateChooseDialog = new DateWheelDialogN(XckcXsBrBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                eTime.setText(time);

            }
        });
        endDateChooseDialog.setDateDialogTitle("结束时间");


        ct1=(LineEditText)findViewById(R.id.et_ct1);
        ct2=(LineEditText)findViewById(R.id.et_ct2);
        ct3=(LineEditText)findViewById(R.id.et_ctdsr);
        ct4=(LineEditText)findViewById(R.id.et_ct4);
        ct5=(LineEditText)findViewById(R.id.et_ct5);
        ct6=(LineEditText)findViewById(R.id.et_ct6);
        ct7=(LineEditText)findViewById(R.id.et_ct7);
        ct8=(LineEditText)findViewById(R.id.et_ct8);
        ct9=(LineEditText)findViewById(R.id.et_ct9);

        bot1=(LineEditText)findViewById(R.id.et_bot1);
        bot2=(LineEditText)findViewById(R.id.et_bot2);
        bot3=(LineEditText)findViewById(R.id.et_bot3);
        bot4=(LineEditText)findViewById(R.id.et_bot4);
        bot5=(LineEditText)findViewById(R.id.et_bot5);

    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_brblReturn:
                    finish();
                    break;
                case R.id.et_time_s:
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_time_e:
                    endDateChooseDialog.showDateChooseDialog();
                    break;
            }
        }
    }

    private void getFileName()
    {
        try{
            File file = new File(Values.PATH_XCBL);
            if(!file.exists())
            {
                file.mkdirs();
            }
            fileName=UtilTc.getCurrentTime()+".doc";
            filePath=Values.PATH_XCBL+fileName;

        }catch (Exception e){
            Log.e("ddd","getFileName ",e);
        }
    }


    private void doScan(){

        File newFile = new File(filePath);
        Map<String,String> map = new HashMap<>();
        map.put("$TITLE$",""+et_title.getText().toString());

        map.put("$stime$",""+sTime.getText().toString());
        map.put("$ETIME",""+eTime.getText().toString());

        map.put("&ct1",""+ct1.getText().toString());
        map.put("&ct2",""+ct2.getText().toString());
        map.put("&ct3",""+ct3.getText().toString());
        map.put("&ct4",""+ct4.getText().toString());
        map.put("&ct5",""+ct5.getText().toString());
        map.put("&ct6",""+ct6.getText().toString());
        map.put("&ct7",""+ct7.getText().toString());
        map.put("&ct8",""+ct8.getText().toString());
        map.put("&ct9",""+ct9.getText().toString());


        map.put("&bot1",""+bot1.getText().toString());
        map.put("&bot2",""+bot2.getText().toString());
        map.put("&bot3",""+bot3.getText().toString());
        map.put("&bot4",""+bot4.getText().toString());
        map.put("&bot5",""+bot5.getText().toString());

        CaseUtil.writeDoc("xsaj-brbl.doc",newFile,map);
    }

    public void onPreview(View v)
    {
        getFileName();

        doScan();

        CaseUtil.doOpenWord(filePath,this);
    }
    public void onPrinter(View V)
    {
        getFileName();
        doScan();
        CaseUtil.doOpenWord(filePath,this);
    }
    public void onUpload(View v)
    {
        File newFile = new File(filePath);


        if(newFile.exists())
        {
            SendFile sf = new  SendFile();
            sf.start();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"未找到文档",Toast.LENGTH_SHORT).show();
        }


    }
    private FTPClient myFtp;
    public class SendFile extends Thread {
        // private String currentPath = "";

        @Override
        public void run()
        {
            try {
               MyFTPDataTransferListener listener = new MyFTPDataTransferListener("");
                  File newFile = new File(filePath);
                    myFtp = new FTPClient();
                    myFtp.connect("61.176.222.166", 21);
                    myFtp.login("admin", "1234");
                    myFtp.changeDirectory("../");
                    myFtp.changeDirectory("xcbl-xs-brbl");



                myFtp.upload(newFile, listener); // ???


            } catch (Exception e)
            {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }
    private CustomProgressDialog progressDialog = null;
    private final static int UPLOAD = 1;
    private String errorMessage = "";
    TcApp myapp;
    // ?????
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type)
            {
                case UPLOAD:
                    progressDialog.setMessage("开始上传");
                    break;
            }
        }
        progressDialog.show();
    }

    // ????????
    private void stopProgressDialog() {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what) {
                case Values.ONE_UPLOAD://
                    UtilTc.myToast(getApplicationContext(), "上传完成");
                    myapp.sendHandleMsg(101, SenceCheck.waitingHandler);
                    finish();
                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.showLog("error");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();

                    break;
                case Values.START_UPLOAD:
                    UtilTc.showLog("开始上传");
                    startProgressDialog(1);

                    break;
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("已上传一个文档");
                    stopProgressDialog();

                    break;

                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("上传失败");
                    stopProgressDialog();
                    break;
            }
        }
    };



    private class MyFTPDataTransferListener implements FTPDataTransferListener
    {
        String fileName = "";

        MyFTPDataTransferListener(String fileNameRet)
        {
            fileName = fileNameRet;
        }

        @Override
        public void aborted() {
            // TODO Auto-generated method stub
        }
        @Override
        public void started() {// ??????
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.START_UPLOAD;;
            mHandler.sendMessage(msg);
        }
        @Override
        public void transferred(int length)
        {
        }

        @Override
        public void completed()
        {// ??????
            // TODO Auto-generated method stub

            Message msg;
            msg = Message.obtain();
            msg.what = Values.SUCCESS_UPLOAD;
            mHandler.sendMessage(msg);

            new Thread(media).start();
        }

        @Override
        public void failed() {// ??????
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler.sendMessage(msg);
        }
    }

    Runnable media = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //??????????????????????
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", ajNum));
            params.add(new BasicNameValuePair("A_type", "文档"));
            params.add(new BasicNameValuePair("A_Format", "doc"));
            params.add(new BasicNameValuePair("A_MM", "/xcbl-xs-brbl"+"/"+fileName));

            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");

                httpRequest.setEntity(formEntity);
                // ???HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200)
                {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    // json ????
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0"))
                    {

                       mHandler.sendEmptyMessage(Values.ONE_UPLOAD);

                    }
                    else if (code.trim().equals("10003"))
                    {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                }
                else
                {
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xckcxsbrbl);
        super.onCreate(savedInstanceState);
        ajNum=getIntent().getStringExtra("name");
        myapp = (TcApp) TcApp.mContent;
        initWidgets();
    }


}
