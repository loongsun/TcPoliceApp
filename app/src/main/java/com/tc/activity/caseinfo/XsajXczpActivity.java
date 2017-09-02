package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.SenceCheck;
import com.tc.app.TcApp;
import com.tc.application.R;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class XsajXczpActivity extends Activity {
    private ImageView btn_xczpReturn,iv_1,iv_2;
    private Button btn_1,btn_2,btn_update;
    private static String picPath1=Environment.getExternalStorageDirectory() + "/" + "TC/xczp1" + ".jpg";
    private static String picPath2=Environment.getExternalStorageDirectory() + "/" + "TC/xczp2" + ".jpg";
    private String ajNum;
    private int upflag=1;
    private void initWidgets()
    {
        iv_1=(ImageView)findViewById(R.id.iv_1);
        iv_2=(ImageView)findViewById(R.id.iv_2);
        btn_xczpReturn=(ImageView)findViewById(R.id.btn_xczpReturn);
        btn_1=(Button)findViewById(R.id.btn_p1);
        btn_2=(Button)findViewById(R.id.btn_p2);
        btn_update=(Button)findViewById(R.id.btn_upload) ;

        btn_1.setOnClickListener(new OnClick());
        btn_2.setOnClickListener(new OnClick());
        btn_update.setOnClickListener(new OnClick());

        btn_xczpReturn.setOnClickListener(new OnClick());

    }
    class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_xczpReturn:
                    finish();
                    break;
                case R.id.btn_p1:
                    UtilTc.showLog("p1 click");
                    Intent cameraintent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraintent, 1);
                    break;
                case R.id.btn_p2:
                    Intent cameraintent2 = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraintent2, 2);
                    break;
                case R.id.btn_upload:
                    File file1=new File(picPath1);
                    File file2=new File(picPath2);
                    if(file1.exists())
                    {
                        if(file2.exists())
                        {
                            SendFile sf = new SendFile();
                            sf.start();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"��Ƭ��ȫ",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"��Ƭ��ȫ",Toast.LENGTH_SHORT).show();
                    }
                    break;


            }

        }
    }

    private FTPClient myFtp;
    public class SendFile extends Thread {
        // private String currentPath = "";

        @Override
        public void run()
        {
            try {
                MyFTPDataTransferListener listener = new MyFTPDataTransferListener("");
                File file;//=new File(picPath1);
                if(upflag==1)
                {
                    myFtp = new FTPClient();
                    myFtp.connect("61.176.222.166", 21); // ����
                    myFtp.login("admin", "1234"); // ��¼
                    myFtp.changeDirectory("../");
                    myFtp.changeDirectory("xcbl-xs-xczp");
                    file=new File(picPath1);

                }
                else
                {
                    file=new File(picPath2);
                }
                myFtp.upload(file, listener); // �ϴ�


            } catch (Exception e)
            {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }
    private CustomProgressDialog progressDialog = null;
    private final static int UPLOAD = 1;
    private String errorMessage = "";
    TcApp myapp;
    // ���ȿ�
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type)
            {
                case UPLOAD:
                    progressDialog.setMessage("�����ϴ���Ϣ,���Ժ�");
                    break;
            }
        }
        progressDialog.show();
    }

    // ȡ�����ȿ�
    private void stopProgressDialog() {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what) {
                case Values.ONE_UPLOAD://
                    UtilTc.myToast(getApplicationContext(), "��һ���ϴ��ɹ�");
                    File file=new File(picPath1);
                   file.deleteOnExit();
                    upflag=2;
                    SendFile sf = new SendFile();
                    sf.start();
                    break;
                case Values.TWO_UPLOAD://
                    File file2=new File(picPath2);
                    file2.deleteOnExit();

                    UtilTc.myToast(getApplicationContext(), "�ڶ����ϴ��ɹ�");
                    myapp.sendHandleMsg(104, SenceCheck.waitingHandler);
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
                    UtilTc.showLog("�������쳣");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();

                    break;
                case Values.START_UPLOAD:
                    UtilTc.showLog("�ļ���ʼ�ϴ�");
                    startProgressDialog(1);
                    //�ı侯��״̬
                    break;
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ��ɹ�");
                    stopProgressDialog();
                    //�ı侯��״̬
                    break;

                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ�ʧ��");
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
        public void started() {// �ϴ���ʼ
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.START_UPLOAD;;
            mHandler.sendMessage(msg);
        }
        @Override
        public void transferred(int length)
        {
        }

        @Override
        public void completed()
        {// �ϴ��ɹ�
            // TODO Auto-generated method stub

            Message msg;
            msg = Message.obtain();
            msg.what = Values.SUCCESS_UPLOAD;
            mHandler.sendMessage(msg);

            new Thread(media).start();
        }

        @Override
        public void failed() {// �ϴ�ʧ��
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler.sendMessage(msg);
        }
    }

    Runnable media = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //��ѯ������ʱ��͵����ֳ�ʱ��
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", ajNum));
            params.add(new BasicNameValuePair("A_type", "ͼƬ"));
            params.add(new BasicNameValuePair("A_Format", "jpg"));

            if(upflag==1)
            params.add(new BasicNameValuePair("A_MM", "/xcbl-xs-xczp"+"/"+"xczp1.jpg"));
            else
                params.add(new BasicNameValuePair("A_MM", "/xcbl-xs-xczp"+"/"+"xczp2.jpg"));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200)
                {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    // json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0"))
                    {
                        //�ϴ��ɹ�
                        if(upflag==1)
                        mHandler.sendEmptyMessage(Values.ONE_UPLOAD);
                        else
                            mHandler.sendEmptyMessage(Values.TWO_UPLOAD);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xsajxczp);
        super.onCreate(savedInstanceState);
        ajNum=getIntent().getStringExtra("name");
        myapp = (TcApp) TcApp.mContent;
        initWidgets();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                afterPhoto(data);
                break;
            case 2:// ��ѡ������ʱ����
                afterPhoto2(data);
                break;
        }
    }

    private void afterPhoto2(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
            compressImage(photo,picPath2);
            if (photo == null) {
                iv_2.setImageResource(R.drawable.icon_photo);
            } else {

                iv_2.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void afterPhoto(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
            compressImage(photo, picPath1);
            if (photo == null) {
                iv_1.setImageResource(R.drawable.icon_photo);
            } else {

                iv_1.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200)
            {    //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
                baos.reset();//����baos�����baos
                options -= 10;//ÿ�ζ�����10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��
            }
            //ѹ���ú�д���ļ���
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
