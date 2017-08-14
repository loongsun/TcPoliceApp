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

public class XsajDjbcqdActivity extends Activity {
    private ImageView btn_djbcqdReturn;
    private EditText et_djbcName,et_djbcCsrq;

    private void initWidgets(){
        btn_djbcqdReturn=(ImageView)findViewById(R.id.btn_djbcqdReturn);
        btn_djbcqdReturn.setOnClickListener(new Onclick());
        et_djbcName=(EditText)findViewById(R.id.et_djbcName);
        et_djbcName.setText(getIntent().getStringExtra("name"));

        et_djbcCsrq=(EditText)findViewById(R.id.et_djbcCsrq);
        et_djbcCsrq.setOnClickListener(new Onclick());
    }
    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_djbcqdReturn:
                finish();
                break;
            case R.id.et_djbcCsrq:
                DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XsajDjbcqdActivity.this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        et_djbcCsrq.setText(time);

                    }
                });
                startDateChooseDialog.setDateDialogTitle("—°‘Ò ±º‰");
                startDateChooseDialog.showDateChooseDialog();
                break;
        }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_djbcqd);
        super.onCreate(savedInstanceState);
        initWidgets();
    }
}
