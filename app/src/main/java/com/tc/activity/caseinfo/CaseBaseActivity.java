package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdses.tool.Values;
import com.tc.app.TcApp;
import com.tc.util.CaseUtil;
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


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Values.SUCCESS_UPLOAD:
                    stopShopProcessDialog();
                    Toast.makeText(CaseBaseActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    break;
                case Values.ERROR_UPLOAD:
                    stopShopProcessDialog();
                    Toast.makeText(CaseBaseActivity.this,"上传失败,请重试",Toast.LENGTH_SHORT).show();
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
            mProcessDialog.setMessage("正在上传信息,请稍后");
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
            Toast.makeText(TcApp.mContent,"网络不通,请连接网络",Toast.LENGTH_SHORT).show();
            return;
        }

        startProcessDialog();
        if(TextUtils.isEmpty(mNewPath)){
            getFileName();
            doScan();
        }
        String ftpPath = geFtpPth();
        CaseUtil.startUploadFile(mNewPath,ftpPath,mName,mHandler);
        mNewPath = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getFileName();//退出时删除非上传的文件
    }

    protected abstract String geFtpPth();

    protected abstract void doScan();

    protected  void getFileName(){
        if(!TextUtils.isEmpty(mNewPath)){
            File file = new File(mNewPath);
            if(file!=null && file.exists()){
                Log.i(TAG,"delete file "+mNewPath);
                file.delete();
            }
            mNewPath = "";
        }
    }


}
