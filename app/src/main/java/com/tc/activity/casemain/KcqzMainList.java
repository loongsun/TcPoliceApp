package com.tc.activity.casemain;

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

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.application.R;

import java.util.ArrayList;
import java.util.List;


public class KcqzMainList extends Fragment {
    private ImageView btn_kcqzReturn;
    private ListView lv_kcqz;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    private List<PoliceStateListBean> stateList = new ArrayList<PoliceStateListBean>();

    private void initWidgets(){
        btn_kcqzReturn=(ImageView)this.getView().findViewById(R.id.btn_kcqzReturn);
        btn_kcqzReturn.setOnClickListener(new OnClick());
        lv_kcqz=(ListView)this.getView().findViewById(R.id.lv_kcqzList);
    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_caseinfo_listmain, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();
        initData();
    }
    private void initData() {
        stateList.clear();
        PoliceStateListBean pl = new PoliceStateListBean();
        pl.setJqNum("12345");
        pl.setJqName("刑事案件");
        pl.setJqTime("2017-03-03");
        stateList.add(pl);
        PoliceStateListBean pl1 = new PoliceStateListBean();
        pl1.setJqNum("67890");
        pl1.setJqName("刑事案件");
        pl1.setJqTime("2017-03-04");
        stateList.add(pl1);
        lv_kcqz.setAdapter(mCommonAdapter);
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
                        getActivity().getApplicationContext()).inflate(
                        R.layout.item_policestate_list, null);
                holder = new ViewHolder();
                holder.tv_jqName = (TextView) mView.findViewById(R.id.tv_name);
                holder.tv_jqTime = (TextView) mView.findViewById(R.id.tv_time);
                holder.tv_jqPosition = (TextView) mView
                        .findViewById(R.id.tv_address);
                holder.parentLayout = (LinearLayout) mView
                        .findViewById(R.id.lin_jqInfo);
                // holder.parentLayout.setTag(R.id.checkout_listitem_layout,
                // position);
                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.e("e", "onClick");
                        startActivity(new Intent(getActivity(), SenceCheck.class)
                        .putExtra("name",stateList.get(position).getJqNum()));
                    }
                });
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            PoliceStateListBean ret = stateList.get(position);
            holder.tv_jqName.setText(ret.getJqName());
            holder.tv_jqTime.setText(ret.getJqTime());
            holder.tv_jqPosition.setText("案件编号"+ret.getJqNum());
            return mView;
        }
        private class ViewHolder {
            TextView tv_jqName, tv_jqTime, tv_jqPosition;
            LinearLayout parentLayout;
        }
    }
}
