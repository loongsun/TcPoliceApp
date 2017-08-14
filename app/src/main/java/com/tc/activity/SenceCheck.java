package com.tc.activity;

import com.tc.activity.caseinfo.TqhjActivity;
import com.tc.activity.caseinfo.XckcBlActivity;
import com.tc.activity.caseinfo.XckcXsBrBlActivity;
import com.tc.activity.caseinfo.XczfListActivity;
import com.tc.activity.caseinfo.XczpAcitivity;
import com.tc.activity.caseinfo.XsajDjbcqdActivity;
import com.tc.activity.caseinfo.XsajFxbgActivity;
import com.tc.activity.caseinfo.XsajHuaTuActivity;
import com.tc.activity.caseinfo.XsajJszjclActivity;
import com.tc.activity.caseinfo.XsajXczpActivity;
import com.tc.application.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SenceCheck extends Activity {
    private ImageView btn_kcqzReturn;
    private ListView lv_xqqzList;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    private List<String> stateList = new ArrayList<String>();
    private String name="";
    private void initWidgets() {
        btn_kcqzReturn = (ImageView) findViewById(R.id.btn_kcqzReturn);
        btn_kcqzReturn.setOnClickListener(new OnClick());
        lv_xqqzList=(ListView)findViewById(R.id.lv_xqqzList);
        name=getIntent().getStringExtra("name");



    }


    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_kcqzReturn:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_4);
        initWidgets();
        initData();
        super.onCreate(savedInstanceState);
    }



    private void initData(){
        stateList.clear();
        stateList.add("现场勘验笔录");
        stateList.add("辨认笔录");
        stateList.add("痕迹取证");
        stateList.add("现场勘验平面图");
        stateList.add("现场照片");
        stateList.add("现场勘验情况分析报告");
        stateList.add("接受证据材料清单");
        stateList.add("登记保存清单");
        lv_xqqzList.setAdapter(mCommonAdapter);
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
            ViewHolder holder = null;
            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        SenceCheck.this).inflate(
                        R.layout.item_list_kybl, null);
                holder = new ViewHolder();
                holder.tv_listName = (TextView) mView.findViewById(R.id.tv_name);
                holder.parentLayout = (LinearLayout) mView
                        .findViewById(R.id.lin_jqInfo);
                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.e("e", "onClick");
                        itemOnClick(position);
                    }
                });
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            holder.tv_listName.setText(stateList.get(position));
            return mView;
        }

        private class ViewHolder {
            TextView tv_listName;
            LinearLayout parentLayout;
        }
    }

    private void  itemOnClick(int position){
        if(position==0){
            startActivity(new Intent(SenceCheck.this,XckcBlActivity.class)
            .putExtra("name",name));
        }else if(position==1){
            startActivity(new Intent(SenceCheck.this,XckcXsBrBlActivity.class)
                    .putExtra("name",name));
        }else if(position==2){
            startActivity(new Intent(SenceCheck.this,TqhjActivity.class)
                    .putExtra("name",name));
        }else if(position==3){
            //现在勘查图
            startActivity(new Intent(SenceCheck.this, XsajHuaTuActivity.class));
        }else if(position==4){
            //现场照片
            startActivity(new Intent(SenceCheck.this, XsajXczpActivity.class));
        }else if(position==5){

            startActivity(new Intent(SenceCheck.this, XsajFxbgActivity.class)
                    .putExtra("name",name));

        }else if(position==6){
            startActivity(new Intent(SenceCheck.this, XsajJszjclActivity.class)
                    .putExtra("name",name));

        }else if(position==7){
            startActivity(new Intent(SenceCheck.this, XsajDjbcqdActivity.class)
                    .putExtra("name",name));
        }
    }
}