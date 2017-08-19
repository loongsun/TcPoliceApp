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

import com.tc.activity.caseinfo.JcBlActivity;
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

        waitingHandler = new Handler(){//��������Ϣ����
            @Override
            public void handleMessage(Message msg){
                switch(Integer.valueOf(msg.obj.toString()))
                {
                    case 100:
                        mCommonAdapter.updateView(0);
                        break;
                }
            }
        };
        super.onCreate(savedInstanceState);
    }



    private void initData(){
        stateList.clear();
        stateList.add("�ֳ���¼");
        stateList.add("����¼");
        stateList.add("�����¼");
        stateList.add("���ϱ�¼");
        stateList.add("֤�ݵǼ��嵥");
        stateList.add("���еǼǱ���֤���嵥");
        stateList.add("����ȡ֤֤���嵥");
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
            //�õ���һ������ʾ�ؼ���λ�ã�
            int visiblePosition = lv_xqqzList.getFirstVisiblePosition();
            //ֻ�е�Ҫ���µ�view�ڿɼ���λ��ʱ�Ÿ��£����ɼ�ʱ������������
            if (itemIndex - visiblePosition >= 0) {
                //�õ�Ҫ���µ�item��view
                View view = lv_xqqzList.getChildAt(itemIndex - visiblePosition);
                //��view��ȡ��holder
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
            startActivity(new Intent(SenceCheck2.this,TqhjActivity.class)
                    .putExtra("name",name));
        }else if(position==3){
            //现在勘查�?
            startActivity(new Intent(SenceCheck2.this, XsajHuaTuActivity.class));
        }else if(position==4){
            //现场照片
            startActivity(new Intent(SenceCheck2.this, XsajXczpActivity.class));
        }else if(position==5){

            startActivity(new Intent(SenceCheck2.this, XsajFxbgActivity.class)
                    .putExtra("name",name));

        }else if(position==6){
            startActivity(new Intent(SenceCheck2.this, XsajJszjclActivity.class)
                    .putExtra("name",name));

        }
    }
}