package com.tc.activity.caseinfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck2;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.client.StringUtils;
import com.tc.util.CaseUtil;
import com.tc.util.CaseUtil2;
import com.tc.util.ConfirmDialog;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialogN;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class XcBlActivity extends Activity {
    //�ɼ���
    private EditText et_gajname,et_gaj,et_pcs
            ,et_fasj,et_fadd,et_dsrxm1,et_dsrxb1
    ,et_dsrage1,et_dsrzz,et_dsrtz1,et_dsrxm2,
            et_dsrxb2
            ,et_dsrage2,et_dsrzz2,et_dsrtz2,et_dsrxm3,
            et_dsrxb3
            ,et_dsrage3,et_dsrzz3,et_dsrtz3,et_dsrxm4,
            et_dsrxb4
            ,et_dsrage4,et_dsrzz4,et_dsrtz4,
            et_zrxm1,et_zrxb1,et_zrage1,et_zrzz1,et_zrbh1,
    et_zrxm2,et_zrxb2,et_zrage2,et_zrzz2,et_zrbh2,
            et_wfxwsygj,et_cwshqk,et_xcphqk,et_qt,et_bz
            ,et_cjry1,et_cjry2,et_jzr,et_kyAjbh
            ;

    String EVIDENCE_NAME = "XCBL";
    private ImageView btn_kcblReturn;
    private String newPath = "";
    private String name="";
    private final static int UPLOAD=1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;
    TcApp ia;

    private List<String> allList = new ArrayList<String>();
    private CommonAdapter2 mCommonAdapter2 = new CommonAdapter2(this);
    private ListView docList;
    // ���ȿ�
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case UPLOAD:
                    progressDialog.setMessage("�����ϴ���Ϣ,���Ժ�");
                    break;
            }
        }
        progressDialog.show();
    }
    // ȡ�����ȿ�
    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initWidgets() {
        //�ɼ����ֲ���

        btn_kcblReturn = (ImageView)findViewById(R.id.btn_kcblReturn);
        btn_kcblReturn.setOnClickListener(new Onclick());
        et_gajname = (EditText) findViewById(R.id.et_gajname);
        et_gaj = (EditText) findViewById(R.id.et_gaj);
        et_pcs = (EditText) findViewById(R.id.et_pcs);
        et_fasj = (EditText) findViewById(R.id.et_fasj);
        et_fadd = (EditText) findViewById(R.id.et_fadd);
        et_dsrxm1 = (EditText) findViewById(R.id.et_dsrxm1);
        et_dsrxb1 = (EditText) findViewById(R.id.et_dsrxb1);





        et_dsrage1 = (EditText) findViewById(R.id.et_dsrage1);
        et_dsrzz = (EditText) findViewById(R.id.et_dsrzz);
        et_dsrtz1 = (EditText) findViewById(R.id.et_dsrtz1);
        et_dsrxm2 = (EditText) findViewById(R.id.et_dsrxm2);
        et_dsrxb2 = (EditText) findViewById(R.id.et_dsrxb2);

        et_dsrage2 = (EditText) findViewById(R.id.et_dsrage2);
        et_dsrzz2 = (EditText) findViewById(R.id.et_dsrzz2);
        et_dsrtz2 = (EditText) findViewById(R.id.et_dsrtz2);
        et_dsrxm3 = (EditText) findViewById(R.id.et_dsrxm3);
        et_dsrxb3 = (EditText) findViewById(R.id.et_dsrxb3);

        et_dsrage3 = (EditText) findViewById(R.id.et_dsrage3);
        et_dsrzz3 = (EditText) findViewById(R.id.et_dsrzz3);
        et_dsrtz3 = (EditText) findViewById(R.id.et_dsrtz3);
        et_dsrxm4 = (EditText) findViewById(R.id.et_dsrxm4);
        et_dsrxb4 = (EditText) findViewById(R.id.et_dsrxb4);

        et_dsrage4 = (EditText) findViewById(R.id.et_dsrage4);
        et_dsrzz4 = (EditText) findViewById(R.id.et_dsrzz4);
        et_dsrtz4 = (EditText) findViewById(R.id.et_dsrtz4);

        et_zrxm1 = (EditText) findViewById(R.id.et_zrxm1);
        et_zrxb1 = (EditText) findViewById(R.id.et_zrxb1);
        et_zrage1 = (EditText) findViewById(R.id.et_zrage1);

        et_zrzz1 = (EditText) findViewById(R.id.et_zrzz1);
        et_zrbh1 = (EditText) findViewById(R.id.et_zrbh1);



        et_zrxm2 = (EditText) findViewById(R.id.et_zrxm2);

        et_zrxb2 = (EditText) findViewById(R.id.et_zrxb2);
        et_zrage2 = (EditText) findViewById(R.id.et_zrage2);
        et_zrzz2 = (EditText) findViewById(R.id.et_zrzz2);

        et_zrbh2 = (EditText) findViewById(R.id.et_zrbh2);
        et_wfxwsygj = (EditText) findViewById(R.id.et_wfxwsygj);
        et_cwshqk = (EditText) findViewById(R.id.et_cwshqk);

        et_xcphqk = (EditText) findViewById(R.id.et_xcphqk);
        et_qt = (EditText) findViewById(R.id.et_qt);
        et_bz = (EditText) findViewById(R.id.et_bz);

        et_cjry1 = (EditText) findViewById(R.id.et_cjry1);
        et_cjry2 = (EditText) findViewById(R.id.et_cjry2);
        et_jzr = (EditText) findViewById(R.id.et_jzr);
        et_kyAjbh = (EditText) findViewById(R.id.et_kyAjbh);

        et_fasj.setOnClickListener(new Onclick());

        name=getIntent().getStringExtra("name");
        et_kyAjbh.setText(name);

        checkDoc();
        docList =(ListView)findViewById(R.id.xsaj_brbl_doc_list);
        docList.setAdapter(mCommonAdapter2);

    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.et_fasj:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_fasj.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("��ʼʱ��");
                    startDateChooseDialog.setTimePickerGone(true);
                    startDateChooseDialog.showDateChooseDialog();
                    break;

                case R.id.btn_kcblReturn:
                    finish();
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_xcbl);
        ia = (TcApp)TcApp.mContent;
        initWidgets();
    }
    //�ϴ���ť
    public void BtnUploadBL(View view) {
//        File fileStart = new File(Values.ALLFILES+"wtxt/XCBL/");
//        boolean flag = getFileName2(fileStart.listFiles(), name);
//
//        if(flag){
//            //���ڱ����ļ�
//            Log.e("e","���ش���");
//        }else{
//            Log.e("e","���ز�����");
//            try {
//                String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//                File file = new File(sdcardPath  + "/TC/wtxt/XCBL/");
//                if (!file.exists()){
//                    file.mkdir();
//                }
//
//                String fileName = Values.PATH_BOOKMARK+"XCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
//                newPath = fileName;
//                InputStream inputStream = getAssets().open("xcbl.doc");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            doScan();
//        }
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCBL/");
            if (!file.exists()){
//                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "XCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();

        startProgressDialog(UPLOAD);
        new Thread(uploadRun).start();
        SendFile sf = new SendFile();
        sf.start();
    }
    private FTPClient myFtp;
    String currentFilePaht = "";
    private String currentFile="";
    private int fileCount = 0;
    private int mTotalSize = 0;
    public class SendFile extends Thread {
        private String currentPath="";
        @Override
        public void run() {
            try {
                myFtp = new FTPClient();
                myFtp.connect("61.176.222.166", 21); // ����
                myFtp.login("admin", "1234"); // ��¼

                Log.e("e","FTp�ϴ�");
//                if(Values.dbjqList.size()>0)
//                    plb= Values.dbjqList.get(0);

                File fileStart = new File(Values.ALLFILES+"wtxt/XCBL/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                	Log.e("path", "bltxt size "+bltxt.size());

                for(int i=0;i<bltxt.size();i++){
                    //�ж��ϴ����ĸ��ļ���

                    Log.e("path", "bltxt name "+bltxt.get(i));
                    if(bltxt.get(i).endsWith(".doc")){
                        myFtp.changeDirectory("../");
                        myFtp.changeDirectory("xcbl-xcbl");
                        currentPath=Values.PATH_xcbl;
                        currentFilePaht="/xcbl-xcbl";
                    }

                    File file = new File(currentPath+bltxt.get(i));
                    fileCount = (int) file.length();

                    mTotalSize = fileCount;
                    currentFile=currentFilePaht+"/"+bltxt.get(i);
                    MyFTPDataTransferListener listener = new MyFTPDataTransferListener(bltxt.get(i));
                    myFtp.upload(file, listener); // �ϴ�
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }
    private String 						mediaType="";
    //�ϴ�ý����Ϣ
    Runnable media=new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //��ѯ������ʱ��͵����ֳ�ʱ��
            params.add(new BasicNameValuePair("A_ID", name));
            params.add(new BasicNameValuePair("A_type", mediaType));
            params.add(new BasicNameValuePair("A_Format",mediaFormat));
            params.add(new BasicNameValuePair("A_MM",
                    currentFile));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient()
                        .execute(httpRequest);
                Log.e("code", "code"
                        + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse
                            .getEntity());
                    Log.e("e", "�ϴ�ý���ֵ�ǣ�" + strResult);
                    // json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //�ϴ��ɹ�
                        //	mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };

    private List<String> bltxt = new ArrayList<String>();
    private void getFileName(File[] files, String jqNum) {
        bltxt.clear();
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName"+fileName);
                        bltxt.add(fileName);
                    }
                }
            }
        }
    }
    private String mediaFormat="";
    private class MyFTPDataTransferListener implements FTPDataTransferListener {

        String fileName = "";
        MyFTPDataTransferListener(String fileNameRet){
            fileName = fileNameRet;
        }
        @Override
        public void aborted() {
            // TODO Auto-generated method stub
        }
        @Override
        public void completed() {// �ϴ��ɹ�
            // TODO Auto-generated method stub
            UtilTc.showLog("currentFile:"+currentFile);
            UtilTc.showLog("currentFile ��3λ"+currentFile.substring(currentFile.length()-3,currentFile.length()));
            mediaFormat=currentFile.substring(currentFile.length()-3,currentFile.length());
            if(mediaFormat.equals("doc")){
                mediaType="�ĵ�";
            }

            File file = new File(Values.PATH_xcbl+fileName);
            if(file.exists()) {
                Log.e("e","����");
//                boolean isDel = file.delete();
            }else{
                Log.e("e","������");
            }
            new Thread(media).start();

            Message msg;
            msg = Message.obtain();
            msg.what = Values.SUCCESS_UPLOAD;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void failed() {// �ϴ�ʧ��
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void started() {// �ϴ���ʼ
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = 2;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void transferred(int length) {// �ϴ����̼���
            int progress = length;
            Message msg;
            msg = Message.obtain();
            msg.what = 1;
            msg.obj = progress;
            mHandler1.sendMessage(msg);
        }
    }

    Handler mHandler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ��ɹ�");

                    stopProgressDialog();
                    //�ı侯��״̬
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ�ʧ��");
                    stopProgressDialog();
                    break;
            }
        };
    };
    //Ԥ���༭
    public void BtneditBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCBL/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "XCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        //�鿴
        doOpenWord(true);
    }

    //��ӡ��¼
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCBL/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "XCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        //�鿴
        doOpenWord(false);
    }


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_RECORDUPLOAD://

                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.showLog("�������쳣");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), "�����ɹ�");
                    ia.sendHandleMsg(100, SenceCheck2.waitingHandler);
                    stopProgressDialog();
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.myToast(getApplicationContext(), ""+errorMessage);
                    stopProgressDialog();
                    break;
            }
        };
    };

    Runnable uploadRun=new Runnable(){
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/xz/ADD_ZF_XZ_XCBL.asp";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID",name));
            params.add(new BasicNameValuePair("CJDW",et_gaj.getText().toString()+"������"+et_pcs.getText().toString()+"�ɳ���"));
            params.add(new BasicNameValuePair("FASJ",et_fasj.getText().toString()));
            params.add(new BasicNameValuePair("FADD",et_fadd.getText().toString()));
            params.add(new BasicNameValuePair("XCQK_WFXWSYGJ",et_wfxwsygj.getText().toString()));
            params.add(new BasicNameValuePair("XCQK_CWSHQK",et_cwshqk.getText().toString()));
            params.add(new BasicNameValuePair("XCQK_XCPHQK",et_xcphqk.getText().toString()));

            params.add(new BasicNameValuePair("XCQK_QT",et_qt.getText().toString()));
            params.add(new BasicNameValuePair("BZ",et_bz.getText().toString()));
            params.add(new BasicNameValuePair("CZRY",et_cjry1.getText().toString()+","+et_cjry2.getText().toString()));
            params.add(new BasicNameValuePair("JZR",et_jzr.getText().toString()));

            Log.e("e","params ��"+params);
            try{
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code"+httpResponse.getStatusLine().getStatusCode());
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String strResult= EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�"+strResult);
                    if(strResult==null||strResult.equals("")){
                              mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if(code.trim().equals("0")){
                        JSONObject jb = person.getJSONObject("data");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                           JSONObject jb = person.getJSONObject("data");
                           errorMessage = jb.getString("message");
                             mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }else  if(code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }
                }else{
                       mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


    private void doScan(){
//        String startTime=et_kssj.getText().toString();
//        String endTime=et_jssj.getText().toString();
//        if(endTime.compareTo(startTime)<=0)
//        {
//            Toast.makeText(getApplicationContext(),"����ʱ��Ҫ���ڿ�ʼʱ��",Toast.LENGTH_SHORT).show();
//            return;
//        }

        //��ȡģ���ļ�
//        File demoFile=new File(demoPath);
        //�������ɵ��ļ�
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$GAJ$", et_gaj.getText().toString());
        map.put("$PCS$", et_pcs.getText().toString());

        try{
            map.put("$FASJ$",
                    et_fasj.getText().toString().substring(0,4)+"��"+
                            et_fasj.getText().toString().substring(5,7)+"��"+
                            et_fasj.getText().toString().substring(8,10)+"��");
        }catch(Exception e){

        }

        map.put("$FADD$", et_fadd.getText().toString());

        map.put("$DSRNAME1$", et_dsrxm1.getText().toString());
        map.put("$SEX1$", et_dsrxb1.getText().toString());
        map.put("$AGE1$", et_dsrage1.getText().toString());
        map.put("$ADRESS1$", et_dsrzz.getText().toString());
        map.put("$TZ1$", et_dsrtz1.getText().toString());

        map.put("$DSRNAME2$", et_dsrxm2.getText().toString());
        map.put("$SEX2$", et_dsrxb2.getText().toString());
        map.put("$AGE2$", et_dsrage2.getText().toString());
        map.put("$ADRESS2$", et_dsrzz2.getText().toString());
        map.put("$TZ2$", et_dsrtz2.getText().toString());

        map.put("$DSRNAME3$", et_dsrxm3.getText().toString());
        map.put("$SEX3$", et_dsrxb3.getText().toString());
        map.put("$AGE3$", et_dsrage3.getText().toString());
        map.put("$ADRESS3$", et_dsrzz3.getText().toString());
        map.put("$TZ3$", et_dsrtz3.getText().toString());

        map.put("$DSRNAME4$", et_dsrxm4.getText().toString());
        map.put("$SEX4$", et_dsrxb4.getText().toString());
        map.put("$AGE4$", et_dsrage4.getText().toString());
        map.put("$ADRESS4$", et_dsrzz4.getText().toString());
        map.put("$TZ4$", et_dsrtz4.getText().toString());

        map.put("$ZRNAME1$", et_zrxm1.getText().toString());
        map.put("$ZRSEX1$", et_zrxb1.getText().toString());
        map.put("$ZRAGE1$", et_zrage1.getText().toString());
        map.put("$ZRADRESS1$", et_zrzz1.getText().toString());
        map.put("$ZRNUM1$", et_zrbh1.getText().toString());

        map.put("$ZRNAME2$", et_zrxm2.getText().toString());
        map.put("$ZRSEX2$", et_zrxb2.getText().toString());
        map.put("$ZRAGE2$", et_zrage2.getText().toString());
        map.put("$ZRADRESS2$", et_zrzz2.getText().toString());
        map.put("$ZRNUM2$", et_zrbh2.getText().toString());

        map.put("$SYGJ$", et_wfxwsygj.getText().toString());
        map.put("$XHQK$", et_cwshqk.getText().toString());
        map.put("$PHQK$", et_xcphqk.getText().toString());
        map.put("$QT$", et_qt.getText().toString());


        map.put("$BZ$", et_bz.getText().toString());
        map.put("$CJRY1$", et_cjry1.getText().toString());
        map.put("$CJRY2$", et_cjry2.getText().toString());
        map.put("$JZR$", et_jzr.getText().toString());

        writeDoc(newFile,map);
    }
    /**
     * �����ֻ��а�װ�Ŀɴ�word�����
     */
    private void doOpenWord(boolean ylFlag){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            startActivity(intent);
            if(ylFlag){
                startActivityForResult(intent,10);
            }
        } catch(ActivityNotFoundException e) {
            //��⵽ϵͳ��δ��װOliveOffice��apk����
            Toast.makeText(this, "δ�ҵ����", Toast.LENGTH_LONG).show();
            //���ȵ�www.olivephone.com/e.apk���ز���װ
        }
    }
    /**
     * demoFile ģ���ļ�
     * newFile �����ļ�
     * map Ҫ��������
     * */
    public void writeDoc( File newFile ,Map<String, String> map)
    {
        try
        {
            InputStream in = getAssets().open("xcbl.doc");
//            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // ��ȡword�ı�����
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // �滻�ı�����
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // ����ֽ���
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean getFileName2(File[] files, String jqNum) {
        boolean isFlag = false;
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName2(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName"+fileName);
                        isFlag =  true;

                    }
                }
            }
        }
        return isFlag;
    }

    private class CommonAdapter2 extends BaseAdapter {

        Activity mContent ;
        public CommonAdapter2(Activity mCtx){
            mContent =mCtx;
        }
        @Override
        public int getCount() {

            if (allList!=null)
            {
                UtilTc.showLog(" bltxt.size()"+ allList.size());
                return allList.size();
            }
            UtilTc.showLog("����0��");
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (allList != null) {
                return allList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            Log.e("e", "getView");
            CommonAdapter2.ViewHolder holder = null;
            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.item_bltxt, null);
                holder = new  CommonAdapter2.ViewHolder();


                holder.tv_blTitle = (TextView) mView.findViewById(R.id.tv_blTitle);
                holder.iv_delete = (ImageView) mView.findViewById(R.id.iv_delete);
                holder.iv_edit = (ImageView) mView.findViewById(R.id.iv_edit);

                holder.parentLayout = (LinearLayout) mView.findViewById(R.id.lin_bl);

                mView.setTag(holder);
            } else {
                holder = (CommonAdapter2.ViewHolder) mView.getTag();
            }

            //word�ļ�ɾ��
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    final ConfirmDialog confirmDialog = new ConfirmDialog(mContent, "ȷ��Ҫɾ����?", "ɾ��", "ȡ��");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();

                            File file=null;
                            String filename = allList.get(position);
                            file = new File(Values.PATH_BOOKMARK + EVIDENCE_NAME+"/"+filename);

                            if(file.exists())
                            {
                                boolean isDel = file.delete();
                                if(isDel)
                                {
                                    allList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void doCancel() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();
                        }
                    });


                }
            });

            //word�ļ��༭
            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {


                    String filename = allList.get(position);
                    String filepath =  Values.PATH_BOOKMARK + EVIDENCE_NAME+"/"+filename;

                    CaseUtil2.doOpenWord(filepath,mContent,false);


                }
            });


            String ret = allList.get(position);
            UtilTc.showLog("ret       :"+ret);
            holder.tv_blTitle.setText(ret);
            return mView;
        }
        private class ViewHolder {
            TextView tv_blTitle;
            LinearLayout parentLayout;
            ImageView iv_delete,iv_edit;
        }
    }
    // -------------------------�����ļ�
    private void checkFileName(File[] files, String jqNum)
    {

        if (files != null)// nullPointer
        {
            for (File file : files)
            {
                if (file.isDirectory()) {
                    checkFileName(file.listFiles(), jqNum);
                }
                else
                {
                    String fileName = file.getName();

                    if (fileName.startsWith(jqNum) && fileName.endsWith(".doc"))
                    {
                        allList.add(fileName);
                    }
                }
            }
        }
    }
    private void checkDoc()
    {
        File file = new File(Values.PATH_BOOKMARK + EVIDENCE_NAME+"/");
        checkFileName(file.listFiles(),name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            File file=new File(newPath);
            if(file.exists()) {
                file.delete();
                Log.e("result","deleted");
            }
        }
    }
}
