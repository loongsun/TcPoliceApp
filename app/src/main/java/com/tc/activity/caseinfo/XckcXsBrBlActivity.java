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
    private EditText et_blAjbh,et_blBeginTime,et_blEndTime;
    private void initWidgets(){
        btn_brblReturn=(ImageView)findViewById(R.id.btn_brblReturn);
        btn_brblReturn.setOnClickListener(new OnClick());
        et_blAjbh=(EditText)findViewById(R.id.et_blAjbh);
        et_blAjbh.setText(getIntent().getStringExtra("name"));
        et_blBeginTime=(EditText)findViewById(R.id.et_blBeginTime);
        et_blBeginTime.setOnClickListener(new OnClick());
        et_blEndTime=(EditText)findViewById(R.id.et_blEndTime);
        et_blEndTime.setOnClickListener(new OnClick());
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_brblReturn:
                    finish();
                    break;
                case R.id.et_blBeginTime:
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XckcXsBrBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_blBeginTime.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("开始时间");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_blEndTime:
                    DateWheelDialogN startDateChooseDialog2 = new DateWheelDialogN(XckcXsBrBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_blEndTime.setText(time);

                        }
                    });
                    startDateChooseDialog2.setDateDialogTitle("结束时间");
                    startDateChooseDialog2.showDateChooseDialog();
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
