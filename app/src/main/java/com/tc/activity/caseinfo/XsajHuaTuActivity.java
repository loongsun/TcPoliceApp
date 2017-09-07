package com.tc.activity.caseinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.huatuban.HuaBanView;
import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.util.CaseUtil;
import com.tc.util.ConfirmDialog;
import com.tc.view.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class XsajHuaTuActivity extends Activity {

    ////********************ListDoc******************************
    private List<String> allList = new ArrayList<String>();
    private  CommonAdapter mCommonAdapter = new  CommonAdapter(this);
    private ListView docList;
    ////********************ListDoc******************************

    /**画画的内容区*/
    private HuaBanView hbView;
    /**设置粗细的Dialog*/
    private AlertDialog dialog;
    private View dialogView;
    private TextView shouWidth;
    private SeekBar widthSb;
    private int paintWidth;
    private Button btn_save,btn_re;


    private String ajNum;//name
    private CustomProgressDialog progressDialog = null;
    private final static int UPLOAD = 1;
    private String errorMessage = "";
    TcApp myapp;
    private String filePath;
    private String fileName;
    // 进度框
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type)
            {
                case UPLOAD:
                    progressDialog.setMessage("正在上传信息,请稍后");
                    break;
            }
        }
        progressDialog.show();
    }

    // 取消进度框
    private void stopProgressDialog() {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    class BtnOnClick implements android.view.View.OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_save:
                    fileName=ajNum+"-"+UtilTc.getCurrentTime()+".jpg";
                    filePath=Values.PATH_XCBL_XSAJ_PMT+fileName;

                    if(SaveViewUtil.saveScreen(hbView,fileName))
                    {
                        UtilTc.showLog("截图成功");
                        SendFile sf = new SendFile();
                        sf.start();
                    }
                    else{
                      //  Toast.makeText(XsajHuaTuActivity.this, "截图失败，请检查sdcard是否可用", 0).show();
                        UtilTc.showLog("截图失败");
                    }
                    break;
                case R.id.btn_re:
                    Log.e("e", "重画");
                    hbView.clearScreen();
                    break;
            }

        }

    }



    private FTPClient myFtp;
    private PoliceStateListBean plb;
    //String currentFilePaht = "";
    //private String currentFile = "";
    //private int fileCount = 0;
    //private int mTotalSize = 0;

    public class SendFile extends Thread {
       // private String currentPath = "";

        @Override
        public void run()
        {
            try {
                myFtp = new FTPClient();
                myFtp.connect("61.176.222.166", 21); // 连接
                myFtp.login("admin", "1234"); // 登录
                myFtp.changeDirectory("../");
                myFtp.changeDirectory("xcbl-xs-xckypmt");
                File file = new File(filePath);
                if(file.exists())
                {
                    MyFTPDataTransferListener listener = new  MyFTPDataTransferListener("");
                    myFtp.upload(file, listener); // 上传
                }

            } catch (Exception e)
            {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what) {
                case Values.SUCCESS_RECORDUPLOAD://
                    UtilTc.myToast(getApplicationContext(), "上传成功");
                    myapp.sendHandleMsg(103, SenceCheck.waitingHandler);
                    finish();
                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.showLog("服务器异常");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();

                    break;
            }
        }
    };

    Handler mHandler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.START_UPLOAD:
                    UtilTc.showLog("文件开始上传");
                    startProgressDialog(1);
                    //改变警情状态
                    break;
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("文件上传成功");
                    stopProgressDialog();

                    //改变警情状态
                    break;

                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("文件上传失败");
                    stopProgressDialog();
                    break;
            }
        }
    };
    private class MyFTPDataTransferListener implements FTPDataTransferListener
    {
        String fileName = "";

        MyFTPDataTransferListener(String fileNameRet)
        {
            fileName = fileNameRet;
        }

        @Override
        public void aborted() {
            // TODO Auto-generated method stub
        }
        @Override
        public void started() {// 上传开始
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.START_UPLOAD;;
            mHandler1.sendMessage(msg);
        }
        @Override
        public void transferred(int length)
        {
//            // 上传过程监听
//            int progress = length;
//            Message msg;
//            msg = Message.obtain();
//            msg.what = 1;
//            msg.obj = progress;
//            mHandler1.sendMessage(msg);
        }

        @Override
        public void completed()
        {// 上传成功
            // TODO Auto-generated method stub
            new Thread(media).start();
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




    }

    //上传媒体信息
    Runnable media = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //查询出出警时间和到达现场时间
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", ajNum));
            params.add(new BasicNameValuePair("A_type", "图片"));
            params.add(new BasicNameValuePair("A_Format", "jpg"));
            params.add(new BasicNameValuePair("A_MM", "/xcbl-xs-xckypmt"+"/"+fileName));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // 取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200)
                {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    // json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0"))
                    {
                        //上传成功
                        mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
                    }
                    else if (code.trim().equals("10003"))
                    {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                }
                else
                    {
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };
    private void initView(){

        ////********************ListDoc******************************
        checkDoc();
        docList =(ListView)findViewById(R.id.xcbl_xsaj_pmt_list);
        docList.setAdapter(mCommonAdapter);
        ////********************ListDoc******************************


        dialogView = getLayoutInflater().inflate(R.layout.dialog_width_set, null);
        shouWidth = (TextView) dialogView.findViewById(R.id.textView1);
        widthSb = (SeekBar) dialogView.findViewById(R.id.seekBar1);
        widthSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                shouWidth.setText("当前选中宽度："+(progress+1));
                paintWidth = progress+1;
            }
        });

        hbView =(HuaBanView)findViewById(R.id.huaBanView1);
        dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("设置画笔宽度").
                setView(dialogView).setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                hbView.setPaintWidth(paintWidth);
            }
        }).setNegativeButton("取消", null).create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ajNum=getIntent().getStringExtra("name");
        //去掉应用标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.acitivity_caseinfo_huatu);
        setContentView(R.layout.activity_huban_main);
        myapp = (TcApp) TcApp.mContent;
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new BtnOnClick());
        btn_re=(Button)findViewById(R.id.btn_re);
        btn_re.setOnClickListener(new BtnOnClick());
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu colorSm = menu.addSubMenu(1, 1, 1, "选择画笔颜色");
        colorSm.add(2, 200, 200, "红色");
        colorSm.add(2, 210, 210, "绿色");
        colorSm.add(2, 220, 220, "蓝色");
        colorSm.add(2, 230, 230, "紫色");
        colorSm.add(2, 240, 240, "黄色");
        colorSm.add(2, 250, 250, "黑色");
        menu.add(1, 2, 2, "设置画笔粗细");
        SubMenu widthSm = menu.addSubMenu(1, 3, 3, "设置画笔样式");
        widthSm.add(3, 300, 300, "线状画笔");
        widthSm.add(3, 301, 301, "填充画笔");
        menu.add(1, 4, 4, "清空画布");
        menu.add(1, 5, 5, "保存画布");
        menu.add(1, 6, 6, "退出应用");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int index = item.getItemId();
        switch(index){
            case 200:
                hbView.setColor(Color.BLACK);
                break;
            case 210:
                hbView.setColor(Color.GREEN);
                break;
            case 220:
                hbView.setColor(Color.BLUE);
                break;
            case 230:
                hbView.setColor(Color.MAGENTA);
                break;
            case 240:
                hbView.setColor(Color.YELLOW);
                break;
            case 250:
                hbView.setColor(Color.BLACK);
                break;
            case 2:
                dialog.show();
                break;
            case 300:
                hbView.setStyle(HuaBanView.PEN);
                break;
            case 301:
                hbView.setStyle(HuaBanView.PAIL);
                break;
            case 4:
                hbView.clearScreen();
                break;
            case 5:
                if(SaveViewUtil.saveScreen(hbView,ajNum)){
                    UtilTc.showLog("截图成功");
                }else{
                    UtilTc.showLog("截图失败");
                }
                break;
            case 6:
                finish();
                break;
        }
        return true;
    }


    ////********************ListDoc******************************
    private class CommonAdapter extends BaseAdapter {

        Activity mContent ;
        public CommonAdapter(Activity mCtx){
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
             CommonAdapter.ViewHolder holder = null;
            View mView = convertView;
            if (mView == null) {
                mView = LayoutInflater.from(
                        getApplicationContext()).inflate(
                        R.layout.item_bltxt, null);
                holder = new  CommonAdapter.ViewHolder();


                holder.tv_blTitle = (TextView) mView.findViewById(R.id.tv_blTitle);
                holder.iv_delete = (ImageView) mView.findViewById(R.id.iv_delete);
                holder.iv_edit = (ImageView) mView.findViewById(R.id.iv_edit);

                holder.parentLayout = (LinearLayout) mView.findViewById(R.id.lin_bl);

                mView.setTag(holder);
            } else {
                holder = ( CommonAdapter.ViewHolder) mView.getTag();
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
                            file = new File(Values.PATH_XCBL_XSAJ_PMT+filename);

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
                    String filepath =  Values.PATH_XCBL_XSAJ_PMT+filename;

                    CaseUtil.doOpenPhoto(filepath,mContent);



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

                    if (fileName.startsWith(jqNum) && fileName.endsWith(".jpg"))
                    {
                        allList.add(fileName);
                    }
                }
            }
        }
    }
    private void checkDoc()
    {
        File file = new File(Values.PATH_XCBL_XSAJ_PMT);
        checkFileName(file.listFiles(),ajNum);
    }
////********************ListDoc******************************

}
