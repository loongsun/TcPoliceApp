package com.tc.activity.caseinfo;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.bean.EvidenceBean;
import com.tc.util.CaseUtil;
import com.tc.util.DateUtil;
import com.tc.view.DateWheelDialogN;
import com.tc.view.FileListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveEvidenceActivity extends CaseBaseActivity {

    private static final String TAG = EvidenceActivity.class.getSimpleName();
    private static final String SAVE_EVIDENCE_NAME = "XXDJBCZJQD";
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
    private EditText mWitness;
    private EditText mSavePlace;
    private EditText mApproval;
    private EditText mSigureTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_evidence);
        initData();
        initView();
        initFileList(SAVE_EVIDENCE_NAME);
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
        mTitleTx.setText("先行登记保存证据清单");
        mBackImg.setOnClickListener(mClickListener);

        mCaseNumber = findViewById(R.id.edt_number);
        mFileListView = (FileListView)findViewById(R.id.file_listview);


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

        mEditStartTime = (EditText)findViewById(R.id.edt_start_time);
        mEdtEndTime = (EditText)findViewById(R.id.edt_end_time);
        mEditStartTime.setOnClickListener(mClickListener);
        mEdtEndTime.setOnClickListener(mClickListener);
        mSavePlace = (EditText)findViewById(R.id.edt_save_place);

        mWorkOne = (EditText)findViewById(R.id.edt_worker1);
        mWorkTwo = (EditText)findViewById(R.id.edt_worker2);
        mWitness = (EditText)findViewById(R.id.edt_witness);
        mApproval = (EditText)findViewById(R.id.edt_approval);
        mSigureTime = (EditText)findViewById(R.id.edt_signa_time);
        mSigureTime.setOnClickListener(mClickListener);

        mListView = (ListView)findViewById(R.id.listview_evidece);
        mCommonAdapter = new CommonAdapter();
        mListView.setAdapter(mCommonAdapter);
        CaseUtil.setListViewHeight(mListView);
    }

    private String year="";
    private String month="";
    private String day="";
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.edt_birthday:
                    DateWheelDialogN chooseDialog = new DateWheelDialogN(SaveEvidenceActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mBirthDay.setText(time);
                        }
                    });
                    chooseDialog.setTimePickerGone(true);
                    chooseDialog.showDateChooseDialog();
                    break;
                case R.id.edt_start_time:
                    DateWheelDialogN startDialog = new DateWheelDialogN(SaveEvidenceActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mEditStartTime.setText(time);
                        }
                    });
                    startDialog.setTimePickerGone(true);
                    startDialog.showDateChooseDialog();
                    break;
                case R.id.edt_end_time:
                    DateWheelDialogN endDialog = new DateWheelDialogN(SaveEvidenceActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            mEdtEndTime.setText(time);
                        }
                    });
                    endDialog.setTimePickerGone(true);
                    endDialog.showDateChooseDialog();
                    break;
                case R.id.edt_signa_time:
                    DateWheelDialogN sigDialog = new DateWheelDialogN(SaveEvidenceActivity.this, new DateWheelDialogN
                            .DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            year = time.split(" ")[0].toString().split("-")[0];
                            month =  time.split(" ")[0].toString().split("-")[1];
                            day =   time.split(" ")[0].toString().split("-")[2];
                            mSigureTime.setText(time);
                        }
                    });
                    sigDialog.setTimePickerGone(true);
                    sigDialog.showDateChooseDialog();
                    break;
            }
        }
    };

    @Override
    protected void getFileName(){
        super.getFileName();
        try{
            File file = new File( Values.PATH_BOOKMARK + SAVE_EVIDENCE_NAME);
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = Values.PATH_BOOKMARK+SAVE_EVIDENCE_NAME+"/"+mName+"_"+ UtilTc.getCurrentTime()+".doc";
            mNewPath = fileName;
        }catch (Exception e){
            Log.e(TAG,"getFileName ",e);
        }
    }

    public void preview(View view){
        String startTime = mEditStartTime.getText().toString();
        String endTime = mEdtEndTime.getText().toString();
        if(!DateUtil.isDateRight(startTime,endTime)){
            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
        getFileName();
        doScan();
        CaseUtil.doOpenWord(mNewPath,this);
    }

    public void printFile(View view){
        preview(view);
    }

    public void uploadFile(View view){
        super.uploadDoc();
    }

    @Override
    protected String geFtpPth() {
        return "xcbl-xz-xxdjbczjqd";
    }

    @Override
    protected void doScan() {
        File file = new File(mNewPath);
        String startTime = mEditStartTime.getText().toString();
        String endTime = mEdtEndTime.getText().toString();
        Map<String,String> map = new HashMap<>();
        DateUtil.handleMonthMap(startTime,endTime,map);
        map.put("$GAJ$",mOfficeName.getText().toString());
        map.put("$REACON$",mReason.getText().toString());
        map.put("$OFFICE$",mPoliceName.getText().toString());
        map.put("$PERSON$",mEvidencePerson.getText().toString());
        map.put("$GENDER$",mGender.getText().toString());
        map.put("$BIRTHDAY$",mBirthDay.getText().toString());
        map.put("$LIVE_PLACE$",mLivePlace.getText().toString());
        map.put("$WORK_PLACE$",mWorkPlace.getText().toString());
        map.put("$PHONE$",mPhone.getText().toString());
        map.put("$START_TIME$", mEditStartTime.getText().toString());
        map.put("$END_TIME$", mEdtEndTime.getText().toString());
        map.put("$SAVE_PLACE$",mSavePlace.getText().toString());
        map.put("$work1$",mWorkOne.getText().toString());
        map.put("$work2$",mWorkTwo.getText().toString());
        map.put("$EVIDENCEMAN$",mWitness.getText().toString());
        map.put("$APPROVAL$",mApproval.getText().toString());
        map.put("$year$",year);
        map.put("$month$",month);
        map.put("$day$",day);
//        map.put("$SIG_TIME$",mSigureTime.getText().toString());

        for(int i=0;i<mEvidenceList.size();i++){
            EvidenceBean evidenceBean = mEvidenceList.get(i);
            map.put("$NUMBER"+i+"$",i+"");
            map.put("$NAME"+i+"$",evidenceBean.name);
            map.put("$STANDARD"+i+"$",evidenceBean.standard);
            map.put("$NUMBER"+i+"$",evidenceBean.amount);
            map.put("$ATTR"+i+"$",evidenceBean.attr);
            map.put("$NOTE"+i+"$",evidenceBean.note);
        }
        CaseUtil.writeDoc("bczj.doc",file,map);
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
                view = LayoutInflater.from(SaveEvidenceActivity.this)
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
