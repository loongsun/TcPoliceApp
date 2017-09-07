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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaer.sdk.utils.LogUtils;
import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.activity.SenceCheck2;
import com.tc.activity.dto.DjbcqdBean;
import com.tc.app.TcApp;
import com.tc.application.R;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by Administrator on 2017-08-12.
 */

public class XsajFxbgActivity extends Activity {
    private ImageView btn_fxbgReturn;
    private LineEditText et_qyName,et_bgtime,XCFXR_edit,GZJY_edit,CBYJ_edit,et_jszqbasj,ZAGC_edit,et_zars,et_jszqbadw,
            et_jszqbgsj,et_jszqbgr,et_jszqTjsj,et_jszqTjr,et_jszqBz,et_jszqTz,et_jszqSl,et_jszqMc,et_shouduan,et_jinchukou;

    String name = "";
    private String newPath = "";
    private final static int UPLOAD=1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;
    String sex = "男";
    String year ="";
    String month = "";
    String day="";
    private List<DjbcqdBean>  hjqzBeanList = new ArrayList<>();
    TcApp ia;
    TextView et_time;
    LineEditText et_bananren,et_jianzhengren;
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

    private void initWidgets() {
        btn_fxbgReturn = (ImageView) findViewById(R.id.btn_fxbgReturn);
        btn_fxbgReturn.setOnClickListener(new OnClick());
        et_qyName=(LineEditText) findViewById(R.id.et_qyName);
        et_qyName.setText(getIntent().getStringExtra("name"));
        et_bgtime=(LineEditText) findViewById(R.id.et_bgtime);


        et_bgtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XsajFxbgActivity.this,
                        new DateWheelDialogN.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                try{
                                    year = time.split(" ")[0].toString().split("-")[0];
                                    month =  time.split(" ")[0].toString().split("-")[1];
                                    day =   time.split(" ")[0].toString().split("-")[2];
                                    et_bgtime.setText(year+"-"+month+"-"+day);
                                }catch(Exception e){

                                }
                            }
                        });
                startDateChooseDialog.setDateDialogTitle("时间选择");
                startDateChooseDialog.showDateChooseDialog();
            }
        });

        XCFXR_edit = (LineEditText)findViewById(R.id.XCFXR_edit);
        GZJY_edit = (LineEditText)findViewById(R.id.GZJY_edit);
        CBYJ_edit = (LineEditText)findViewById(R.id.CBYJ_edit);
        et_jszqbasj = (LineEditText)findViewById(R.id.et_jszqbasj);


        ZAGC_edit = (LineEditText)findViewById(R.id.ZAGC_edit);
        et_zars = (LineEditText)findViewById(R.id.et_zars);
        et_jszqbadw = (LineEditText)findViewById(R.id.et_jszqbadw);
        et_jszqbgsj = (LineEditText)findViewById(R.id.et_jszqbgsj);
        et_jszqbgr = (LineEditText)findViewById(R.id.et_jszqbgr);

        et_jszqTjsj = (LineEditText)findViewById(R.id.et_jszqTjsj);
        et_jszqTjr = (LineEditText)findViewById(R.id.et_jszqTjr);
        et_jszqBz = (LineEditText)findViewById(R.id.et_jszqBz);
        et_jszqTz = (LineEditText)findViewById(R.id.et_jszqTz);
        et_jszqSl = (LineEditText)findViewById(R.id.et_jszqSl);


        et_shouduan  = (LineEditText)findViewById(R.id.et_shouduan);
        et_jinchukou  = (LineEditText)findViewById(R.id.et_jinchukou);

        et_jszqMc  = (LineEditText)findViewById(R.id.et_jszqMc);
    }

   class OnClick implements View.OnClickListener{
       @Override
       public void onClick(View v) {
           switch (v.getId()){
               case R.id.btn_fxbgReturn:
                    finish();
                   break;
           }
       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xsajfxbg);
        initWidgets();
        ia = (TcApp) TcApp.mContent;
        name=getIntent().getStringExtra("name");
        super.onCreate(savedInstanceState);
    }



    //上传按钮
    public void BtnUploadBL(View view) {
//        File fileStart = new File(Values.ALLFILES+"wtxt/XCKYQKFXBG/");
//        boolean flag = getFileName2(fileStart.listFiles(), name);
//
//        if(flag){
//            //存在本地文件
//        }else{
//            try {
//                String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//                File file = new File(sdcardPath  + "/TC/wtxt/XCKYQKFXBG/");
//                if (!file.exists()){
//                    file.mkdir();
//                }
//
//                String fileName = Values.PATH_BOOKMARK+"XCKYQKFXBG/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
//                newPath = fileName;
//                InputStream inputStream = getAssets().open("xsaj_xckyqkfxbg.doc");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            doScan();
//        }

        startProgressDialog(UPLOAD);

        SendFile sf = new SendFile();
        sf.start();
    }
    private FTPClient myFtp;
    private PoliceStateListBean plb;
    String currentFilePaht = "";
    private String currentFile="";
    private int fileCount = 0;
    private int mTotalSize = 0;
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

                File fileStart = new File(Values.ALLFILES+"wtxt/XCKYQKFXBG/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                //	Log.e("path", "path"+path);

                for(int i=0;i<bltxt.size();i++){
                    //判断上传到哪个文件夹
                    if(bltxt.get(i).endsWith(".doc")){
                        myFtp.changeDirectory("../");
                        myFtp.changeDirectory("xcbl-xs-xckyqkfxbg");
                        currentPath=Values.PATH_XCKYQKFXBG;
                        currentFilePaht="/xcbl-xs-xckyqkfxbg";
                    }

                    File file = new File(currentPath+bltxt.get(i));
                    fileCount = (int) file.length();

                    mTotalSize = fileCount;
                    currentFile=currentFilePaht+"/"+bltxt.get(i);
                    MyFTPDataTransferListener listener = new MyFTPDataTransferListener(bltxt.get(i));
                    myFtp.upload(file, listener); // 上传
                }
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
            UtilTc.showLog("currentFile:"+currentFile);
            UtilTc.showLog("currentFile 后3位"+currentFile.substring(currentFile.length()-3,currentFile.length()));
            mediaFormat=currentFile.substring(currentFile.length()-3,currentFile.length());
            if(mediaFormat.equals("doc")){
                mediaType="文档";
            }
            new Thread(media).start();

            File file = new File(Values.PATH_XCKYQKFXBG+fileName);
            if(file.exists()) {
                boolean isDel = file.delete();
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
                    UtilTc.myToast(XsajFxbgActivity.this,"上传成功");
                    ia.sendHandleMsg(105, SenceCheck.waitingHandler);
                    stopProgressDialog();
                    //改变警情状态
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("文件上传失败");
                    UtilTc.myToast(XsajFxbgActivity.this,"上传失败");
                    stopProgressDialog();
                    break;
            }
        };
    };
    //预览编辑
    public void BtneditBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCKYQKFXBG/");
            if (!file.exists()){
                file.mkdir();
            }

            String fileName = Values.PATH_BOOKMARK+"XCKYQKFXBG/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
            newPath = fileName;
            InputStream inputStream = getAssets().open("xsaj_xckyqkfxbg.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        //查看
        doOpenWord();
    }
    //打印笔录
    public void BtnPrintBL(View view) {
        try {
            String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(sdcardPath  + "/TC/wtxt/XCKYQKFXBG/");
            if (!file.exists()){
                file.mkdir();
            }

            String fileName = Values.PATH_BOOKMARK+"XCKYQKFXBG/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
            newPath = fileName;
            InputStream inputStream = getAssets().open("xsaj_xckyqkfxbg.doc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
        doOpenWord();
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
                    ia.sendHandleMsg(101, SenceCheck2.waitingHandler);
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



    private void  doScan(){
        //获取模板文件
//        File demoFile=new File(demoPath);
        //创建生成的文件
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
//        private LineEditText et_djbcName,et_djbcmc,et_djbcAy,et_djbcDw,et_djbcCyr;
//        RadioButton rb_handMan,rb_handWomen;
//        private LineEditText et_djbcCsrq,et_djbcXzz,et_djbcGzdw,et_djbcLxDh,et_djbcZjcyr;

        LogUtils.e("et_jszqTjr.getText().toString()  "+et_jszqTjr.getText().toString());
        map.put("$y$", year);
        map.put("$m$", month);
        map.put("$r$", day);

        map.put("$ajbh$", et_qyName.getText().toString());
        map.put("$kch$", et_jszqMc.getText().toString());
        map.put("$xcfx$", et_jszqSl.getText().toString());
        map.put("$qhmbjss$", et_jszqTz.getText().toString());
        map.put("$zadj$", et_jszqBz.getText().toString());
        map.put("$zajck$", et_jinchukou.getText().toString());
        map.put("$zasd$", et_jszqTjr.getText().toString());
        map.put("$zasd2$", et_shouduan.getText().toString());
        map.put("$qrfs$", et_jszqTjsj.getText().toString());
        map.put("$zagj$", et_jszqbgr.getText().toString());
        map.put("$zadjmd$", et_jszqbgsj.getText().toString());
        map.put("$ajxz$", et_jszqbadw.getText().toString());
        map.put("$zars$", et_zars.getText().toString());
        map.put("$zagc$", ZAGC_edit.getText().toString());
        map.put("$zartd$", et_jszqbasj.getText().toString());
        map.put("$cbyjygj$", CBYJ_edit.getText().toString());

        map.put("$gzjy$", GZJY_edit.getText().toString());
        map.put("$xcfxr$", XCFXR_edit.getText().toString());
        writeDoc(newFile,map);

    }
    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            startActivity(intent);
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
        try
        {
            InputStream in = getAssets().open("xsaj_xckyqkfxbg.doc");
//            FileInputStream in = new FileInputStream(demoFile);
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

//            FileOutputStream out1 = null;
//            CustomXWPFDocument doc = new CustomXWPFDocument();
//            try {
//                URL url =  new URL("http://f.hiphotos.baidu.com/image/h%3D200/sign=333f3ac494510fb367197097e932c893/a8014c086e061d95df89434571f40ad163d9ca84.jpg");
//                BufferedInputStream fis = new BufferedInputStream(url.openStream());
//                String picId = doc.addPictureData(fis, XWPFDocument.PICTURE_TYPE_JPEG);
//                doc.createPicture(picId, doc.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_JPEG), 200, 200);
//
//                out = new FileOutputStream("simple.docx");
//                doc.write(out1);
//                out1.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
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
}
