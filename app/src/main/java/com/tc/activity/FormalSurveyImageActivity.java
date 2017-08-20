package com.tc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tc.application.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormalSurveyImageActivity extends Activity {

    @BindView(R.id.btn_xczpReturn)
    ImageView btnXczpReturn;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formalsurvey_image);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_xczpReturn, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_xczpReturn:
                finish();
                break;
            case R.id.btn_upload:

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),ActivityTraceEvidence.class);
                startActivity(intent);

                break;
        }
    }
}
