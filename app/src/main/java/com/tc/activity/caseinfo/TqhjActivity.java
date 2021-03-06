package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaer.sdk.utils.LogUtils;
import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.activity.dto.HjqzBean;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.client.StringUtils;
import com.tc.util.CaseUtil;
import com.tc.util.CaseUtil2;
import com.tc.util.ConfirmDialog;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialogN;
import com.tc.view.LineEditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class TqhjActivity extends Activity {
    ListView lv_xqqzList ;
    ImageView btn_kcqzListReturn;
    CommonAdapter.ViewHolder holder = null;
    private CommonAdapter mCommonAdapter = new CommonAdapter();

    String name = "";
    private TextView et_hjtqsj;
    private LineEditText et_hjjzr,et_hjAjbh;
    LinearLayout ll_time;
    String year ="";
    String month = "";
    String day="";
    private List<HjqzBean>  hjqzBeanList = new ArrayList<>();


    private String newPath = "";
    String nameRet = "";
    private final static int UPLOAD=1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;

    TcApp ia;
    String EVIDENCE_NAME = "HJQZ";
    private List<String> allList = new ArrayList<String>();
    private CommonAdapter2 mCommonAdapter2 = new CommonAdapter2(this);
    private ListView  docList;
    // 进度框
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case UPLOAD:
                    progressDialog.setMessage("正在上传信息,请稍后");
                    break;
            }
        }
        progressDialog.show();
    }
    // 取消进度框
    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private void initWidgets(){

        btn_kcqzListReturn= (ImageView)findViewById(R.id.btn_kcqzListReturn);
        btn_kcqzListReturn.setOnClickListener(new OnBtnClick());
        lv_xqqzList = (ListView)findViewById(R.id.lv_xqqzList);

        et_hjAjbh=(LineEditText)findViewById(R.id.et_hjAjbh);
        et_hjAjbh.setText(name);

        et_hjjzr=(LineEditText)findViewById(R.id.et_hjjzr);
        ll_time = (LinearLayout)findViewById(R.id.ll_time);
        et_hjtqsj=(TextView)findViewById(R.id.et_hjtqsj);

        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("时间选择器弹窗");
                DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(TqhjActivity.this,
                        new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        try{
                            year = time.split(" ")[0].toString().split("-")[0];
                            month =  time.split(" ")[0].toString().split("-")[1];
                            day =   time.split(" ")[0].toString().split("-")[2];
                            LogUtils.e(year+"  "+month+"  "+day);
                            et_hjtqsj.setText(year+"-"+month+"-"+day);
                        }catch(Exception e){

                        }
                    }
                });
                startDateChooseDialog.setDateDialogTitle("提取时间");
                startDateChooseDialog.showDateChooseDialog();
            }
        });


        for(int i =0;i<14;i++){
            HjqzBean bean = new HjqzBean();
            hjqzBeanList.add(bean);
        }
        lv_xqqzList.setAdapter(mCommonAdapter);
        setListViewHeightBasedOnChildren2(lv_xqqzList,true);
        mCommonAdapter.notifyDataSetChanged();

        checkDoc();
        docList =(ListView)findViewById(R.id.xsaj_brbl_doc_list);
        docList.setAdapter(mCommonAdapter2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            File file=new File(newPath);
            if(file.exists()) {
                file.delete();
                Log.e("result","deleted");
            }
        }
    }
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
            CommonAdapter2.ViewHolder holder = null;
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
                holder = (CommonAdapter2.ViewHolder) mView.getTag();
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

                    CaseUtil2.doOpenWord(filepath,mContent,false);


                }
            });


            String ret = allList.get(position);
            UtilTc.showLog("ret       :"+ret);
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
                        allList.add(fileName);
                    }
                }
            }
        }
    }
    private void checkDoc()
    {
        File file = new File(Values.PATH_BOOKMARK + EVIDENCE_NAME+"/");
        checkFileName(file.listFiles(),name);
    }

    public void setListViewHeightBasedOnChildren2(ListView listView, boolean kill)
    {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        int list_count = 0;
        if((listAdapter.getCount())>0)
        {
            list_count = (listAdapter.getCount())+1;
        }
        else
        {
            list_count = (listAdapter.getCount());
        }

        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        totalHeight += listItem.getMeasuredHeight();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if(kill == true)
        {
            params.height = (totalHeight+1)*list_count;
        }
        else
        {
            params.height = (totalHeight)*list_count;
        }


        listView.setLayoutParams(params);
    }
    class OnBtnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_kcqzListReturn:
                    finish();
                    break;
            }
        }
    }
//    $xh$	$name$	$jbtz$	$sl$	$tqbw$	$tqff$	$tqr$	$bz$
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caseinfo_kcqzlist);
        ia = (TcApp) TcApp.mContent;
        name=getIntent().getStringExtra("name");
        initWidgets();
    }

    private class CommonAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (hjqzBeanList != null) {
                return hjqzBeanList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (hjqzBeanList != null) {
                return hjqzBeanList.get(position);
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

            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.activity_caseinfo_kcqzlistitem, null);
                holder = new CommonAdapter.ViewHolder();

                holder.et_hjmc=(LineEditText)mView.findViewById(R.id.et_hjmc);
                holder.et_hjjbtz=(LineEditText)mView.findViewById(R.id.et_hjjbtz);
                holder.et_hjsl=(LineEditText)mView.findViewById(R.id.et_hjsl);
                holder.et_hjtqbw=(LineEditText)mView.findViewById(R.id.et_hjtqbw);
                holder.et_hjtqff=(LineEditText)mView.findViewById(R.id.et_hjtqff);
                holder.et_hjtqr=(LineEditText)mView.findViewById(R.id.et_hjtqr);
                holder.et_hjbz=(LineEditText)mView.findViewById(R.id.et_hjbz);

                mView.setTag(holder);
            } else {
                holder = (CommonAdapter.ViewHolder) mView.getTag();
            }

            holder.et_hjmc.setText(hjqzBeanList.get(position).getName());
            holder.et_hjjbtz.setText(hjqzBeanList.get(position).getJbtz());
            holder.et_hjsl.setText(hjqzBeanList.get(position).getSl());
            holder.et_hjtqbw.setText(hjqzBeanList.get(position).getTqbw());
            holder.et_hjtqff.setText(hjqzBeanList.get(position).getTqff());
            holder.et_hjtqr.setText(hjqzBeanList.get(position).getTqr());
            holder.et_hjbz.setText(hjqzBeanList.get(position).getBz());

            holder.et_hjtqr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setTqr(editable.toString());
                }
            });
            holder.et_hjbz.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setBz(editable.toString());
                }
            });
            holder.et_hjmc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    LogUtils.e(position+"内容"+editable.toString());
                    hjqzBeanList.get(position).setName(editable.toString());
                }
            });

            holder.et_hjjbtz.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setJbtz(editable.toString());
                }
            });

            holder.et_hjsl.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setSl(editable.toString());
                }
            });

            holder.et_hjtqbw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setTqbw(editable.toString());
                }
            });

            holder.et_hjtqff.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    hjqzBeanList.get(position).setTqff(editable.toString());
                }
            });

            return mView;
        }

        private class ViewHolder {
            private LineEditText et_hjmc;
            private LineEditText et_hjjbtz;
            private LineEditText et_hjsl;
            private LineEditText et_hjtqbw;
            private LineEditText et_hjtqff;
            private LineEditText et_hjtqr;
            private LineEditText et_hjbz;

        }
    }



    //上传按钮
    public void BtnUploadBL(View view) {
//        File fileStart = new File(Values.ALLFILES+"wtxt/HJQZ/");
//        boolean flag = getFileName2(fileStart.listFiles(), name);
//
//        if(flag){
//            //存在本地文件
//        }else{
//            try {
//                String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//                File file = new File(sdcardPath  + "/TC/wtxt/HJQZ/");
//                if (!file.exists()){
//                    file.mkdir();
//                }
//
//                String fileName = Values.PATH_BOOKMARK+"HJQZ/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
//                newPath = fileName;
//                InputStream inputStream = getAssets().open("xsaj_hjqz.doc");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            doScan();
//        }
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/HJQZ/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                nameRet = name + "_" + UtilTc.getCurrentTime() + ".doc";
                String fileName = Values.PATH_BOOKMARK + "HJQZ/" + nameRet;
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xsaj_hjqz.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();

        startProgressDialog(UPLOAD);
        new Thread(uploadRun).start();

        SendFile sf = new SendFile();
        sf.start();
    }
    private FTPClient myFtp;
    private PoliceStateListBean plb;
    String currentFilePaht = "";
    private String currentFile="";
    private int fileCount = 0;
//    private int mTotalSize = 0;
    public class SendFile extends Thread {
        private String currentPath="";
        @Override
        public void run() {
            try {
                myFtp = new FTPClient();
                myFtp.connect("61.176.222.166", 21); // 连接
                myFtp.login("admin", "1234"); // 登录

                if(Values.dbjqList.size()>0)
                    plb= Values.dbjqList.get(0);

                File fileStart = new File(Values.ALLFILES+"wtxt/HJQZ/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                //	Log.e("path", "path"+path);
                myFtp.changeDirectory("../");
                myFtp.changeDirectory("xcbl-xs-hjqz");
                File file = new File(newPath);
                currentFilePaht="/xcbl-xs-hjqz";
                currentFile = currentFilePaht+"/"+nameRet;
                MyFTPDataTransferListener listener = new MyFTPDataTransferListener(nameRet);
                myFtp.upload(file, listener); // 上传

//                for(int i=0;i<bltxt.size();i++){
//                    //判断上传到哪个文件夹
//                    if(bltxt.get(i).endsWith(".doc")){
//                        myFtp.changeDirectory("../");
//                        myFtp.changeDirectory("xcbl-xs-hjqz");
//                        currentPath=Values.PATH_hjqz;
////                        currentFilePaht="/xcbl-xs-hjqz";
//                    }
//
//                    File file = new File(newPath);
////                    currentFile=currentFilePaht+"/"+bltxt.get(i);
//                    MyFTPDataTransferListener listener = new MyFTPDataTransferListener(bltxt.get(i));
//                    myFtp.upload(file, listener); // 上传
//                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }
    private List<String> bltxt = new ArrayList<String>();
    private void getFileName(File[] files, String jqNum) {
        bltxt.clear();
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName"+fileName);
                        bltxt.add(fileName);
                    }
                }
            }
        }
    }
    private String mediaFormat="";
    private class MyFTPDataTransferListener implements FTPDataTransferListener {

        String fileName = "";
        MyFTPDataTransferListener(String fileNameRet){
            fileName = fileNameRet;
        }
        @Override
        public void aborted() {
            // TODO Auto-generated method stub
        }
        @Override
        public void completed() {// 上传成功
            // TODO Auto-generated method stub
//            UtilTc.showLog("currentFile:"+currentFile);
//            UtilTc.showLog("currentFile 后3位"+currentFile.substring(currentFile.length()-3,currentFile.length()));
//            mediaFormat=currentFile.substring(currentFile.length()-3,currentFile.length());
//            if(mediaFormat.equals("doc")){
//                mediaType="文档";
//            }
            mediaType="文档";
            new Thread(media).start();

            File file = new File(Values.PATH_hjqz+fileName);
            if(file.exists()) {
//                boolean isDel = file.delete();
            }

            Message msg;
            msg = Message.obtain();
            msg.what = Values.SUCCESS_UPLOAD;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void failed() {// 上传失败
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void started() {// 上传开始
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = 2;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void transferred(int length) {// 上传过程监听
            int progress = length;
            Message msg;
            msg = Message.obtain();
            msg.what = 1;
            msg.obj = progress;
            mHandler1.sendMessage(msg);
        }
    }

    Handler mHandler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("文件上传成功");
                    UtilTc.myToast(TqhjActivity.this,"上传成功");
                    ia.sendHandleMsg(102, SenceCheck.waitingHandler);
                    stopProgressDialog();
                    //改变警情状态
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("文件上传失败");
                    UtilTc.myToast(TqhjActivity.this,"上传失败");
                    stopProgressDialog();
                    break;
            }
        };
    };
    //预览编辑
    public void BtneditBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/HJQZ/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                nameRet = name + "_" + UtilTc.getCurrentTime() + ".doc";
                String fileName = Values.PATH_BOOKMARK + "HJQZ/" + nameRet;
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xsaj_hjqz.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        //查看
        doOpenWord(true);
    }
    //打印笔录
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/HJQZ/");
            if (!file.exists()){
                file.mkdir();
            }
            if(StringUtils.isEmpty(newPath)) {
                nameRet = name + "_" + UtilTc.getCurrentTime() + ".doc";
                String fileName = Values.PATH_BOOKMARK + "HJQZ/" + nameRet;
                newPath = fileName;
            }
            InputStream inputStream = getAssets().open("xsaj_hjqz.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        doOpenWord(false);
    }


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_RECORDUPLOAD://

                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), ""+errorMessage);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.myToast(getApplicationContext(), "服务器异常");
                    stopProgressDialog();
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.myToast(getApplicationContext(), ""+errorMessage);
                    stopProgressDialog();
                    break;
            }
        };
    };

    Runnable uploadRun=new Runnable(){
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/xz/ADD_ZF_XS_HJQZ.asp";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID",name));

//            params.add(new BasicNameValuePair("KSSJ",et_kssj.getText().toString()));
//            params.add(new BasicNameValuePair("JSSJ",et_jssj.getText().toString()));
//            params.add(new BasicNameValuePair("JCDX",et_jcdx.getText().toString()));
//            params.add(new BasicNameValuePair("JCZHM",et_gzzjhm.getText().toString()));
//            params.add(new BasicNameValuePair("JCRYXM",et_jcryxm.getText().toString()));
//
//            params.add(new BasicNameValuePair("JCRYZW",et_zw.getText().toString()));
//            params.add(new BasicNameValuePair("JCRYGZDW",et_gzdw.getText().toString()));
//            params.add(new BasicNameValuePair("GCJJG",et_gcjg.getText().toString()));
//            params.add(new BasicNameValuePair("JCR",et_jcr1.getText().toString()+","+et_jcr2.getText().toString()));
//            params.add(new BasicNameValuePair("JLR",et_jlr.getText().toString()));


            Log.e("e","params 是"+params);
            try{
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code"+httpResponse.getStatusLine().getStatusCode());
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String strResult= EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "传回来的值是："+strResult);
                    if(strResult==null||strResult.equals("")){
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
                    if(code.trim().equals("0")){
                        JSONObject jsResult=person.getJSONObject("data");
                        errorMessage = jsResult.getString("message");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }else if(code.trim().equals("10001")){
                        JSONObject jsResult=person.getJSONObject("data");
                        errorMessage = jsResult.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
                    }
                }else{
                    //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


    private void  doScan(){
//        String startTime=mEditStartTime.getText().toString();
//        String endTime=mEdtEndTime.getText().toString();
//        if(endTime.compareTo(startTime)<=0)
//        {
//            Toast.makeText(getApplicationContext(),"结束时间要大于开始时间",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
        //获取模板文件
//        File demoFile=new File(demoPath);
        //创建生成的文件
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();

        map.put("$year$", year);
        map.put("$month$", month);
        map.put("$day$", day);
        map.put("$jzr$", et_hjjzr.getText().toString());
        map.put("$tqrAll$", "");

        int i = 1;
        for(HjqzBean item :hjqzBeanList){
            map.put("$xh"+i+"$", i+".");
            map.put("$name"+i+"$", item.getName());
            map.put("$jbtz"+i+"$", item.getJbtz());
            map.put("$sl"+i+"$", item.getSl());
            map.put("$tqbw"+i+"$", item.getTqbw());
            map.put("$tqff"+i+"$", item.getTqff());
            map.put("$tqr"+i+"$", item.getTqr());
            map.put("$bz"+i+"$", item.getBz());
            i++;
        }

        writeDoc(newFile,map);

    }
    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord(boolean ylFlag){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            startActivity(intent);
            if(ylFlag){
                startActivityForResult(intent,10);
            }
        } catch(ActivityNotFoundException e) {
            //检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
            //请先到www.olivephone.com/e.apk下载并安装
        }
    }
    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     * */
    public void writeDoc( File newFile ,Map<String, String> map)
    {
        findViewById(R.id.btn_upload).setEnabled(true);
        findViewById(R.id.btn_upload).setBackgroundColor(getResources().getColor(R.color.bluetc));
        try
        {
            InputStream in = getAssets().open("xsaj_hjqz.doc");
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // 替换文本内容
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());

            }



            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();

//            OutputStream out = new FileOutputStream(newPath);
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//
//            in.close();
//            out.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean getFileName2(File[] files, String jqNum) {
        boolean isFlag = false;
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName2(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName"+fileName);
                        isFlag =  true;

                    }
                }
            }
        }
        return isFlag;
    }

    private String 						mediaType="";
    //上传媒体信息
    Runnable media=new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //查询出出警时间和到达现场时间
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", name));
            params.add(new BasicNameValuePair("A_type", mediaType));
            params.add(new BasicNameValuePair("A_Format",mediaFormat));
            params.add(new BasicNameValuePair("A_MM",
                    currentFile));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // 取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient()
                        .execute(httpRequest);
                Log.e("code", "code"
                        + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse
                            .getEntity());
                    Log.e("e", "上传媒体的值是：" + strResult);
                    // json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //上传成功
                        //	mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };
}
