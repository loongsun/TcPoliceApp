package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.util.CaseUtil;
import com.tc.view.DateWheelDialogN;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//辨认笔录
public class BrBlActivity extends Activity {

    private static final String TAG = BrBlActivity.class.getSimpleName();
    private ImageView mImgBack;
    private TextView mTitleTx;
    private EditText mEdtNumber;
    private EditText mEdtOfficeName;
    private EditText mEditStartTime;
    private EditText mEdtEndTime;
    private EditText mCheckPlace;
    private EditText mCheckerName;
    private EditText mCheckerOffice;
    private EditText mCheckerDuty;
    private EditText mProcess;
    private EditText mWorkerOne;
    private EditText mWorkerTwo;
    private EditText mEdtBr;
    private Button mBtnPreview;
    private Button mBtnUpload;
    private Button mBtnPrint;
    private EditText mBrName;
    private EditText mBrOffice;
    private EditText mBrDuty;
    private String mName;
    public static final String BR_NAME = "BRBL";//辨认笔录
    private String mNewPath;
    private EditText mEdtObj;
    private EditText mEdtPlane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_br_bl);
        initView();
        initData();
    }

    private void initView() {
        mImgBack = (ImageView)findViewById(R.id.img_back);
        mTitleTx = (TextView)findViewById(R.id.tx_head_title);
        mTitleTx.setText("辨认笔录");
        mImgBack.setOnClickListener(mOnClicKListener);

        mEdtNumber = (EditText)findViewById(R.id.edt_number);
        mEdtOfficeName = (EditText)findViewById(R.id.edt_office_name);
        mEditStartTime = (EditText)findViewById(R.id.edt_start_time);
        mEdtEndTime = (EditText)findViewById(R.id.edt_end_time);
        mEdtEndTime.setOnClickListener(mOnClicKListener);
        mEditStartTime.setOnClickListener(mOnClicKListener);
        mCheckPlace = (EditText)findViewById(R.id.edt_place);

        mCheckerName = (EditText)findViewById(R.id.edt_checker_name);
        mCheckerOffice = (EditText)findViewById(R.id.edt_checker_office);
        mCheckerDuty = (EditText)findViewById(R.id.edt_checker_duty);

        mBrName = (EditText)findViewById(R.id.edt_br_name);
        mBrOffice = (EditText)findViewById(R.id.edt_br_office);
        mBrDuty = (EditText)findViewById(R.id.edt_br_duty);

        mEdtObj = (EditText)findViewById(R.id.edt_obj);
        mEdtPlane = (EditText)findViewById(R.id.edt_plan);
        mProcess= (EditText)findViewById(R.id.edt_process);

        mWorkerOne = (EditText)findViewById(R.id.edt_kanyan1);
        mWorkerTwo = (EditText)findViewById(R.id.edt_kan2);
        mEdtBr = (EditText)findViewById(R.id.edt_witness);

        mBtnPreview = (Button)findViewById(R.id.btn_preview);
        mBtnUpload = (Button)findViewById(R.id.btn_upload);
        mBtnPrint = (Button)findViewById(R.id.btn_print);
        mBtnPreview.setOnClickListener(mOnClicKListener);
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        mName=extras.getString("name");
        Log.i(TAG,"name = "+mName);
        mEdtNumber.setText(mName);
    }

    private View.OnClickListener mOnClicKListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.edt_start_time:
                    DateWheelDialogN chooseDialog = new DateWheelDialogN(BrBlActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mEditStartTime.setText(time);
                        }
                    });
                    chooseDialog.setDateDialogTitle("开始时间");
                    chooseDialog.showDateChooseDialog();
                    break;
                case R.id.edt_end_time:
                    DateWheelDialogN endDialog = new DateWheelDialogN(BrBlActivity.this, new DateWheelDialogN
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

    private void getFileName() {
        try{
            File file = new File( Values.PATH_BOOKMARK + BR_NAME);
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = Values.PATH_BOOKMARK+BR_NAME+"/"+mName+"_"+ UtilTc.getCurrentTime()+".doc";
            mNewPath = fileName;
        }catch (Exception e){
            Log.e(TAG,"getFileName ",e);
        }
    }

    private void doScan(){
        File newFile = new File(mNewPath);
        Map<String,String> map = new HashMap<>();
        map.put("$GAJ$",mEdtOfficeName.getText().toString());
        map.put("$TIMESTART$",mEditStartTime.getText().toString());
        map.put("$TIMEEND$ ",mEdtEndTime.getText().toString());
        map.put("$BRPLACE$",mCheckPlace.getText().toString());
        map.put("$BARNAME$",mCheckerName.getText().toString());
        map.put("$BAROFFICE$",mCheckerOffice.getText().toString());
        map.put("$BARDUTY$",mCheckerDuty.getText().toString());
        map.put("$BRRNAME$",mBrName.getText().toString());

        map.put("$BRROFFICE$",mBrOffice.getText().toString());
        map.put("$BRRDUTY$",mBrDuty.getText().toString());
        map.put("$BROBJ$",mEdtObj.getText().toString());
        map.put("$BRPLANE$",mEdtPlane.getText().toString());

        map.put("$PROCESS$",mProcess.getText().toString());
        map.put("$WORKER1$",mWorkerOne.getText().toString());
        map.put("$WORKER2$",mWorkerTwo.getText().toString());
        map.put("$BRNAME$", mEdtBr.getText().toString());
        CaseUtil.writeDoc("brbl.doc",newFile,map);
    }

    private void uploadDoc() {

    }

    private void printDoc() {
        previewDoc();
    }

    private void previewDoc() {
        getFileName();
        doScan();
        CaseUtil.doOpenWord(mNewPath,this);
    }
}
