package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

/**
 * Created by Administrator on 2017-08-12.
 */

public class XsajJszjclActivity extends Activity {
    private ImageView btn_jsclReturn;
    private EditText et_qyName,et_jszqTjsj,et_jszqbgsj,et_jszqbasj;
    private void initWidgets(){
        btn_jsclReturn=(ImageView)findViewById(R.id.btn_jsclReturn);
        btn_jsclReturn.setOnClickListener(new OnClick());
        et_qyName=(EditText)findViewById(R.id.et_qyName);
        et_qyName.setText(getIntent().getStringExtra("name"));
        et_jszqTjsj=(EditText)findViewById(R.id.et_jszqTjsj);
        et_jszqTjsj.setOnClickListener(new OnClick());

        et_jszqbgsj=(EditText)findViewById(R.id.et_jszqbgsj);
        et_jszqbgsj.setOnClickListener(new OnClick());

        et_jszqbasj=(EditText)findViewById(R.id.et_jszqbasj);
        et_jszqbasj.setOnClickListener(new OnClick());

    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_jsclReturn:
                    finish();
                    break;
                case R.id.et_jszqTjsj:
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XsajJszjclActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jszqTjsj.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("提交时间");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_jszqbgsj:
                    DateWheelDialogN startDateChooseDialog1 = new DateWheelDialogN(XsajJszjclActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jszqbgsj.setText(time);

                        }
                    });
                    startDateChooseDialog1.setDateDialogTitle("保管时间");
                    startDateChooseDialog1.showDateChooseDialog();
                    break;
                case R.id.et_jszqbasj:
                    DateWheelDialogN startDateChooseDialog2 = new DateWheelDialogN(XsajJszjclActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_jszqbasj.setText(time);

                        }
                    });
                    startDateChooseDialog2.setDateDialogTitle("办案时间");
                    startDateChooseDialog2.showDateChooseDialog();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_jszjclqd);
        super.onCreate(savedInstanceState);
        initWidgets();
    }
}
