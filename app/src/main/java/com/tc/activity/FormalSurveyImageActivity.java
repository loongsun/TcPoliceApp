package com.tc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sdses.tool.UtilTc;
import com.tc.application.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormalSurveyImageActivity extends Activity {

    @BindView(R.id.btn_xczpReturn)
    ImageView btnXczpReturn;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    private ImageView btn_xczpReturn,iv_1,iv_2;
    private Button btn_1,btn_2,btn_3;
    private void initWidgets(){
        iv_1=(ImageView)findViewById(R.id.iv_1);
        iv_2=(ImageView)findViewById(R.id.iv_2);
        btn_xczpReturn=(ImageView)findViewById(R.id.btn_xczpReturn);
        btn_1=(Button)findViewById(R.id.btn_p1);
        btn_2=(Button)findViewById(R.id.btn_p2);
        btn_3 = (Button) findViewById(R.id.btn_upload);

        btn_3.setOnClickListener(new OnClick());
        btn_1.setOnClickListener(new OnClick());
        btn_2.setOnClickListener(new OnClick());
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

                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),ActivityTraceEvidence.class);
                    startActivity(intent);

                    break;

            }

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formalsurvey_image);
        ButterKnife.bind(this);

        initWidgets();
    }

//    @OnClick({R.id.btn_xczpReturn, R.id.btn_upload})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_xczpReturn:
//                finish();
//                break;
//            case R.id.btn_upload:
//
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(),ActivityTraceEvidence.class);
//                startActivity(intent);
//
//                break;
//        }
//    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp2" + ".jpg");
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
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp1" + ".jpg");
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
}
