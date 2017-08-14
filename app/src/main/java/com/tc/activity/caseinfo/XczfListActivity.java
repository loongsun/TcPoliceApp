package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;

import java.util.ArrayList;
import java.util.List;


public class XczfListActivity extends Activity {
    private ListView lv_ssajList;
    private ImageView btn_kcListReturn;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    private List<String> stateList = new ArrayList<String>();

    private void initWidgets() {
        lv_ssajList = (ListView) findViewById(R.id.lv_ssajList);
        btn_kcListReturn = (ImageView) findViewById(R.id.btn_kcListReturn);
        btn_kcListReturn.setOnClickListener(new OnClick());

    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lv_ssajList:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xsajlist);
        super.onCreate(savedInstanceState);
        initWidgets();
        initData();

    }
    private void initData(){
        stateList.clear();
        stateList.add("现场勘验笔录");
        stateList.add("辨认笔录");
        lv_ssajList.setAdapter(mCommonAdapter);
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
                        XczfListActivity.this).inflate(
                        R.layout.item_list_kybl, null);
                holder = new ViewHolder();
                holder.tv_listName = (TextView) mView.findViewById(R.id.tv_name);
                holder.parentLayout = (LinearLayout) mView
                        .findViewById(R.id.lin_jqInfo);

                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.e("e", "onClick");
                        if(position==0){
                            startActivity(new Intent(XczfListActivity.this,XckcBlActivity.class));
                        }else if(position==1){
                            startActivity(new Intent(XczfListActivity.this,XckcXsBrBlActivity.class));
                        }
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

}
