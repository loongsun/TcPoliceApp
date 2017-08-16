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

public class XsajFxbgActivity extends Activity {
    private ImageView btn_fxbgReturn;
    private EditText et_qyName,et_bgtime;
    private void initWidgets() {
        btn_fxbgReturn = (ImageView) findViewById(R.id.btn_fxbgReturn);
        btn_fxbgReturn.setOnClickListener(new OnClick());
        et_qyName=(EditText)findViewById(R.id.et_qyName);
        et_qyName.setText(getIntent().getStringExtra("name"));
        et_bgtime=(EditText)findViewById(R.id.et_bgtime);
        et_bgtime.setOnClickListener(new OnClick());
    }

   class OnClick implements View.OnClickListener{
       @Override
       public void onClick(View v) {
           switch (v.getId()){
               case R.id.btn_fxbgReturn:
                    finish();
                   break;
               case R.id.et_bgtime:
                   DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XsajFxbgActivity.this, new DateWheelDialogN.DateChooseInterface() {
                       @Override
                       public void getDateTime(String time, boolean longTimeChecked) {
                           et_bgtime.setText(time);

                       }
                   });
                   startDateChooseDialog.setDateDialogTitle("报告时间");
                   startDateChooseDialog.showDateChooseDialog();
                   break;
           }
       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xsajfxbg);
        initWidgets();
        super.onCreate(savedInstanceState);
    }
}
