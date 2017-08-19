package com.tc.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.application.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserManageActivity extends Fragment {
    @BindView(R.id.btn_userReturn)
    ImageView btnUserReturn;
    @BindView(R.id.persion_img)
    ImageView persionImg;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.personal_register_tv)
    TextView personalRegisterTv;
    @BindView(R.id.personal_forget_tv)
    TextView personalForgetTv;
    @BindView(R.id.personal_bluetooth_tv)
    TextView personalBluetoothTv;
    @BindView(R.id.exit_btn)
    Button exitBtn;
    Unbinder unbinder;
    private ImageView btn_userReturn;

    private void inidWidgets() {
        btn_userReturn = (ImageView) this.getView().findViewById(R.id.btn_userReturn);
        btn_userReturn.setOnClickListener(new OnClick());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @butterknife.OnClick({R.id.personal_register_tv, R.id.personal_forget_tv, R.id.personal_bluetooth_tv, R.id.exit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
        case R.id.personal_register_tv:
                startActivity(new Intent(getActivity(),RegisterActivity.class));
//                getActivity().finish();
                break;
            case R.id.personal_forget_tv:
                startActivity(new Intent(getActivity(),ChagePassActivity.class));
//                getActivity().finish();
                break;
            case R.id.personal_bluetooth_tv:
                if (isBluetoothEnable()){
                    openSetting("´ò¿ªÀ¶ÑÀ");
                }else {

                }

                break;
            case R.id.exit_btn:
                getActivity().finish();
                break;
        }
    }

    public static boolean isBluetoothEnable() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }


    private int openSetting(String setting){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            startActivity(intent);
        } catch(ActivityNotFoundException ex){
            ex.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }



    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_userReturn:
                    getActivity().finish();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_5, null);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inidWidgets();
    }
}
