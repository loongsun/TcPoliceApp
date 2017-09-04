package com.tc.activity.caseinfo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.sdses.tool.Values;
import com.tc.util.CaseUtil;
import com.tc.view.CustomProgressDialog;


/**
 * Created by zhao
 */

public abstract class CaseBaseActivity extends Activity {
    protected String mName;
    protected String mNewPath;

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
            mProcessDialog.setMessage("正在上传信息，请稍后");
        }
        mProcessDialog.show();
    }

    protected void uploadDoc(){
        startProcessDialog();
        if(TextUtils.isEmpty(mNewPath)){
            getFileName();
            doScan();
        }
        String ftpPath = geFtpPth();
        CaseUtil.startUploadFile(mNewPath,ftpPath,mName,mHandler);
    }

    protected abstract String geFtpPth();

    protected abstract void doScan();

    protected abstract void getFileName();


}
