package com.tc.activity.caseinfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.tc.activity.SenceExcute;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class JcBlActivity extends Activity {
    //�ɼ���
    private EditText et_gajname,et_kssj,et_jssj
            ,et_jcdx,et_gzzjhm,et_jcryxm,et_gzdw
            ,et_zw,et_gcjg,et_jcr1,et_jcr2,
            et_jlr
            ,et_jzr,et_kyAjbh
            ;

    private ImageView btn_kcblReturn;
    private String newPath = "";
    private String name="";
    private final static int UPLOAD=1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;
    private List<String> allList = new ArrayList<String>();
    private CommonAdapter2 mCommonAdapter2 = new CommonAdapter2(this);
    private ListView docList;
    TcApp ia;
    String EVIDENCE_NAME = "JCBL";
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
        et_kssj = (EditText) findViewById(R.id.et_kssj);
        et_jssj = (EditText) findViewById(R.id.et_jssj);
        et_jssj.setOnClickListener(new Onclick());
        et_jcdx = (EditText) findViewById(R.id.et_jcdx);
        et_gzzjhm = (EditText) findViewById(R.id.et_gzzjhm);
        et_jcryxm = (EditText) findViewById(R.id.et_jcryxm);
        et_gzdw = (EditText) findViewById(R.id.et_gzdw);

        et_zw = (EditText) findViewById(R.id.et_zw);
        et_gcjg = (EditText) findViewById(R.id.et_gcjg);
        et_jcr1 = (EditText) findViewById(R.id.et_jcr1);
        et_jcr2 = (EditText) findViewById(R.id.et_jcr2);
        et_jlr = (EditText) findViewById(R.id.et_jlr);

        et_jzr = (EditText) findViewById(R.id.et_jzr);
        et_kyAjbh = (EditText) findViewById(R.id.et_kyAjbh);

        et_kssj.setOnClickListener(new Onclick());

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
                case R.id.et_kssj:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(JcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kssj.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("��ʼʱ��");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_jssj:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN endDateChooseDialog = new DateWheelDialogN(JcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jssj.setText(time);

                        }
                    });
                    endDateChooseDialog.setDateDialogTitle("����ʱ��");
                    endDateChooseDialog.showDateChooseDialog();
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
        setContentView(R.layout.activity_case_jcbl);
        ia = (TcApp)TcApp.mContent;
        initWidgets();
    }
    //�ϴ���ť
    public void BtnUploadBL(View view) {
        String startTime=et_kssj.getText().toString();
        String endTime=et_jssj.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"����ʱ��Ҫ���ڿ�ʼʱ��",Toast.LENGTH_SHORT).show();
            return;
        }
//        File fileStart = new File(Values.ALLFILES+"wtxt/JCBL/");
//        boolean flag = getFileName2(fileStart.listFiles(), name);
//
//        if(flag){
//            //���ڱ����ļ�
//        }else{
//            try {
//                String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//                File file = new File(sdcardPath  + "/TC/wtxt/JCBL/");
//                if (!file.exists()){
//                    file.mkdir();
//                }
//
//                String fileName = Values.PATH_BOOKMARK+"JCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
//                newPath = fileName;
//                InputStream inputStream = getAssets().open("jcbl.doc");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            doScan();
//        }
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/JCBL/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "JCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("jcbl.doc");
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
    private PoliceStateListBean plb;
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

                if(Values.dbjqList.size()>0)
                    plb= Values.dbjqList.get(0);

                File fileStart = new File(Values.ALLFILES+"wtxt/JCBL/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                //	Log.e("path", "path"+path);

                for(int i=0;i<bltxt.size();i++){
                    //�ж��ϴ����ĸ��ļ���
                    if(bltxt.get(i).endsWith(".doc")){
                        myFtp.changeDirectory("../");
                        myFtp.changeDirectory("xcbl-jcbl");
                        currentPath=Values.PATH_jcbl;
                        currentFilePaht="/xcbl-jcbl";
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
            new Thread(media).start();

            File file = new File(Values.PATH_jcbl+fileName);
            if(file.exists()) {
//                boolean isDel = file.delete();
            }

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

            File file = new File(sdcardPath  + "/TC/wtxt/JCBL/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "JCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("jcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        //�鿴
        doOpenWord(true);
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
    //��ӡ��¼
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/JCBL/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                String fileName = Values.PATH_BOOKMARK + "JCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("jcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
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
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), ""+errorMessage);
                    ia.sendHandleMsg(101, SenceCheck2.waitingHandler);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.myToast(getApplicationContext(), "�������쳣");
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
            String url_passenger ="http://61.176.222.166:8765/interface/xz/ADD_ZF_XZ_JCBL.asp";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID",name));

            params.add(new BasicNameValuePair("KSSJ",et_kssj.getText().toString()));
            params.add(new BasicNameValuePair("JSSJ",et_jssj.getText().toString()));
            params.add(new BasicNameValuePair("JCDX",et_jcdx.getText().toString()));
            params.add(new BasicNameValuePair("JCZHM",et_gzzjhm.getText().toString()));
            params.add(new BasicNameValuePair("JCRYXM",et_jcryxm.getText().toString()));

            params.add(new BasicNameValuePair("JCRYZW",et_zw.getText().toString()));
            params.add(new BasicNameValuePair("JCRYGZDW",et_gzdw.getText().toString()));
            params.add(new BasicNameValuePair("GCJJG",et_gcjg.getText().toString()));
            params.add(new BasicNameValuePair("JCR",et_jcr1.getText().toString()+","+et_jcr2.getText().toString()));
            params.add(new BasicNameValuePair("JLR",et_jlr.getText().toString()));


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
                        JSONObject jsResult=person.getJSONObject("data");
                        errorMessage = jsResult.getString("message");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }else if(code.trim().equals("10001")){
                        JSONObject jsResult=person.getJSONObject("data");
                        errorMessage = jsResult.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }
                }else{
                    //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


    private void  doScan(){
        //��ȡģ���ļ�
        String startTime=et_kssj.getText().toString();
        String endTime=et_jssj.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"����ʱ��Ҫ���ڿ�ʼʱ��",Toast.LENGTH_SHORT).show();
            return;
        }

//        File demoFile=new File(demoPath);
        //�������ɵ��ļ�
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$GAJ$", et_gajname.getText().toString());
//        map.put("$TIME1$",
//                et_kssj.getText().toString().substring(0,4)+"��"
//                        +et_kssj.getText().toString().substring(5,7)+"��"
//                        +et_kssj.getText().toString().substring(8,10)+"��"
//                        +et_kssj.getText().toString().substring(11,13)+"ʱ"
//                        +et_kssj.getText().toString().substring(14,16)+"��"
//                );
//        map.put("$TIME2$",
//                et_jssj.getText().toString().substring(0,4)+"��"
//                        +et_jssj.getText().toString().substring(5,7)+"��"
//                        +et_jssj.getText().toString().substring(8,10)+"��"
//                        +et_jssj.getText().toString().substring(11,13)+"ʱ"
//                        +et_jssj.getText().toString().substring(14,16)+"��"
//        );

        map.put("$NIAN1$", et_kssj.getText().toString().substring(0,4));
        map.put("$YUE1$", et_kssj.getText().toString().substring(5,7));
        map.put("$RI1$", et_kssj.getText().toString().substring(8,10));
        map.put("$SHI1$", et_kssj.getText().toString().substring(11,13));
        map.put("$FEN1$", et_kssj.getText().toString().substring(14,16));


        map.put("$NIAN2$", et_jssj.getText().toString().substring(0,4));
        map.put("$YUE2$", et_jssj.getText().toString().substring(5,7));
        map.put("$RI2$", et_jssj.getText().toString().substring(8,10));
        map.put("$SHI2$", et_jssj.getText().toString().substring(11,13));
        map.put("$FEN2$", et_jssj.getText().toString().substring(14,16));


        map.put("$JCDX$", et_jcdx.getText().toString());

        map.put("$JCZHM$", et_gzzjhm.getText().toString());

        map.put("$JCRXM$", et_jcryxm.getText().toString());
        map.put("$JCRGZDW$", et_gzdw.getText().toString());
        map.put("$JCRZW$", et_zw.getText().toString());

        map.put("$GCJG$", et_gcjg.getText().toString());

        map.put("$JCR1$", et_jcr1.getText().toString());
        map.put("$JCR2$", et_jcr2.getText().toString());

        map.put("$JLR$", et_jlr.getText().toString());
        map.put("$JZR$", et_jzr.getText().toString());

        writeDoc(newFile,map);

    }
    /**
     * �����ֻ��а�װ�Ŀɴ�word�����
     */
    private void doOpenWord(boolean ylFlag){
        String startTime=et_kssj.getText().toString();
        String endTime=et_jssj.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"����ʱ��Ҫ���ڿ�ʼʱ��",Toast.LENGTH_SHORT).show();
            return;
        }
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
            InputStream in = getAssets().open("jcbl.doc");
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

//            FileOutputStream out1 = null;
//            CustomXWPFDocument doc = new CustomXWPFDocument();
//            try {
//                URL url =  new URL("http://f.hiphotos.baidu.com/image/h%3D200/sign=333f3ac494510fb367197097e932c893/a8014c086e061d95df89434571f40ad163d9ca84.jpg");
//                BufferedInputStream fis = new BufferedInputStream(url.openStream());
//                String picId = doc.addPictureData(fis, XWPFDocument.PICTURE_TYPE_JPEG);
//                doc.createPicture(picId, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_JPEG), 200, 200);
//
//                out = new FileOutputStream("simple.docx");
//                doc.write(out1);
//                out1.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

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

    private String 						mediaType="";
    //�ϴ�ý����Ϣ
    Runnable media=new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //��ѯ������ʱ��͵����ֳ�ʱ��
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
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
}
