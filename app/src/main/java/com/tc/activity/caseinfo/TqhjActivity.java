package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tc.activity.SenceCheck;
import com.tc.application.R;
import com.tc.view.DateWheelDialogN;

import java.util.ArrayList;
import java.util.List;


public class TqhjActivity extends Activity {
//    private ImageView btn_kcqzListReturn;
//    private EditText et_hjAjbh,et_hjtqsj;
    ListView lv_xqqzList ;
    CommonAdapter.ViewHolder holder = null;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    List<String> stateList = new ArrayList<>();
    private void initWidgets(){
//        btn_kcqzListReturn=(ImageView)findViewById(R.id.btn_kcqzListReturn);
//        btn_kcqzListReturn.setOnClickListener(new OnBtnClick());
//        et_hjAjbh=(EditText)findViewById(R.id.et_hjAjbh);
//        et_hjAjbh.setText(getIntent().getStringExtra("name"));
//        et_hjtqsj=(EditText)findViewById(R.id.et_hjtqsj);
//        et_hjtqsj.setOnClickListener(new OnBtnClick());


        lv_xqqzList = (ListView)findViewById(R.id.lv_xqqzList);
        for(int i =0;i<10;i++){
            stateList.add(i,"1");
        }
        lv_xqqzList.setAdapter(mCommonAdapter);

    }
    class OnBtnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_kcqzListReturn:
                    finish();
                    break;
                case R.id.et_hjtqsj:
//                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(TqhjActivity.this, new DateWheelDialogN.DateChooseInterface() {
//                        @Override
//                        public void getDateTime(String time, boolean longTimeChecked) {
//                            et_hjtqsj.setText(time);
//
//                        }
//                    });
//                    startDateChooseDialog.setDateDialogTitle("提取时间");
//                    startDateChooseDialog.showDateChooseDialog();
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

    private class CommonAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (stateList != null) {
                return stateList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (stateList != null) {
                return stateList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.activity_caseinfo_kcqzlistitem, null);
                holder = new CommonAdapter.ViewHolder();

                holder.btn_kcqzListReturn=(ImageView)mView.findViewById(R.id.btn_kcqzListReturn);

                holder.et_hjAjbh=(EditText)mView.findViewById(R.id.et_hjAjbh);

                holder.et_hjtqsj=(EditText)mView.findViewById(R.id.et_hjtqsj);
                mView.setTag(holder);
            } else {
                holder = (CommonAdapter.ViewHolder) mView.getTag();
            }

            holder.et_hjtqsj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(TqhjActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            System.out.print("選擇了时间");
                            holder.et_hjtqsj.setText(time);
                            holder.et_hjtqsj.setText("333");

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("提取时间");
                    startDateChooseDialog.showDateChooseDialog();
                }
            });
            return mView;
        }

        private class ViewHolder {
            private ImageView btn_kcqzListReturn;
            private EditText et_hjAjbh,et_hjtqsj;
        }
    }
}
