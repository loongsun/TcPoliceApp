package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.app.TcApp;
import com.tc.application.R;
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

public class XckcBlActivity extends Activity {
    //�ɼ���
    private EditText et_kyAjbh, et_kyKydw, et_kyZpbgdw, et_kyZpsj, et_kySy, et_kyBeginTime, et_kyEndTime, et_kyXcdd,
            et_kyXcbhqk, et_kyWd, et_kySd, et_kyFx, et_kyZhr, et_kyZhrDw, et_kyZhrZw, et_kyKyqk, et_kyHzt, et_kyZx,
            et_kyLx, et_kyLy, et_kyBlr, et_kyZtr, et_kyZxr, et_kyLxr, et_kyLyr,et_kyTime;

    EditText et_brqm1,et_brdw1,et_brzw1;
    EditText et_brqm2,et_brdw2,et_brzw2;
    EditText et_brqm3,et_brdw3,et_brzw3;
    EditText et_brqm4,et_brdw4,et_brzw4;
    EditText et_brqm5,et_brdw5,et_brzw5;
    EditText et_brqm6,et_brdw6,et_brzw6;

    EditText et_jzrqm1,et_jzrxb1,et_jzrsr1,et_jzrzz1;
    EditText et_jzrqm2,et_jzrxb2,et_jzrsr2,et_jzrzz2;
    TcApp ia;

    private RadioGroup rg_kyTq, rg_kyXctj, rg_kyGx;
    private ImageView btn_kcblReturn;
    private String newPath = "";
    private String name="";
    private String kyTq="";
    private String kyXctj="";
    private String kyGx="";
    private RadioButton rb_qing,rb_yin,rb_yu,rb_wu,rb_xue,rb_bdxc,rb_ysxc,rb_zrg,rb_dg;

    private final static int UPLOAD=1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;
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
        et_kyAjbh = (EditText) findViewById(R.id.et_kyAjbh);
        et_kyKydw = (EditText) findViewById(R.id.et_kyKydw);
        et_kyZpbgdw = (EditText) findViewById(R.id.et_kyZpbgdw);
        et_kyZpsj = (EditText) findViewById(R.id.et_kyZpsj);
        et_kySy = (EditText) findViewById(R.id.et_kySy);
        et_kyBeginTime = (EditText) findViewById(R.id.et_kyBeginTime);
        et_kyEndTime = (EditText) findViewById(R.id.et_kyEndTime);
        et_kyXcdd = (EditText) findViewById(R.id.et_kyXcdd);
        et_kyXcbhqk = (EditText) findViewById(R.id.et_kyXcbhqk);
        et_kyWd = (EditText) findViewById(R.id.et_kyWd);
        et_kySd = (EditText) findViewById(R.id.et_kySd);
        et_kyFx = (EditText) findViewById(R.id.et_kyFx);
        et_kyZhr = (EditText) findViewById(R.id.et_kyZhr);
        et_kyZhrDw = (EditText) findViewById(R.id.et_kyZhrDw);
        et_kyZhrZw = (EditText) findViewById(R.id.et_kyZhrZw);
        et_kyZx = (EditText) findViewById(R.id.et_kyZx);
        et_kyKyqk = (EditText) findViewById(R.id.et_kyKyqk);
        et_kyHzt = (EditText) findViewById(R.id.et_kyHzt);
        et_kyLx = (EditText) findViewById(R.id.et_kyLx);
        et_kyLy = (EditText) findViewById(R.id.et_kyLy);
        et_kyBlr = (EditText) findViewById(R.id.et_kyBlr);
        et_kyZtr = (EditText) findViewById(R.id.et_kyZtr);
        et_kyZxr = (EditText) findViewById(R.id.et_kyZxr);
        et_kyLxr = (EditText) findViewById(R.id.et_kyLxr);
        et_kyLyr = (EditText) findViewById(R.id.et_kyLyr);

        et_brqm1 = (EditText) findViewById(R.id.et_brqm1);
        et_brdw1 = (EditText) findViewById(R.id.et_brdw1);
        et_brzw1 = (EditText) findViewById(R.id.et_brzw1);

        et_brqm2 = (EditText) findViewById(R.id.et_brqm2);
        et_brdw2 = (EditText) findViewById(R.id.et_brdw2);
        et_brzw2 = (EditText) findViewById(R.id.et_brzw2);

        et_brqm3 = (EditText) findViewById(R.id.et_brqm3);
        et_brdw3 = (EditText) findViewById(R.id.et_brdw3);
        et_brzw3 = (EditText) findViewById(R.id.et_brzw3);

        et_brqm4 = (EditText) findViewById(R.id.et_brqm4);
        et_brdw4 = (EditText) findViewById(R.id.et_brdw4);
        et_brzw4 = (EditText) findViewById(R.id.et_brzw4);

        et_brqm5 = (EditText) findViewById(R.id.et_brqm5);
        et_brdw5 = (EditText) findViewById(R.id.et_brdw5);
        et_brzw5 = (EditText) findViewById(R.id.et_brzw5);

        et_brqm6 = (EditText) findViewById(R.id.et_brqm6);
        et_brdw6 = (EditText) findViewById(R.id.et_brdw6);
        et_brzw6 = (EditText) findViewById(R.id.et_brzw6);

        et_jzrqm1 = (EditText) findViewById(R.id.et_jzrqm1);
        et_jzrxb1 = (EditText) findViewById(R.id.et_jzrxb1);
        et_jzrsr1 = (EditText) findViewById(R.id.et_jzrsr1);
        et_jzrzz1 = (EditText) findViewById(R.id.et_jzrzz1);

        et_jzrqm2 = (EditText) findViewById(R.id.et_jzrqm2);
        et_jzrxb2 = (EditText) findViewById(R.id.et_jzrxb2);
        et_jzrsr2 = (EditText) findViewById(R.id.et_jzrsr2);
        et_jzrzz2 = (EditText) findViewById(R.id.et_jzrzz2);


        et_kyTime = (EditText) findViewById(R.id.et_kyTime);
        et_kyTime.setOnClickListener(new Onclick());

        et_jzrsr1.setOnClickListener(new Onclick());
        et_jzrsr2.setOnClickListener(new Onclick());

        //ѡ�񲿷�
        rg_kyTq = (RadioGroup) findViewById(R.id.rg_kyTq);
        rg_kyXctj = (RadioGroup) findViewById(R.id.rg_kyXctj);
        rg_kyGx = (RadioGroup) findViewById(R.id.rg_kyGx);
        et_kyBeginTime.setOnClickListener(new Onclick());
        et_kyEndTime.setOnClickListener(new Onclick());

        btn_kcblReturn=(ImageView)findViewById(R.id.btn_kcblReturn);
        btn_kcblReturn.setOnClickListener(new Onclick());
        name=getIntent().getStringExtra("name");
        et_kyAjbh.setText(name);
        et_kyZpsj.setOnClickListener(new Onclick());


        rb_qing = (RadioButton) findViewById(R.id.rb_qing);
        rb_yin = (RadioButton) findViewById(R.id.rb_yin);
        rb_yu = (RadioButton) findViewById(R.id.rb_yu);
        rb_wu = (RadioButton) findViewById(R.id.rb_wu);
        rb_xue = (RadioButton) findViewById(R.id.rb_xue);
        rb_bdxc = (RadioButton) findViewById(R.id.rb_bdxc);
        rb_ysxc = (RadioButton) findViewById(R.id.rb_ysxc);
        rb_zrg = (RadioButton) findViewById(R.id.rb_zrg);
        rb_dg = (RadioButton) findViewById(R.id.rb_dg);

        rg_kyTq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_yin.getId()){
                    kyTq = "��";
                }else if(checkedId == rb_qing.getId()){
                    kyTq = "��";
                }else if(checkedId == rb_yu.getId()){
                    kyTq = "��";
                }else if(checkedId == rb_wu.getId()){
                    kyTq = "��";
                }else if(checkedId == rb_xue.getId()){
                    kyTq = "ѩ";
                }
            }
        });

        rg_kyXctj.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == rb_bdxc.getId()){
                    kyXctj = "�䶯�ֳ�";
                }else if(checkedId == rb_ysxc.getId()){
                    kyXctj = "ԭʼ�ֳ�";
                }
            }
        });

        rg_kyGx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == rb_zrg.getId()){
                    kyGx = "��Ȼ��";
                }else if(checkedId == rb_dg.getId()){
                    kyGx = "�ƹ�";
                }
            }
        });


    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.et_kyBeginTime:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyBeginTime.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("��ʼʱ��");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyTime:
                    UtilTc.showLog("kyTime");
                    DateWheelDialogN kyTime = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyTime.setText(time);

                        }
                    });
                    kyTime.setDateDialogTitle("��ʼʱ��");
                    kyTime.showDateChooseDialog();
                    break;
                case R.id.et_jzrsr1:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN jzrsr1 = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jzrsr1.setText(time);

                        }
                    });
                    jzrsr1.setDateDialogTitle("��ʼʱ��");
                    jzrsr1.showDateChooseDialog();
                    break;
                case R.id.et_jzrsr2:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN jzrsr2 = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jzrsr2.setText(time);

                        }
                    });
                    jzrsr2.setDateDialogTitle("��ʼʱ��");
                    jzrsr2.showDateChooseDialog();
                    break;
                case R.id.et_kyEndTime:
                    DateWheelDialogN endDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyEndTime.setText(time);

                        }
                    });
                    endDateChooseDialog.setDateDialogTitle("����ʱ��");
                    endDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyZpsj:
                    DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyZpsj.setText(time);

                        }
                    });
                    kyDateChooseDialog3.setDateDialogTitle("ָ��ʱ��");
                    kyDateChooseDialog3.showDateChooseDialog();
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
        setContentView(R.layout.activity_case_xckcbl);
        ia = (TcApp)TcApp.mContent;
        initWidgets();
    }
    //�ϴ���ť
    public void BtnUploadBL(View view) {
        startProgressDialog(UPLOAD);
        new Thread(uploadRun).start();
    }

    //��ӡ��¼
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCKYBL/");
            if (!file.exists()){
                file.mkdir();
            }

            String fileName = Values.PATH_BOOKMARK+"XCKYBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
            newPath = fileName;
            InputStream inputStream = getAssets().open("xckybl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
    }


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_RECORDUPLOAD://

                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "��������:"
                            + errorMessage);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.showLog("�������쳣");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.showLog("�ϴ��ɹ�");
                    stopProgressDialog();
                    ia.sendHandleMsg(100, SenceCheck.waitingHandler);
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("�ϴ�ʧ��");
                    stopProgressDialog();
                    break;
            }
        };
    };

    Runnable uploadRun=new Runnable(){
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/xs/ADD_ZF_XS_XCKYBL.asp";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID",name));
            params.add(new BasicNameValuePair("XCKYDW",et_kyKydw.getText().toString()));
            params.add(new BasicNameValuePair("ZPBGDW",et_kyZpbgdw.getText().toString()));
            params.add(new BasicNameValuePair("ZPSJ",et_kyZpsj.getText().toString()));
            params.add(new BasicNameValuePair("KYSY",et_kySy.getText().toString()));
            params.add(new BasicNameValuePair("XCKYKSSJ",et_kyBeginTime.getText().toString()));
            params.add(new BasicNameValuePair("XCKYJSSJ",et_kyEndTime.getText().toString()));


            params.add(new BasicNameValuePair("XCDD",et_kyXcdd.getText().toString()));
            params.add(new BasicNameValuePair("XCBHQK",et_kyXcbhqk.getText().toString()));
            params.add(new BasicNameValuePair("TQ",kyTq));
            params.add(new BasicNameValuePair("WD",et_kyWd.getText().toString()));
            params.add(new BasicNameValuePair("SD",et_kySd.getText().toString()));
            params.add(new BasicNameValuePair("FX",et_kyFx.getText().toString()));
            params.add(new BasicNameValuePair("KGQXCTJ",kyXctj));
            params.add(new BasicNameValuePair("XCKYGX",kyGx));
            params.add(new BasicNameValuePair("XCKYZHR",et_kyZhr.getText().toString()));
            params.add(new BasicNameValuePair("XCKYZHRDW",et_kyZhrDw.getText().toString()));
            params.add(new BasicNameValuePair("XCKYZHRZW",et_kyZhrZw.getText().toString()));
            params.add(new BasicNameValuePair("XCKYQK",et_kyKyqk.getText().toString()));
            params.add(new BasicNameValuePair("XCKYHZT",et_kyHzt.getText().toString()));
            params.add(new BasicNameValuePair("XCKYZX",et_kyZx.getText().toString()));

            params.add(new BasicNameValuePair("XCKYLX",et_kyLx.getText().toString()));
            params.add(new BasicNameValuePair("XCKYLY",et_kyLy.getText().toString()));

            params.add(new BasicNameValuePair("BLR",et_kyBlr.getText().toString()));
            params.add(new BasicNameValuePair("ZTR",et_kyZtr.getText().toString()));
            params.add(new BasicNameValuePair("ZXR",et_kyZxr.getText().toString()));
            params.add(new BasicNameValuePair("LXR",et_kyLxr.getText().toString()));
            params.add(new BasicNameValuePair("LYR",et_kyLyr.getText().toString()));
            params.add(new BasicNameValuePair("KYSJ",et_kyTime.getText().toString()));
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
                    //    jsResult=person.getJSONObject("data");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }else{
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
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


    private void doScan(){
        //��ȡģ���ļ�
//        File demoFile=new File(demoPath);
        //�������ɵ��ļ�
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$XCKYDW$", et_kyKydw.getText().toString());
        map.put("$BGDW$", et_kyZpbgdw.getText().toString());
        map.put("$TIME$", et_kyZpsj.getText().toString());
        map.put("$KYSY$", et_kySy.getText().toString());

        map.put("$XCKYKSSJ$", et_kyBeginTime.getText().toString());
        map.put("$XCKYJSSJ$", et_kyEndTime.getText().toString());
        map.put("$XCDD$", et_kyXcdd.getText().toString());
        map.put("$SCBHQK$", et_kyXcbhqk.getText().toString());
        map.put("$TQ$", kyTq);
        map.put("$WD$", et_kyWd.getText().toString());
        map.put("$SD$", et_kySd.getText().toString());
        map.put("$FX$", et_kyFx.getText().toString());
        map.put("$XCTJ$", kyXctj);
        map.put("$GX$", kyGx);
        map.put("$ZHR$", et_kyZhr.getText().toString());

        map.put("$DW$", et_kyZhrDw.getText().toString());
        map.put("$ZW$", et_kyZhrZw.getText().toString());
        map.put("$XCKYQK$", et_kyKyqk.getText().toString());
        map.put("$ZTNUM$", et_kyHzt.getText().toString());

        map.put("$ZXNUM$", et_kyZx.getText().toString());
        map.put("$LX$", et_kyLx.getText().toString());
        map.put("$LYTIME$", et_kyLy.getText().toString());

        map.put("$BLR$", et_kyBlr.getText().toString());
        map.put("$ZTR$", et_kyZtr.getText().toString());
        map.put("$ZXR$", et_kyZxr.getText().toString());
        map.put("$LXR$", et_kyLxr.getText().toString());
        map.put("$LYR$", et_kyLyr.getText().toString());

        map.put("$BRQM1$", et_brqm1.getText().toString());
        map.put("$BRDW1$", et_brdw1.getText().toString());
        map.put("$BRZW1$", et_brzw1.getText().toString());

        map.put("$BRQM2$", et_brqm2.getText().toString());
        map.put("$BRDW2$", et_brdw2.getText().toString());
        map.put("$BRZW2$", et_brzw2.getText().toString());

        map.put("$BRQM3$", et_brqm3.getText().toString());
        map.put("$BRDW3$", et_brdw3.getText().toString());
        map.put("$BRZW3$", et_brzw3.getText().toString());

        map.put("$BRQM4$", et_brqm4.getText().toString());
        map.put("$BRDW4$", et_brdw4.getText().toString());
        map.put("$BRZW4$", et_brzw4.getText().toString());

        map.put("$BRQM5$", et_brqm5.getText().toString());
        map.put("$BRDW5$", et_brdw5.getText().toString());
        map.put("$BRZW5$", et_brzw5.getText().toString());

        map.put("$BRQM6$", et_brqm6.getText().toString());
        map.put("$BRDW6$", et_brdw6.getText().toString());
        map.put("$BRZW6$", et_brzw6.getText().toString());



        map.put("$JZRQM1$", et_jzrqm1.getText().toString());
        map.put("$JZRSEX1$", et_jzrxb1.getText().toString());
        map.put("$JZRSR1$", et_jzrsr1.getText().toString());
        map.put("$JZRZZ1$", et_jzrzz1.getText().toString());

        map.put("$JZRQM2$", et_jzrqm2.getText().toString());
        map.put("$JZRSEX2$", et_jzrxb2.getText().toString());
        map.put("$JZRSR2$", et_jzrsr2.getText().toString());
        map.put("$JZRZZ2$", et_jzrzz2.getText().toString());





        writeDoc(newFile,map);
        //�鿴
        doOpenWord();
    }
    /**
     * �����ֻ��а�װ�Ŀɴ�word�����
     */
    private void doOpenWord(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            startActivity(intent);
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
            InputStream in = getAssets().open("xckybl.doc");
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
}
