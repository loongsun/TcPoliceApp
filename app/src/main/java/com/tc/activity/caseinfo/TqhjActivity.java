package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;


public class TqhjActivity extends Activity {
    private ImageView btn_kcqzListReturn;
    private EditText et_hjAjbh,et_hjtqsj;
    private void initWidgets(){
        btn_kcqzListReturn=(ImageView)findViewById(R.id.btn_kcqzListReturn);
        btn_kcqzListReturn.setOnClickListener(new OnBtnClick());
        et_hjAjbh=(EditText)findViewById(R.id.et_hjAjbh);
        et_hjAjbh.setText(getIntent().getStringExtra("name"));
        et_hjtqsj=(EditText)findViewById(R.id.et_hjtqsj);
        et_hjtqsj.setOnClickListener(new OnBtnClick());
    }
    class OnBtnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_kcqzListReturn:
                    finish();
                    break;
                case R.id.et_hjtqsj:
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(TqhjActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_hjtqsj.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("提取时间");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caseinfo_kcqzlist);
        initWidgets();
    }
}
