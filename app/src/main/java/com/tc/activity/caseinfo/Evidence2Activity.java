package com.tc.activity.caseinfo;

/**
 * Created by 123 on 2017/9/4.
 */

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.bean.EvidenceBean;
import com.tc.util.CaseUtil;
import com.tc.util.ConfirmDialog;
import com.tc.view.DateWheelDialogN;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evidence2Activity extends CaseBaseActivity {

    private static final String TAG = EvidenceActivity.class.getSimpleName();
    private static final String EVIDENCE_NAME = "CYQZZJQD";
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

    private EditText mWorkOne;
    private EditText mWorkTwo;
    private EditText edt_chouyangaddress,edt_time;
    String year ="";
    String month = "";
    String day="";

    private List<String> allList = new ArrayList<String>();
    private CommonAdapter2 mCommonAdapter2 = new CommonAdapter2(Evidence2Activity.this);
    private ListView  docList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence2);
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
        mTitleTx.setText("抽样取证证据清单");
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
        mBirthDay.setOnClickListener(mClickListener);
        mLivePlace = (EditText)findViewById(R.id.edt_live_place);
        mWorkPlace = (EditText)findViewById(R.id.edt_work_place);
        mPhone = (EditText)findViewById(R.id.edt_phone);
        edt_time = (EditText)findViewById(R.id.edt_time);
        edt_time.setOnClickListener(mClickListener);

        mWorkOne = (EditText)findViewById(R.id.edt_worker1);
        mWorkTwo = (EditText)findViewById(R.id.edt_worker2);
        edt_chouyangaddress = (EditText)findViewById(R.id.edt_chouyangaddress);

        mListView = (ListView)findViewById(R.id.listview_evidece);
        mCommonAdapter = new CommonAdapter();
        mListView.setAdapter(mCommonAdapter);
        CaseUtil.setListViewHeight(mListView);

        checkDoc();
        System.out.print("allList is "+allList.size());
        for(int i=0;i<allList.size();i++){

            UtilTc.showLog("分析  "+allList.get(i).toString());
        }
        docList =(ListView)findViewById(R.id.xsaj_brbl_doc_list);
        docList.setAdapter(mCommonAdapter2);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.edt_birthday:
                    DateWheelDialogN chooseDialog = new DateWheelDialogN(Evidence2Activity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mBirthDay.setText(time);
                        }
                    });
                    chooseDialog.setTimePickerGone(true);
                    chooseDialog.showDateChooseDialog();
                    break;
                case R.id.edt_time:
                    DateWheelDialogN chooseDialog2 = new DateWheelDialogN(Evidence2Activity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            year = time.split(" ")[0].toString().split("-")[0];
                            month =  time.split(" ")[0].toString().split("-")[1];
                            day =   time.split(" ")[0].toString().split("-")[2];
                            edt_time.setText(time);
                        }
                    });
                    chooseDialog2.setTimePickerGone(true);
                    chooseDialog2.showDateChooseDialog();
                    break;
            }
        }
    };

    @Override
    protected void getFileName(){
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
//        super.uploadDoc();
        startProcessDialog();
//        if(TextUtils.isEmpty(mNewPath)){
            getFileName();
            doScan();
//        }
        String ftpPath = geFtpPth();
        CaseUtil.startUploadFile(mNewPath,ftpPath,mName,mHandler);
    }

    @Override
    protected String geFtpPth() {
        return "xcbl-xz-cyqzzjqd";
    }

    @Override
    protected void doScan() {
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
        map.put("$JZR$",mWorkOne.getText().toString());
        map.put("$work1$",mWorkTwo.getText().toString());
        map.put("$cydd$",edt_chouyangaddress.getText().toString());

        map.put("$year$",year);
        map.put("$month$",month);
        map.put("$day$",day);

        for(int i=0;i<mEvidenceList.size();i++){
            EvidenceBean evidenceBean = mEvidenceList.get(i);
            map.put("$NUMBER"+i+"$",i+"");
            map.put("$NAME"+i+"$",evidenceBean.name);
            map.put("$STANDARD"+i+"$",evidenceBean.standard);
            map.put("$NUMBER"+i+"$",evidenceBean.amount);
            map.put("$ATTR"+i+"$",evidenceBean.attr);
            map.put("$NOTE"+i+"$",evidenceBean.note);
        }
        CaseUtil.writeDoc("xzaj_cyqzzjqd.doc",file,map);
        findViewById(R.id.btn_upload).setEnabled(true);
    }



    private class CommonAdapter extends BaseAdapter {

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
                view = LayoutInflater.from(Evidence2Activity.this)
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

    ////********************ListDoc******************************
    private class CommonAdapter2 extends BaseAdapter {

        Activity mContent ;
        public CommonAdapter2(Activity mCtx){
            mContent =mCtx;
        }
        @Override
        public int getCount() {

            if (allList!=null)
            {
                UtilTc.showLog(" bltxt.size()"+ allList.size());
                return allList.size();
            }
            UtilTc.showLog("返回0了");
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (allList != null) {
                return allList.get(position);
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
            Log.e("e", "getView");
            ViewHolder holder = null;
            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.item_bltxt, null);
                holder = new  CommonAdapter2.ViewHolder();


                holder.tv_blTitle = (TextView) mView.findViewById(R.id.tv_blTitle);
                holder.iv_delete = (ImageView) mView.findViewById(R.id.iv_delete);
                holder.iv_edit = (ImageView) mView.findViewById(R.id.iv_edit);

                holder.parentLayout = (LinearLayout) mView.findViewById(R.id.lin_bl);

                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }

            //word文件删除
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    final ConfirmDialog confirmDialog = new ConfirmDialog(mContent, "确定要删除吗?", "删除", "取消");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();

                            File file=null;
                            String filename = allList.get(position);
                            file = new File(Values.PATH_BOOKMARK + EVIDENCE_NAME+"/"+filename);

                            if(file.exists())
                            {
                                boolean isDel = file.delete();
                                if(isDel)
                                {
                                    allList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void doCancel() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();
                        }
                    });


                }
            });

            //word文件编辑
            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {


                    String filename = allList.get(position);
                    String filepath =  Values.PATH_BOOKMARK + EVIDENCE_NAME+"/"+filename;

                    CaseUtil.doOpenWord(filepath,mContent);


                }
            });


            String ret = allList.get(position);
            UtilTc.showLog(position+"每一项:"+ret);
            holder.tv_blTitle.setText(ret);
            return mView;
        }
        private class ViewHolder {
            TextView tv_blTitle;
            LinearLayout parentLayout;
            ImageView iv_delete,iv_edit;
        }
    }
    // -------------------------遍历文件
    private void checkFileName(File[] files, String jqNum)
    {

        if (files != null)// nullPointer
        {
            for (File file : files)
            {
                if (file.isDirectory()) {
                    checkFileName(file.listFiles(), jqNum);
                }
                else
                {
                    String fileName = file.getName();

                    if (fileName.startsWith(jqNum) && fileName.endsWith(".doc"))
                    {
                        UtilTc.showLog("名称  "+fileName);
                        allList.add(fileName);
                    }
                }
            }
        }
    }
    private void checkDoc()
    {
        File file = new File(Values.PATH_BOOKMARK + EVIDENCE_NAME+"/");
        checkFileName(file.listFiles(),mName);
    }
////********************ListDoc******************************

}
