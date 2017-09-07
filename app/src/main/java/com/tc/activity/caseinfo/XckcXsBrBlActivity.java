package com.tc.activity.caseinfo;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private  LinearLayout docLinearLayout;
    private TextView docName;
    private ImageView docEdit,docDelete;
    DateWheelDialogN startDateChooseDialog,endDateChooseDialog;
    private LineEditText et_title;
    private LineEditText sTime,eTime;
    private LineEditText ct1, ct2,ct3,ct4,ct5,ct6,ct7,ct8,ct9;
    private LineEditText bot1,bot2,bot3,bot4,bot5;

    private String ajNum;
    private  String filePath;
    private String fileName;

    private  String filePath1;
    private String fileName1;

    private void initWidgets(){
        btn_brblReturn=(ImageView)findViewById(R.id.btn_brblReturn);
        btn_brblReturn.setOnClickListener(new OnClick());


        docLinearLayout=(LinearLayout)findViewById(R.id.xsaj_brbl_doc_item);
        docLinearLayout.setVisibility(View.GONE);
        docName=(TextView)findViewById(R.id.doc_name);
        docEdit=(ImageView)findViewById(R.id.doc_edit);
        docDelete=(ImageView)findViewById(R.id.doc_delete);



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
            File file = new File(Values.PATH_XCBL_XSAJ_BRBL);
            if(!file.exists())
            {
                file.mkdirs();
            }

            if(TextUtils.isEmpty(fileName))
            {
                fileName=ajNum+"-"+UtilTc.getCurrentTime()+".doc";
                filePath=Values.PATH_XCBL_XSAJ_BRBL+fileName;
            }
            else
            {
                File newFile = new File(filePath);
                if(newFile.exists())
                    newFile.delete();
            }

        }catch (Exception e){
            Log.e("ddd","getFileName ",e);
        }
    }


    private void doScan(){

        File newFile = new File(filePath);
        Map<String,String> map = new HashMap<>();
        map.put("$TITLE$",""+et_title.getText().toString());

        String startTime=sTime.getText().toString();
        String endTime=eTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
        String y1=startTime.substring(0,4);
        String M1 =startTime.substring(5,7);
        String d1 =startTime.substring(8,10);
        String H1 = startTime.substring(11,13);
        String m1 = startTime.substring(14,16);

        String y2=endTime.substring(0,4);
        String M2 =endTime.substring(5,7);
        String d2 =endTime.substring(8,10);
        String H2 = endTime.substring(11,13);
        String m2 = endTime.substring(14,16);

        map.put("&yy1",""+y1);
        map.put("&M1",""+M1);
        map.put("&d1",""+d1);
        map.put("&H1",""+H1);
        map.put("&f1",""+m1);

        map.put("&yy2",""+y2);
        map.put("&M2",""+M2);
        map.put("&d2",""+d2);
        map.put("&H2",""+H2);
        map.put("&f2",""+m2);



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
        String startTime=sTime.getText().toString();
        String endTime=eTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }

        getFileName();

        doScan();

        CaseUtil.doOpenWord(filePath,this);
    }
    public void onPrinter(View V)
    {
        String startTime=sTime.getText().toString();
        String endTime=eTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
        getFileName();
        doScan();
        CaseUtil.doOpenWord(filePath,this);
    }
    public void onUpload(View v)
    {
        String startTime=sTime.getText().toString();
        String endTime=eTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }

        getFileName();
        doScan();

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
    //WPS查看
    public void onDocEdit(View v)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(this.filePath)), fileMimeType);
        try{
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            //检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
            //请先到www.olivephone.com/e.apk下载并安装
        }


    }
    public void onDocDelete(View v)
    {
        if(this.filePath!=null)
        {
            File file =new File(filePath);
            if(file.exists())
            file.delete();
            Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
            docLinearLayout.setVisibility(View.GONE);
        }
    }
    // -------------------------遍历文件
    private void checkFileName(File[] files, String jqNum) {

        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    checkFileName(file.listFiles(), jqNum);
                }
                else
                {
                    String fileName = file.getName();

                    if (fileName.startsWith(jqNum) && fileName.endsWith(".doc"))
                    {
                        Log.e("e", "fileName="+fileName);
                         this.fileName=fileName;
                         this.filePath=Values.PATH_XCBL_XSAJ_BRBL+this.fileName;
                         docName.setText(this.fileName);
                         docLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
private void checkDoc()
{
    File file = new File(Values.PATH_XCBL_XSAJ_BRBL);
    checkFileName(file.listFiles(),ajNum);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xckcxsbrbl);
        super.onCreate(savedInstanceState);
        ajNum=getIntent().getStringExtra("name");
        myapp = (TcApp) TcApp.mContent;
        initWidgets();
        checkDoc();
    }


}
