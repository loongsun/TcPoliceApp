package com.tc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FormalSurveyActivity extends Fragment {


    @BindView(R.id.next_btn)
    Button nextBtn;
    Unbinder unbinder;
    @BindView(R.id.start_time)
    EditText startTime;
    @BindView(R.id.end_time)
    EditText endTime;
    @BindView(R.id.save_time)
    EditText saveTime;
    @BindView(R.id.ending_time)
    EditText endingTime;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.et_kyKydw)
    EditText etKyKydw;
    @BindView(R.id.et_kyZpbgdw)
    EditText etKyZpbgdw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_formalsurvey, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.next_btn)
    public void onViewClicked() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), FormalSurveyImageActivity.class);
        startActivity(intent);


    }

    @OnClick({R.id.start_time, R.id.end_time, R.id.save_time,R.id.ending_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_time:
                DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(getActivity(), new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        startTime.setText(time);

                    }
                });
                kyDateChooseDialog3.setDateDialogTitle("案发时间");
                kyDateChooseDialog3.showDateChooseDialog();
                break;
            case R.id.end_time:

                DateWheelDialogN kyDateChooseDialog4 = new DateWheelDialogN(getActivity(), new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endTime.setText(time);

                    }
                });
                kyDateChooseDialog4.setDateDialogTitle("结案时间");
                kyDateChooseDialog4.showDateChooseDialog();
                break;
            case R.id.save_time:

                DateWheelDialogN kyDateChooseDialog5 = new DateWheelDialogN(getActivity(), new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        saveTime.setText(time);

                    }
                });
                kyDateChooseDialog5.setDateDialogTitle("保护时间");
                kyDateChooseDialog5.showDateChooseDialog();
                break;

            case R.id.ending_time:

                DateWheelDialogN kyDateChooseDialog6 = new DateWheelDialogN(getActivity(), new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endingTime.setText(time);

                    }
                });
                kyDateChooseDialog6.setDateDialogTitle("终止时间");
                kyDateChooseDialog6.showDateChooseDialog();
                break;
        }
    }

}
