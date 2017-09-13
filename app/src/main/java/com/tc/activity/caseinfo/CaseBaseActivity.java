package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sdses.tool.Values;
import com.tc.app.TcApp;
import com.tc.util.CaseUtil;
import com.tc.util.DateUtil;
import com.tc.view.CustomProgressDialog;
import com.tc.view.FileListView;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by zhao
 */

public abstract class CaseBaseActivity extends Activity {
    public static final String TAG = CaseBaseActivity.class.getSimpleName();
    protected String mName;
    protected String mNewPath;
    protected FileListView mFileListView;
    protected EditText mEditStartTime;
    protected EditText mEdtEndTime;
    protected String mPreFilePath;



    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Values.SUCCESS_UPLOAD:
                    stopShopProcessDialog();
                    Toast.makeText(CaseBaseActivity.this,"�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
                    break;
                case Values.ERROR_UPLOAD:
                    stopShopProcessDialog();
                    Toast.makeText(CaseBaseActivity.this,"�ϴ�ʧ�ܣ����Ժ�����",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    protected CustomProgressDialog mProcessDialog;

    protected void stopShopProcessDialog(){
        if(mProcessDialog!=null){
            mProcessDialog.dismiss();
        }
    }


    protected void startProcessDialog() {
        if(mProcessDialog == null){
            mProcessDialog = CustomProgressDialog.createDialog(this);
            mProcessDialog.setMessage("�����ϴ���Ϣ,���Ժ�");
        }
        mProcessDialog.show();
    }

    protected void initFileList(String name){
        String path = Values.PATH_BOOKMARK + name+"/";
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        if(files!=null){
            for(File file : files){
                if(file!=null && file.exists()){
                    Log.i(TAG,"doc name = "+file.getParent()+file.getName());
                    fileList.add(file.getParent()+"/"+file.getName());
                }
            }
        }
        if(fileList!=null && fileList.size()>0){
            mFileListView.setVisibility(View.VISIBLE);
            mFileListView.setFileList(fileList);
        }
    }


    protected void uploadDoc(){
        if(!CaseUtil.isNetConnent(this)){
            Toast.makeText(TcApp.mContent,"����δ����",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mEditStartTime!=null && mEdtEndTime!=null){
            if(!DateUtil.isDateRight(mEditStartTime.getText().toString(),mEdtEndTime.getText().toString())){
                Toast.makeText(this, "����ʱ��Ҫ���ڿ�ʼʱ��", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"time error");
                return;
            }
        }

        startProcessDialog();
//        if(TextUtils.isEmpty(mNewPath)){
//            getFileName();
//            doScan();
//        }
        getFileName(false);
        doScan(false);
        String ftpPath = geFtpPth();
        CaseUtil.startUploadFile(mNewPath,ftpPath,mName,mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!TextUtils.isEmpty(mPreFilePath)){
            File file = new File(mPreFilePath);
            if(file.exists()){
                file.delete();
            }
        }
    }

    protected abstract String geFtpPth();

    protected abstract void doScan(boolean isPreview);

    protected  void getFileName(boolean isPreview){
        if(isPreview){
            if(!TextUtils.isEmpty(mPreFilePath)){
                File file = new File(mPreFilePath);
                if(file!=null && file.exists()){
                    file.delete();
                }
            }
        }else{
            if(!TextUtils.isEmpty(mNewPath)){
                File file = new File(mNewPath);
                if(file!=null && file.exists()){
                    Log.i(TAG,"delete file "+mNewPath);
                    file.delete();
                }
            }
        }

    }


}
