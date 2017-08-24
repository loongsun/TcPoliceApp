package com.tc.fragment.tableFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MovieFragment extends Fragment {
    @BindView(R.id.btn_userReturn)
    ImageView btnUserReturn;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.et_kyAjbh)
    EditText etKyAjbh;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.start_time)
    EditText startTime;
    @BindView(R.id.ending_time)
    EditText endingTime;
    @BindView(R.id.end_time)
    EditText endTime;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.save_time)
    EditText saveTime;
    @BindView(R.id.et_kyKydw)
    EditText etKyKydw;
    @BindView(R.id.et_kyZpbgdw)
    EditText etKyZpbgdw;
    @BindView(R.id.next_btn)
    Button nextBtn;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_userReturn, R.id.start_time, R.id.ending_time, R.id.end_time, R.id.save_time, R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_userReturn:
                break;
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
            case R.id.next_btn:
                break;
        }
    }
}
