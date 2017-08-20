package com.tc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tc.application.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：陈鹤 on 2017/8/20.
 * 版本：v1.0
 */

public class ActivityAnalysisOpinion extends Activity {

    @BindView(R.id.btn_xczpReturn)
    ImageView btnXczpReturn;
    @BindView(R.id.save_btn)
    Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analysis_opinion);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_xczpReturn, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_xczpReturn:
                finish();
                break;
            case R.id.save_btn:
                Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
