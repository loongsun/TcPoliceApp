package com.tc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tc.activity.caseinfo.BrBlActivity;
import com.tc.activity.caseinfo.EvidenceActivity;
import com.tc.activity.caseinfo.JcBlActivity;
import com.tc.activity.caseinfo.KyBlActivity;
import com.tc.activity.caseinfo.SaveEvidenceActivity;
import com.tc.activity.caseinfo.TqhjActivity;
import com.tc.activity.caseinfo.XcBlActivity;
import com.tc.activity.caseinfo.XsajFxbgActivity;
import com.tc.activity.caseinfo.XsajHuaTuActivity;
import com.tc.activity.caseinfo.XsajJszjclActivity;
import com.tc.activity.caseinfo.XsajXczpActivity;
import com.tc.application.R;

import java.util.ArrayList;
import java.util.List;

public class SenceCheck2 extends Activity {
    private ImageView btn_kcqzReturn;
    private ListView lv_xqqzList;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    private List<String> stateList = new ArrayList<String>();
    private String name="";
    TextView tv_ajname;
    private void initWidgets() {
        btn_kcqzReturn = (ImageView) findViewById(R.id.btn_kcqzReturn);
        btn_kcqzReturn.setOnClickListener(new OnClick());
        lv_xqqzList=(ListView)findViewById(R.id.lv_xqqzList);
        name=getIntent().getStringExtra("name");
        tv_ajname = (TextView)findViewById(R.id.tv_ajname);
        tv_ajname.setText(getIntent().getStringExtra("anjianname"));

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
    public static Handler waitingHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_4);
        initWidgets();
        initData();

        waitingHandler = new Handler(){//?????????????
            @Override
            public void handleMessage(Message msg){
                switch(msg.what)
                {
                    case 100:
                        mCommonAdapter.updateView(0);
                        break;
                    case 101:
                        mCommonAdapter.updateView(1);
                        break;
                    case 102:
                        mCommonAdapter.updateView(2);
                        break;
                    case 103:
                        mCommonAdapter.updateView(3);
                        break;
                    case 104:
                        mCommonAdapter.updateView(4);
                        break;
                    case 105:
                        mCommonAdapter.updateView(5);
                        break;
                    case 106:
                        mCommonAdapter.updateView(6);
                        break;
                    case 107:
                        mCommonAdapter.updateView(7);
                        break;
                }
            }
        };
        super.onCreate(savedInstanceState);
    }



    private void initData(){
        stateList.clear();
        stateList.add("现场笔录");
        stateList.add("检查笔录");
        stateList.add("勘验笔录");
        stateList.add("辨认笔录");
        stateList.add("证据登记清单");
        stateList.add("先行登记保存证据清单");
        stateList.add("抽样取证证据清单");
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
        public void updateView(int itemIndex) {
            //??????????????????λ???
            int visiblePosition = lv_xqqzList.getFirstVisiblePosition();
            //??е???????view??????λ???????????????????????????
            if (itemIndex - visiblePosition >= 0) {
                //?????????item??view
                View view = lv_xqqzList.getChildAt(itemIndex - visiblePosition);
                //??view?????holder
                ViewHolder holder = (ViewHolder) view.getTag();

                holder.iv = (ImageView) view.findViewById(R.id.iv);
                holder.iv.setBackgroundColor(getResources().getColor(R.color.Blue));
            }
        }
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        SenceCheck2.this).inflate(
                        R.layout.item_list_kybl, null);
                holder = new ViewHolder();

                holder.iv = (ImageView) mView.findViewById(R.id.iv);
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
            ImageView iv;
            LinearLayout parentLayout;
        }
    }

    private void  itemOnClick(int position){
        if(position==0){
            startActivity(new Intent(SenceCheck2.this,XcBlActivity.class)
                    .putExtra("name",name));
        }else if(position==1){
            startActivity(new Intent(SenceCheck2.this,JcBlActivity.class)
                    .putExtra("name",name));
        }else if(position==2){
            Intent intent = new Intent(SenceCheck2.this, KyBlActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
//            startActivity(new Intent(SenceCheck2.this,TqhjActivity.class)
//                    .putExtra("name",name));
        }else if(position==3){
            Intent intent = new Intent(SenceCheck2.this, BrBlActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
//            startActivity(new Intent(SenceCheck2.this, XsajHuaTuActivity.class));
        }else if(position==4){
            //证据登记
            Intent intent = new Intent(SenceCheck2.this, EvidenceActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
//            startActivity(new Intent(SenceCheck2.this, XsajXczpActivity.class));
        }else if(position==5){
            //保存证据清单
            Intent intent = new Intent(SenceCheck2.this, SaveEvidenceActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
//            startActivity(new Intent(SenceCheck2.this, XsajFxbgActivity.class)
//                    .putExtra("name",name));

        }else if(position==6){
            startActivity(new Intent(SenceCheck2.this, XsajJszjclActivity.class)
                    .putExtra("name",name));

        }
    }
}