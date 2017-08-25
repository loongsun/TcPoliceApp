package com.tc.fragment.tableFragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


@SuppressLint("NewApi")
public class AnimeFragment extends Fragment {
    @BindView(R.id.btn_xczpReturn)
    ImageView btnXczpReturn;
    @BindView(R.id.three_get_time)
    EditText getTime;
    @BindView(R.id.save_btn)
    Button saveBtn;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_xczpReturn, R.id.three_get_time, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_xczpReturn:
                break;
            case R.id.three_get_time:

                DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(getActivity(), new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        getTime.setText(time);

                    }
                });
                kyDateChooseDialog3.setDateDialogTitle("提取时间");
                kyDateChooseDialog3.showDateChooseDialog();
                break;
            case R.id.save_btn:
                break;
        }
    }
}
