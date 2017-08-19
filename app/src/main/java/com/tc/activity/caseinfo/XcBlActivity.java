package com.tc.activity.caseinfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
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

    private ImageView btn_kcblReturn;
    private String newPath = "";
    private String name="";

    private void initWidgets() {
        //�ɼ����ֲ���


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
        initWidgets();
    }
    //�ϴ���ť
    public void BtnUploadBL(View view) {
        new Thread(uploadRun).start();
    }

    //��ӡ��¼
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCBL/");
            if (!file.exists()){
                file.mkdir();
            }

            String fileName = Values.PATH_BOOKMARK+"XCBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
            newPath = fileName;
            InputStream inputStream = getAssets().open("xcbl.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
    }
    Runnable uploadRun=new Runnable(){
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/xz/ADD_ZF_XZ_JCBL.asp";
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
                        //      mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if(code.trim().equals("0")){
                        //    jsResult=person.getJSONObject("data");
                        //    mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        //   JSONObject jb = person.getJSONObject("data");
                        //   errorMessage = jb.getString("message");
                        //     mHandler.sendEmptyMessage(Values.ERROR_OTHER);
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
        map.put("$GAJ$", et_gaj.getText().toString());
        map.put("$PCS$", et_pcs.getText().toString());
        map.put("$FASJ$", et_fasj.getText().toString());
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
}
