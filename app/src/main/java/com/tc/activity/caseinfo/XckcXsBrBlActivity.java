package com.tc.activity.caseinfo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;


public class XckcXsBrBlActivity extends Activity {
    private ImageView btn_brblReturn;
    DateWheelDialogN startDateChooseDialog,endDateChooseDialog;
    private EditText et_title;
    private EditText sTime,eTime;
    private EditText ct1, ct2,ct3,ct4,ct5,ct6,ct7,ct8,ct9;
    private EditText bot1,bot2,bot3,bot4,bot5;

    private void initWidgets(){
        btn_brblReturn=(ImageView)findViewById(R.id.btn_brblReturn);
        btn_brblReturn.setOnClickListener(new OnClick());


        et_title=(EditText)findViewById(R.id.et_title);

        sTime=(EditText)findViewById(R.id.et_time_s);
        sTime.setOnClickListener(new OnClick());
        eTime=(EditText)findViewById(R.id.et_time_e);
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


        ct1=(EditText)findViewById(R.id.et_ct1);
        ct2=(EditText)findViewById(R.id.et_ct2);
        ct3=(EditText)findViewById(R.id.et_ct3);
        ct4=(EditText)findViewById(R.id.et_ct4);
        ct5=(EditText)findViewById(R.id.et_ct5);
        ct6=(EditText)findViewById(R.id.et_ct6);
        ct7=(EditText)findViewById(R.id.et_ct7);
        ct8=(EditText)findViewById(R.id.et_ct8);
        ct9=(EditText)findViewById(R.id.et_ct9);

        bot1=(EditText)findViewById(R.id.et_bot1);
        bot2=(EditText)findViewById(R.id.et_bot2);
        bot3=(EditText)findViewById(R.id.et_bot3);
        bot4=(EditText)findViewById(R.id.et_bot4);
        bot5=(EditText)findViewById(R.id.et_bot5);

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xckcxsbrbl);
        super.onCreate(savedInstanceState);
        initWidgets();
    }
}
