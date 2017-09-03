package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//勘验笔录
public class KyBlActivity extends Activity {

    private static final String TAG = KyBlActivity.class.getSimpleName();
    private ImageView mImgBack;
    private TextView mTitleTx;
    private EditText mEdtNumber;
    private EditText mEdtOfficeName;
    private EditText mEditStartTime;
    private EditText mEdtEndTime;
    private EditText mCheckPlace;
    private EditText mCardNumber;
    private EditText mCheckerName;
    private EditText mCheckerOffice;
    private EditText mCheckerDuty;
    private EditText mProcess;
    private EditText mKanyanOne;
    private EditText mKanyanTwo;
    private EditText mRecorder;
    private EditText mWitness;
    private Button mBtnPreview;
    private Button mBtnUpload;
    private Button mBtnPrint;
    private String mName;
    private String mNewPath;
    private CustomProgressDialog mProcessDialog;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ky_bl);
        initView();
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        mName=extras.getString("name");
        Log.i(TAG,"name = "+mName);
        mEdtNumber.setText(mName);
    }

    private void initView() {
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mTitleTx = (TextView)findViewById(R.id.tx_head_title);
        mTitleTx.setText("勘验笔录");

        mEdtNumber = (EditText)findViewById(R.id.edt_number);
        mEdtOfficeName = (EditText)findViewById(R.id.edt_office_name);
        mEditStartTime = (EditText)findViewById(R.id.edt_start_time);
        mEdtEndTime = (EditText)findViewById(R.id.edt_end_time);
        mEdtEndTime.setOnClickListener(mOnClicKListener);
        mEditStartTime.setOnClickListener(mOnClicKListener);
        mCheckPlace = (EditText)findViewById(R.id.edt_place);
        mCardNumber = (EditText)findViewById(R.id.edt_card_number);

        mCheckerName = (EditText)findViewById(R.id.edt_checker_name);
        mCheckerOffice = (EditText)findViewById(R.id.edt_checker_office);
        mCheckerDuty = (EditText)findViewById(R.id.edt_checker_duty);

        mProcess= (EditText)findViewById(R.id.edt_process);

        mKanyanOne = (EditText)findViewById(R.id.edt_kanyan1);
        mKanyanTwo = (EditText)findViewById(R.id.edt_kan2);
        mRecorder = (EditText)findViewById(R.id.edt_recorder);
        mWitness = (EditText)findViewById(R.id.edt_witness);

        mBtnPreview = (Button)findViewById(R.id.btn_preview);
        mBtnUpload = (Button)findViewById(R.id.btn_upload);
        mBtnPrint = (Button)findViewById(R.id.btn_print);
        mBtnPreview.setOnClickListener(mOnClicKListener);
    }

    private View.OnClickListener mOnClicKListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.edt_start_time:
                    DateWheelDialogN chooseDialog = new DateWheelDialogN(KyBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mEditStartTime.setText(time);
                        }
                    });
                    chooseDialog.setDateDialogTitle("开始时间");
                    chooseDialog.showDateChooseDialog();
                break;
                case R.id.edt_end_time:
                    DateWheelDialogN endDialog = new DateWheelDialogN(KyBlActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mEdtEndTime.setText(time);
                        }
                    });
                    endDialog.setDateDialogTitle("开始时间");
                    endDialog.showDateChooseDialog();
                    break;
                case R.id.btn_preview:
                    previewDoc();
                    break;
                case R.id.btn_print:
                    printDoc();
                    break;
                case R.id.btn_upload:
                    uploadDoc();
                    break;
            }
        }
    };

    private void uploadDoc(){
        File fileStart = new File(Values.ALLFILES+"wtxt/KYBL");
        boolean flag = getfileName2(fileStart.listFiles(),mName);
        if(flag){
            //存在本地
        }else{
            getFileName();
            doScan();
            startProcessDialog(1);
            new Thread(uploadTask).start();
        }
    }

    public class SendFile extends Thread{

    }

    private String errorMessage;
    Runnable uploadTask = new Runnable() {
        @Override
        public void run() {
            String url = "http://61.176.222.166:8765/interface/xz/ADD_ZF_XZ_JCBL.asp";
            HttpPost httpRequest = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("A_ID",mName));
            params.add(new BasicNameValuePair("KSSJ",mEditStartTime.getText().toString()));
            params.add(new BasicNameValuePair("JSSJ",mEdtEndTime.getText().toString()));
            params.add(new BasicNameValuePair("JCDX",mCheckPlace.getText().toString()));
            params.add(new BasicNameValuePair("JCZHM",mCardNumber.getText().toString()));
            params.add(new BasicNameValuePair("JCRYXM",mCheckerName.getText().toString()));

            params.add(new BasicNameValuePair("JCRYGZDW",mCheckerOffice.getText().toString()));
            params.add(new BasicNameValuePair("JCRYZW",mCheckerDuty.getText().toString()));
            params.add(new BasicNameValuePair("GCJJG",mProcess.getText().toString()));
            params.add(new BasicNameValuePair("JCR",mKanyanOne.getText().toString()+","+mKanyanTwo.getText().toString()));
            params.add(new BasicNameValuePair("JLR",mRecorder.getText().toString()));

            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
                httpRequest.setEntity(formEntity);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if(statusCode == 200){
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    if(TextUtils.isEmpty(strResult)){
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void startProcessDialog(int type) {
        if(mProcessDialog == null){
            mProcessDialog = CustomProgressDialog.createDialog(this);
            mProcessDialog.setMessage("正在上传信息，请稍后");
        }
        mProcessDialog.show();
    }

    private boolean getfileName2(File[] files, String name) {
        boolean isFlag = false;
        if(files!=null){
            for(File file :files){
                if(file.isDirectory()){
                    getfileName2(file.listFiles(),name);
                }else{
                    String fileName = file.getName();
                    if(fileName.contains(name) && fileName.endsWith(".doc")){
                        Log.e(TAG," file name = "+fileName);
                        isFlag = true;
                    }
                }
            }
        }
        return isFlag;
    }

    private void printDoc(){
        getFileName();
        doScan();
        doOpenWord();
    }

    private void previewDoc() {
        getFileName();

        doScan();

        doOpenWord();

    }

    private void getFileName() {
        try{
            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(sdcardPath + "/TC/wtxt/KYBL");
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = Values.PATH_BOOKMARK+"KYBL/"+mName+"_"+ UtilTc.getCurrentTime()+".doc";
            mNewPath = fileName;
        }catch (Exception e){
            Log.e(TAG,"getFileName ",e);
        }

    }

    //用app查看doc文件
    private void doOpenWord() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(mNewPath)),fileMimeType);
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "未找到doc软件,请安装查看", Toast.LENGTH_LONG).show();

        }

    }

    //生成doc文件，并保存
    private void doScan() {
        File newFile = new File(mNewPath);
        Map<String,String> map = new HashMap<>();
        map.put("$GAJ$",mEdtOfficeName.getText().toString());
        map.put("$TIME1$", mEditStartTime.getText().toString());
        map.put("$TIME2$", mEdtEndTime.getText().toString());
        map.put("$JCDX$", mCheckPlace.getText().toString());

        map.put("$JCZHM$", mCardNumber.getText().toString());

        map.put("$JCRXM$", mCheckerName.getText().toString());
        map.put("$JCRGZDW$", mCheckerOffice.getText().toString());
        map.put("$JCRZW$", mCheckerDuty.getText().toString());

        map.put("$GCJG$", mProcess.getText().toString());

        map.put("$JCR1$", mKanyanOne.getText().toString());
        map.put("$JCR2$", mKanyanTwo.getText().toString());

        map.put("$JLR$", mRecorder.getText().toString());
        map.put("$JZR$", mWitness.getText().toString());

        writeDoc(newFile,map);
    }

    private void writeDoc(File newFile, Map<String, String> map) {
        try {
            InputStream inputStream = getAssets().open("kybl.doc");
            HWPFDocument hdt = new HWPFDocument(inputStream);
            Range range = hdt.getRange();
            //替换文本内容
            for(Map.Entry<String,String> entry:map.entrySet()){
                range.replaceText(entry.getKey(),entry.getValue());
            }
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile,true);

            hdt.write(byteOutputStream);
            fileOutputStream.write(byteOutputStream.toByteArray());
            byteOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG," write doc",e);
        }catch (Exception e){
            Log.e(TAG," writeDoc",e);
        }
    }
}
