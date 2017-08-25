package com.tc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：陈鹤 on 2017/8/20.
 * 版本：v1.0
 */

public class ActivityTraceEvidence extends Activity {

    @BindView(R.id.btn_xczpReturn)
    ImageView btnXczpReturn;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.get_time)
    EditText getTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trace_evidence);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_xczpReturn, R.id.save_btn,R.id.get_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_xczpReturn:
                finish();
                break;
            case R.id.save_btn:
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), ActivityAnalysisOpinion.class);
                startActivity(intent);
                break;

            case R.id.get_time:

                DateWheelDialogN kyDateChooseDialog5 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        getTime.setText(time);

                    }
                });
                kyDateChooseDialog5.setDateDialogTitle("保护时间");
                kyDateChooseDialog5.showDateChooseDialog();
                break;
        }
    }

}
