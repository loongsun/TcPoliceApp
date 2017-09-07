package com.tc.activity.caseinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.Account;
import com.tc.activity.FormalNewActivity;
import com.tc.activity.SenceCheck;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.bean.ImageListBean;
import com.tc.util.ConfirmDialog;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialogN;

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

import butterknife.BindView;
import butterknife.OnClick;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import static com.baidu.navisdk.BNaviModuleManager.getActivity;
import static com.tc.application.R.id.one_save_time;


public class XsajXczpActivity extends Activity  implements View.OnClickListener{

    //**************JPGList************
    private RecyclerView photoRecycleview;
    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;
    //**************JPGList************

   private TextView takePhotoTv;




    private ImageView btn_xczpReturn;
    private Button btn_update;
    private String ajNum;

    static class BackOrderAdapter extends RecyclerView.Adapter<BackOrderAdapter.ViewHolder> {

        private ArrayList<ImageListBean> mDatas;
        private Context mContext;


        public BackOrderAdapter(ArrayList<ImageListBean> titles, Context context) {
            this.mDatas = titles;
            this.mContext = context;
           // mAccount = Account.GetInstance();
        }

        @Override
        public int getItemCount() {

//            return 1;
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public  BackOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BackOrderAdapter.ViewHolder viewHolder = null;
            viewHolder = new  BackOrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder( BackOrderAdapter.ViewHolder holder, final int position) {

            holder.Image.setImageURI(Uri.parse(mDatas.get(position).getImageUrl()));
            Log.e("图片路径", mDatas.get(position).getImageUrl());


            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    final ConfirmDialog confirmDialog = new ConfirmDialog(mContext, "确定要删除吗?", "删除", "取消");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();

                            File file=null;
                            String filepath = mDatas.get(position).getImageUrl();
                            file = new File(filepath);

                            if(file.exists())
                            {
                                boolean isDel = file.delete();
                                if(isDel)
                                {
                                    mDatas.remove(position);
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


        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView Image;
            ImageView deleteImg;

            public ViewHolder(View itemView)
            {
                super(itemView);
                Image = (ImageView) itemView.findViewById(R.id.item_image);
                deleteImg = (ImageView) itemView.findViewById(R.id.xczp_img_delete);


            }
        }
    }

    private void initWidgets()
    {
        checkDoc();
        photoRecycleview=(RecyclerView)findViewById(R.id.photo_recycleview);
        photoRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new BackOrderAdapter(mImageList, this);
        photoRecycleview.setAdapter(mAdapter);



        takePhotoTv = (TextView)findViewById(R.id.take_photo_tv);
        btn_xczpReturn=(ImageView)findViewById(R.id.btn_xczpReturn);
        btn_update=(Button)findViewById(R.id.btn_upload) ;

        takePhotoTv.setOnClickListener(this);
        btn_xczpReturn.setOnClickListener(this);
        btn_update.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_xczpReturn:
                finish();
                break;

            case R.id.btn_upload:

                SendFile sf = new  SendFile();
                sf.start();
                break;
            case R.id.take_photo_tv:

                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, 2);
                break;

            default:
                break;
        }
    }



    private FTPClient myFtp;
    private int countFile=0;
    public class SendFile extends Thread {
         private String currentPath = "";

        @Override
        public void run()
        {
            try {

                if(countFile==0)
                {
                    myFtp = new FTPClient();
                    myFtp.connect("61.176.222.166", 21); // 连接
                    myFtp.login("admin", "1234"); // 登录
                    myFtp.changeDirectory("../");
                    myFtp.changeDirectory("xcbl-xs-xczp");

                    Message msg;
                    msg = Message.obtain();
                    msg.what = Values.START_UPLOAD;
                    mHandler.sendMessage(msg);
                }

                if(countFile<mImageList.size())
                {
                    currentPath = mImageList.get(countFile).getImageUrl();
                    File file = new File(currentPath);
                    MyFTPDataTransferListener listener = new  MyFTPDataTransferListener(currentPath);
                    myFtp.upload(file, listener); // 上传
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }


        }
    }
    private CustomProgressDialog progressDialog = null;
    private final static int UPLOAD = 1;
    private String errorMessage = "";
    TcApp myapp;
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

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what) {

                case Values.SUCCESS_RECORDUPLOAD://

                    countFile++;
                    if(countFile==mImageList.size())
                    {
                        UtilTc.myToast(getApplicationContext(), "传输完毕");
                        stopProgressDialog();
                        myapp.sendHandleMsg(104, SenceCheck.waitingHandler);
                        finish();
                    }
                    else
                    {
                        SendFile sf = new  SendFile();
                        sf.start();
                    }


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

        }
        @Override
        public void transferred(int length)
        {
        }

        @Override
        public void completed()
        {// 上传成功
            // TODO Auto-generated method stub
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            new Thread(media).start();


        }

        @Override
        public void failed() {// 上传失败
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

            params.add(new BasicNameValuePair("A_ID", ajNum));
            params.add(new BasicNameValuePair("A_type", "图片"));
            params.add(new BasicNameValuePair("A_Format", "jpg"));
                params.add(new BasicNameValuePair("A_MM", "/xcbl-xs-xczp"+"/"+mImageList.get(countFile).getImageName()));
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_caseinfo_xsajxczp);
        super.onCreate(savedInstanceState);
        ajNum=getIntent().getStringExtra("name");
        myapp = (TcApp) TcApp.mContent;


        mImageList = new ArrayList<>();
        initWidgets();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 2:// 当选择拍照时调用
                afterPhoto2(data);
                break;
        }
    }

    private void afterPhoto2(Intent data) {

        File file = new File(Values.PATH_XCBL_XSAJ_XCZP);
        if(!file.exists())
        {
            file.mkdirs();
        }

        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            String fileName = ajNum+"-"+UtilTc.getCurrentTime() + ".jpg";
            String filePath= Values.PATH_XCBL_XSAJ_XCZP+ fileName;
            compressImage(photo, filePath);
            if (photo == null) {

            }
            else
            {
                mImageList.add(new ImageListBean(filePath,fileName));
                //mAccount.setmImageList(mImageList);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200)
            {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            //压缩好后写入文件中
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

                        String filePath= Values.PATH_XCBL_XSAJ_XCZP+ fileName;
                        mImageList.add(new ImageListBean(filePath,fileName));
                    }
                }
            }
        }
    }
    private void checkDoc()
    {
        File file = new File(Values.PATH_XCBL_XSAJ_XCZP);
        checkFileName(file.listFiles(),ajNum);
    }
}
