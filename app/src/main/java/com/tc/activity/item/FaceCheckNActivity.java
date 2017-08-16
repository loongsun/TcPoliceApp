package com.tc.activity.item;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static loongsun.com.facetest.reconova.App.testPersonMatch;
import static loongsun.com.facetest.reconova.App.testPersonMatchNew;
import static loongsun.com.facetest.reconova.App.setupClient;
import static loongsun.com.facetest.reconova.App.testDetectFace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.android.bba.common.util.Util;
import com.sdses.bean.FaceBean;
import com.sdses.bean.WpBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.client.FcsApiClient;
import com.tc.view.CustomProgressDialog;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FaceCheckNActivity extends Activity {
    private Button btn_queryPhoto, btn_faceReturn;
    private ImageView iv_head,iv_faceBack;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 2;// ����
    private static final int PHOTO_REQUEST_GALLERY = 3;// �������ѡ��
    private static final int PHOTO_REQUEST_CUT = 4;// ���
    loongsun.com.facetest.reconova.client.FcsApiClient client = null;
    private TcApp mApp;
    private AsyncTask asyncTask1;
    private AsyncTask asyncTask2;
    private CustomProgressDialog progressDialog = null;
    private final static int QUERY_TZ = 1;
    private final static int QUERY_BD = 2;
    private final static int GETIMAGE = 3;

    String imageBase64Result = "";
    //��ʾ
    private TextView tv_hint;
    private String errorMessage = "";
    //����ֵ
    JSONObject resultRe;
    //������ʾ
    private Button btn_faceLast, btn_faceNext;
    private TextView tv_faceCount;
    private int currentItem = 1;
    private int maxItem;
    List<FaceBean> faceReList = new ArrayList<FaceBean>();

    //�������ݴ���
    private TextView tv_nameValue, tv_sexValue, tv_dateTypeValue, tv_faceReID2Num, tv_faceRyms, tv_faceReSim;

    private void initWidgets() {
        //������ʾ
        btn_faceLast = (Button) findViewById(R.id.btn_faceLast);
        btn_faceNext = (Button) findViewById(R.id.btn_faceNext);
        btn_faceLast.setOnClickListener(new OnClick());
        btn_faceNext.setOnClickListener(new OnClick());
        tv_faceCount = (TextView) findViewById(R.id.tv_faceCount);
        btn_queryPhoto = (Button) findViewById(R.id.btn_queryPhoto);
        btn_queryPhoto.setOnClickListener(new OnClick());
        btn_faceReturn = (Button) findViewById(R.id.btn_faceReturn);
        btn_faceReturn.setOnClickListener(new OnClick());
        iv_head = (ImageView) findViewById(R.id.im_headsrc);
        iv_faceBack=(ImageView)findViewById(R.id.iv_faceBack);
        mApp = (TcApp) getApplication();
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        //��������
        tv_nameValue = (TextView) findViewById(R.id.tv_nameValue);
        tv_sexValue = (TextView) findViewById(R.id.tv_sexValue);
        tv_dateTypeValue = (TextView) findViewById(R.id.tv_dateTypeValue);
        tv_faceReID2Num = (TextView) findViewById(R.id.tv_faceReID2Num);
        tv_faceRyms = (TextView) findViewById(R.id.tv_faceRyms);
        tv_faceReSim = (TextView) findViewById(R.id.tv_faceReSim);


    }

    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case QUERY_TZ:
                    progressDialog.setMessage("������ȡ����,���Ժ�");
                    break;
                case QUERY_BD:
                    progressDialog.setMessage("���ڱȶ�,���Ժ�");
                    break;
                case GETIMAGE:
                    progressDialog.setMessage("���ڻ�ȡͼ��,���Ժ�");

                    break;
            }
        }
        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_faceReturn:
                    finish();
                    break;
                case R.id.btn_queryPhoto:
                    clearInfo();
                    takeFacePhoto();
                    break;
                case R.id.btn_faceLast:
                    btnQyLast();
                    break;
                case R.id.btn_faceNext:
                    btnQyNext();
                    break;
            }
        }
    }

    //����ȡ����
    private void takeFacePhoto() {
        Intent cameraintent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // ָ������������պ���Ƭ�Ĵ���·��
                /*cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
								.getExternalStorageDirectory()
								+ "/"
								+ "temp"
								+ ".jpg")));
				startActivityForResult(cameraintent, PHOTO_REQUEST_CUT);*/
        startActivityForResult(cameraintent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faceoneton);
        initWidgets();
        clearInfo();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                afterPhoto(data);
                break;
            case PHOTO_REQUEST_TAKEPHOTO:// ��ѡ������ʱ����
                UtilTc.showLog("PHOTO_REQUEST_TAKEPHOTO");
                startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + "temp" + ".jpg")));
                break;
            case PHOTO_REQUEST_GALLERY:// ��ѡ��ӱ��ػ�ȡͼƬʱ
                // ���ǿ��жϣ������Ǿ��ò����������¼��õ�ʱ��㲻�ᱨ�쳣����ͬ
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            case PHOTO_REQUEST_CUT:// ���صĽ��

                sentPicToNext();
                faceClient();
                initTask1();
                asyncTask1.execute();
//				initTask2();
//				asyncTask2.execute();
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //Intent intent = new Intent();
        intent.setDataAndType(uri, "image/*");
        // cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
        intent.putExtra("crop", "true");
        // aspectX aspectY �ǿ�ߵı���
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 2);
        // outputX,outputY �Ǽ���ͼƬ�Ŀ��
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void sentPicToNext(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        UtilTc.showLog("1");
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo == null) {
                iv_head.setImageResource(R.drawable.lufei);
            } else {
                iv_head.setImageBitmap(photo);
            }
            //        ByteArrayOutputStream baos = null;
            UtilTc.showLog("2");
            try {
                compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "temp" + ".jpg");
                UtilTc.showLog("3");
//				baos = new ByteArrayOutputStream();
//				photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//				byte[] photodata = baos.toByteArray();
//				System.out.println(photodata.toString());
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
//                if (baos != null) {
//                    try {
//                        baos.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }

    private void afterPhoto(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "temp" + ".jpg");
            if (photo == null) {
                mHandler.sendEmptyMessage(Values.ERROR_SHOOTPHOTO);
                iv_head.setImageResource(R.drawable.photo_bg);
            } else {
                faceClient();
                initTask1();
                asyncTask1.execute();
                iv_head.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(Values.ERROR_SHOOTPHOTO);

        }

    }

    private void sentPicToNext() {
        UtilTc.showLog("1");
        UtilTc.showLog("2");
        try {
            Bitmap photo = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "temp" + ".jpg");
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "temp" + ".jpg");
            UtilTc.showLog("3");
            if (photo == null) {
                iv_head.setImageResource(R.drawable.photo_bg);
            } else {
                iv_head.setImageBitmap(photo);
            }
//				baos = new ByteArrayOutputStream();
//				photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//				byte[] photodata = baos.toByteArray();
//				System.out.println(photodata.toString());
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
//                if (baos != null) {
//                    try {
//                        baos.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;  //1��ʾ������
            if (height > reqHeight || width > reqWidth) {
                int heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        } catch (Exception e) {
            Log.d("wzc", "��:" + this.getClass().getName() + " ������" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " �쳣 " + e);
            return 1;
        }
    }

    private void faceClient() {
        int port = 80;
        String Ip = "218.61.149.35";
        if (client == null) {
            client = setupClient(Ip, port, "admin", "admin12345");
            mApp.setClient(client);
        }
    }

    private void initTask1() {
        asyncTask1 = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                com.alibaba.fastjson.JSONObject data = testDetectFace(client,
                        Environment.getExternalStorageDirectory() + "/"
                                + "temp" + ".jpg");
                UtilTc.showLog("1000000000000");
                UtilTc.showLog("tc" + data.toString());
                com.alibaba.fastjson.JSONArray result = data
                        .getJSONArray("result");
                if (result.size() > 0) {
                    com.alibaba.fastjson.JSONObject first = result
                            .getJSONObject(0);

                    return first;
                } else {
                    return "";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startProgressDialog(QUERY_TZ);
//				Toast.makeText(FaceCheckNActivity.this, "��ʼ�ϴ�",
//						Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    stopProgressDialog();
                    com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) o;
                    UtilTc.showLog("result for person" + object.toString());
                    int age = object.getInteger("age");
                    int gender = object.getInteger("gender");// get("gender");
                    imageBase64Result = object.getString("face_image");
                    UtilTc.showLog("ͼƬ�����յ���" + imageBase64Result);
                    if (UtilTc.checkConditionOk(imageBase64Result)) {
                        UtilTc.showLog("��ȡ�����ɹ�,��ʼ�ȶ�");
                        mHandler.sendEmptyMessage(Values.SUCCESS_GETIMAGETZ);
                        initTask2();
                        asyncTask2.execute();
                    } else {
                        mHandler.sendEmptyMessage(Values.ERROR_GETIMAGETZ);
                    }
//					String sex;
//					if (gender == 1)
//						sex = "��";
//					else
//						sex = "Ů";
                } catch (Exception e) {
                    e.printStackTrace();
                    UtilTc.showLog("��ȡ����ʧ��");
                    mHandler.sendEmptyMessage(Values.ERROR_GETIMAGETZ);
                }
            }
        };
    }

    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200) {    //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
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

    private void initTask2() {
        asyncTask2 = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                // String result = null;

                resultRe = testPersonMatch(client, Environment.getExternalStorageDirectory() + "/"
                        + "temp.jpg", "10402");
                return resultRe;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Toast.makeText(FaceCheckNActivity.this, "��ʼ�ϴ�2", Toast.LENGTH_SHORT).show();
                startProgressDialog(QUERY_BD);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                stopProgressDialog();
                try {
                    UtilTc.showLog("bd result" + resultRe.toString());
                    //��������
                    parseFaceData(resultRe);
                } catch (Exception e) {
                    e.printStackTrace();
                    UtilTc.showLog("Exception thread2");
                    mHandler.sendEmptyMessage(Values.ERROR_GETIMAGBD);
                }
            }
        };
    }

    private void clearInfo() {
        //�����Ϣ
        currentItem = 1;
        maxItem = 1;
        tv_hint.setText("");
        tv_faceCount.setText("1/1");
        tv_nameValue.setText("");
        tv_sexValue.setText("");
        tv_dateTypeValue.setText("");
        tv_faceReID2Num.setText("");
        tv_faceRyms.setText("");
        tv_faceReSim.setText("");
        iv_faceBack.setImageResource(R.drawable.photo_bg);
        iv_head.setImageResource(R.drawable.photo_bg);
    }

    private void parseFaceData(JSONObject person) {
        faceReList.clear();
        try {
            JSONArray jsperson = person.getJSONArray("content");
            int size = jsperson.size();
            if (size > 0) {
                //��ʾ
                UtilTc.showLog("size face" + size);
                mHandler.sendEmptyMessage(Values.SUCCESS_GETIMAGEBD);
                for (int i = 0; i < size; i++) {
                    FaceBean fb = new FaceBean();
                    JSONObject personOne = jsperson.getJSONObject(i);
                    //person������
                    JSONObject qyj = personOne.getJSONObject("person");
                    fb.setFaceReName(qyj.getString("name"));
                    fb.setFaceReSex(UtilTc.getGender(qyj.getString("gender")));
                    fb.setFaceReId2Num(qyj.getString("idCard"));
                    fb.setFaceUro(qyj.getString("faceUrl"));
                    //���ƶ�
                    fb.setFaceReSim(personOne.getString("similarity"));
                    //������  ��Ա����
                    JSONObject facedb = qyj.getJSONObject("facedb");
                    fb.setFaceReDbType(facedb.getString("name"));
                    fb.setFaceReRyMs(facedb.getString("description"));
                    faceReList.add(fb);
                    UtilTc.showLog("������ʾ��" + "\n name" + fb.getFaceReName()
                            + "\n sex" + fb.getFaceReSex() +
                            "\n dbtype" + fb.getFaceReDbType() +
                            "\n faceid2" + fb.getFaceReId2Num() +
                            "\n faceryms" + fb.getFaceReRyMs() +
                            "\n faceReSim" + fb.getFaceReSim());
                }
                currentItem = 1;
                maxItem = faceReList.size();
                UtilTc.showLog("5" + maxItem);
                tv_faceCount.setText(currentItem + "/" + maxItem);
                showPageValue(faceReList.get(0));
            } else {
                mHandler.sendEmptyMessage(Values.ERROR_FACENOFOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(Values.ERROR_GETIMAGBD);
        }
    }

    //��ҳ����
    private void showPageValue(FaceBean wpre) {
        //��������ͼƬ
        String faceUri = wpre.getFaceUro();
        getFaceImage("http://218.61.149.35" + faceUri);
        tv_nameValue.setText(wpre.getFaceReName());
        tv_sexValue.setText(wpre.getFaceReSex());
        tv_dateTypeValue.setText(wpre.getFaceReDbType());
        tv_faceReID2Num.setText(wpre.getFaceReId2Num());
        tv_faceRyms.setText(wpre.getFaceReRyMs());
        tv_faceReSim.setText(wpre.getFaceReSim());
    }

    private void getFaceImage(String path) {
        try{
            UtilTc.showLog("facePath:" + path);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(path).build();
            Call call = okHttpClient.newCall(request);
            //4.�첽��������������
            startProgressDialog(GETIMAGE);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(Values.ERROR_GETFACEIMAGE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //�õ������ϻ�ȡ��Դ��ת����������Ҫ������
                    byte[] Picture_bt = response.body().bytes();
                    //   ͨ��handler����UI
                    Message message = mHandler.obtainMessage();
                    message.obj = Picture_bt;
                    message.what = Values.SUCCESS_GETFACEIMAGE;
                    mHandler.sendMessage(message);
                }

            });
        }catch(Exception e){
            e.printStackTrace();
            mHandler.sendEmptyMessage(Values.ERROR_GETFACEIMAGE);
        }


    }
        //last click

    private void btnQyLast() {
        if (currentItem == 1) {
            tv_hint.setText("�Ѿ��ǵ�һҳ");
        } else if (currentItem - 1 >= 1) {
            currentItem = currentItem - 1;
            tv_faceCount.setText(currentItem + "/" + maxItem);
            showPageValue(faceReList.get(currentItem - 1));
        } else {
            tv_hint.setText("û�и����չʾ����");
        }
    }

    //next click
    private void btnQyNext() {
        if (currentItem == maxItem) {
            tv_hint.setText("�Ѿ������һҳ");
        } else if (currentItem + 1 <= maxItem) {
            currentItem += 1;
            tv_faceCount.setText(currentItem + "/" + maxItem);
            showPageValue(faceReList.get(currentItem - 1));
        }
    }

    //���ո�����Ϣ
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case Values.ERROR_SHOOTPHOTO:
                    tv_hint.setText("����ʧ��,������");
                    break;
                case Values.ERROR_GETIMAGETZ:
                    tv_hint.setText("��ȡ����ʧ��");
                    break;
                case Values.ERROR_GETIMAGBD:
                    tv_hint.setText("����ȶ�ʧ��");
                    break;
                case Values.SUCCESS_GETIMAGETZ:
                    startProgressDialog(QUERY_BD);
                    tv_hint.setText("��ȡ�����ɹ�,��ʼ�ȶ�");
                    break;
                case Values.SUCCESS_GETIMAGEBD:
                    tv_hint.setText("�ѷ��������ȶԽ��");
                    break;
                case Values.ERROR_FACENOFOUND:
                    tv_hint.setText("û���ҵ�ƥ������");
                    break;
                case Values.ERROR_GETFACEIMAGE:
                    tv_hint.setText("����Աͷ����Ƭ��ȡʧ��");
                    break;
                case Values.SUCCESS_GETFACEIMAGE:
                    byte[]pic=(byte[])msg.obj;
                    Bitmap bm=BitmapFactory.decodeByteArray(pic,0,pic.length);
                    iv_faceBack.setImageBitmap(bm);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
