package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.bean.EvidenceBean;
import com.tc.util.CaseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnTextChanged;

import static com.tc.activity.caseinfo.BrBlActivity.BR_NAME;

public class EvidenceActivity extends Activity {

    private static final String TAG = EvidenceActivity.class.getSimpleName();
    private static final String EVIDENCE_NAME = "evidence_name";
    private String mName;
    private EditText mCaseNumber;
    private ListView mListView;
    private ArrayList<EvidenceBean> mEvidenceList = new ArrayList<>();
    private CommonAdapter mCommonAdapter;
    private ImageView mBackImg;
    private TextView mTitleTx;
    private EditText mNumberEdt;
    private EditText mOfficeName;
    private EditText mPoliceName;
    private EditText mReason;
    private EditText mEvidencePerson;
    private EditText mGender;
    private EditText mBirthDay;
    private EditText mLivePlace;
    private EditText mWorkPlace;
    private EditText mPhone;
    private EditText mLawName;
    private EditText mWorkOne;
    private EditText mWorkTwo;
    private EditText mWitness;
    private String mNewPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence);
        initData();
        initView();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        mName = extras.getString("name");
        Log.i(TAG, "name = " + mName);
//        mEdtNumber.setText(mName);
        for(int i=0;i< 6;i++){
            mEvidenceList.add(new EvidenceBean());
        }
    }

    private void initView() {
        mBackImg = (ImageView)findViewById(R.id.img_back);
        mTitleTx = (TextView)findViewById(R.id.tx_head_title);
        mTitleTx.setText("证据登记清单");
        mBackImg.setOnClickListener(mClickListener);

        mCaseNumber = findViewById(R.id.edt_number);

        mNumberEdt = (EditText)findViewById(R.id.edt_number);
        mNumberEdt.setText(mName);
        mOfficeName = (EditText)findViewById(R.id.edt_office_name);
        mPoliceName = (EditText)findViewById(R.id.edt_police_name);
        mReason = (EditText)findViewById(R.id.edt_reason);
        mEvidencePerson = (EditText)findViewById(R.id.edt_evidence_people);
        mGender = (EditText)findViewById(R.id.edt_gender);
        mBirthDay = (EditText)findViewById(R.id.edt_birthday);
        mLivePlace = (EditText)findViewById(R.id.edt_live_place);
        mWorkPlace = (EditText)findViewById(R.id.edt_work_place);
        mPhone = (EditText)findViewById(R.id.edt_phone);
        mLawName = (EditText)findViewById(R.id.edt_law);

        mWorkOne = (EditText)findViewById(R.id.edt_worker1);
        mWorkTwo = (EditText)findViewById(R.id.edt_worker2);
        mWitness = (EditText)findViewById(R.id.edt_witness);

        mListView = (ListView)findViewById(R.id.listview_evidece);
        mCommonAdapter = new CommonAdapter();
        mListView.setAdapter(mCommonAdapter);
        setListViewHeightBasedOnChildren(mListView);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;
            }
        }
    };

    private void getFileName(){
        try{
            File file = new File( Values.PATH_BOOKMARK + EVIDENCE_NAME);
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = Values.PATH_BOOKMARK+EVIDENCE_NAME+"/"+mName+"_"+ UtilTc.getCurrentTime()+".doc";
            mNewPath = fileName;
        }catch (Exception e){
            Log.e(TAG,"getFileName ",e);
        }
    }

    public void preview(View view){
        getFileName();
        doScan();
        CaseUtil.doOpenWord(mNewPath,this);
    }

    public void printFile(View view){
        preview(view);
    }

    public void uploadFile(View view){

    }

    private void doScan() {
        File file = new File(mNewPath);
        Map<String,String> map = new HashMap<>();
        map.put("$GAJ$",mOfficeName.getText().toString());
        map.put("$REACON$",mReason.getText().toString());
        map.put("$OFFICE$",mPoliceName.getText().toString());
        map.put("$PERSON$",mEvidencePerson.getText().toString());
        map.put("$GENDER$",mGender.getText().toString());
        map.put("$BIRTHDAY$",mBirthDay.getText().toString());
        map.put("$LIVE_PLACE$",mLivePlace.getText().toString());
        map.put("$WORK_PLACE$",mWorkPlace.getText().toString());
        map.put("$PHONE$",mPhone.getText().toString());
        map.put("$LAW$",mLawName.getText().toString());
        map.put("$work1$",mWorkOne.getText().toString());
        map.put("$work2$",mWorkTwo.getText().toString());
        map.put("$EVIDENCEMAN$",mWitness.getText().toString());

        for(int i=0;i<mEvidenceList.size();i++){
            EvidenceBean evidenceBean = mEvidenceList.get(i);
            map.put("$NUMBER"+i+"$",i+"");
            map.put("$NAME"+i+"$",evidenceBean.name);
            map.put("$STANDARD"+i+"$",evidenceBean.standard);
            map.put("$NUMBER"+i+"$",evidenceBean.amount);
            map.put("$ATTR"+i+"$",evidenceBean.attr);
            map.put("$NOTE"+i+"$",evidenceBean.note);
        }
        CaseUtil.writeDoc("zjdj.doc",file,map);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项Vie的宽高w
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


private class CommonAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mEvidenceList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(EvidenceActivity.this)
                        .inflate(R.layout.item_evidence_view, viewGroup, false);
                myViewHolder = new MyViewHolder();
                myViewHolder.nameEdt = (EditText)view.findViewById(R.id.et_name);
                myViewHolder.standardEdt = (EditText)view.findViewById(R.id.et_standard);
                myViewHolder.numberEdt = (EditText)view.findViewById(R.id.et_number);
                myViewHolder.attrEdt = (EditText)view.findViewById(R.id.et_attr);
                myViewHolder.noteEdt = (EditText)view.findViewById(R.id.et_note);
                view.setTag(myViewHolder);
            }else{
                myViewHolder = (MyViewHolder)view.getTag();
            }
            final EvidenceBean evidenceBean = mEvidenceList.get(i);
            myViewHolder.nameEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    evidenceBean.name = editable.toString();
                }
            });

            myViewHolder.standardEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    evidenceBean.standard = editable.toString();
                }
            });
            myViewHolder.numberEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    evidenceBean.amount=editable.toString();
                }
            });
            myViewHolder.attrEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    evidenceBean.attr = editable.toString();
                }
            });
            myViewHolder.noteEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    evidenceBean.note = editable.toString();
                }
            });

            return view;
        }

        private class MyViewHolder{
            EditText nameEdt;
            EditText standardEdt;
            EditText numberEdt;
            EditText attrEdt;
            EditText noteEdt;

            TextView nameTx;
            TextView numberTx;
            TextView standardTx;
            TextView attrTx;
            TextView noteTx;
        }


    }


}
