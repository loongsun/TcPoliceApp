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
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.util.CaseUtil;
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
public class KyBlActivity extends CaseBaseActivity {

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
    private CustomProgressDialog mProcessDialog;


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
        mImgBack.setOnClickListener(mOnClicKListener);

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
        mBtnUpload.setOnClickListener(mOnClicKListener);
    }

    private View.OnClickListener mOnClicKListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;
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

    protected void uploadDoc(){
        super.uploadDoc();
//        boolean netConnent = CaseUtil.isNetConnent(TcApp.mContent);
//        if(!netConnent){
//            Toast.makeText(TcApp.mContent,"当前网络未连接",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        startProcessDialog();
//        if(TextUtils.isEmpty(mNewPath)){
//            getFileName();
//            doScan();
//        }
//        String ftpPath = "xcbl-xz-kybl";
//        CaseUtil.startUploadFile(mNewPath,ftpPath,mName,mHandler);
    }

    @Override
    protected String geFtpPth() {
        return "xcbl-xz-kybl";
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

    @Override
    protected void getFileName() {
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
        String startTime=mEditStartTime.getText().toString();
        String endTime=mEdtEndTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
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
    @Override
    protected void doScan() {
        String startTime=mEditStartTime.getText().toString();
        String endTime=mEdtEndTime.getText().toString();
        if(endTime.compareTo(startTime)<=0)
        {
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
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
