package com.tc.activity.casemain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.ChagePassActivity;
import com.tc.activity.LoginActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 123 on 2017/8/24.
 */

public class KcqzAddAjActivity extends Activity {

    RadioButton rb_xzaj,rb_xsaj;
    EditText tv_anjian_name;
    Button save;
    ImageView btn_blReturn;
    String a_name,error;
    String a_type = "刑事案件";
//    Values.USERNAME
    //用户名
    private CustomProgressDialog progressDialog = null;

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);

            progressDialog.setMessage("正在修改,请稍后");
        }
        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initWidgets() {
        tv_anjian_name = (EditText) findViewById(R.id.tv_anjian_name);
        save = (Button) findViewById(R.id.save);
        rb_xsaj = (RadioButton) findViewById(R.id.rb_xsaj);
        rb_xzaj = (RadioButton) findViewById(R.id.rb_xzaj);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_anjian_name.getText().toString().equals("")){
                    UtilTc.myToast(KcqzAddAjActivity.this,"案件名称不能为空");
                    return ;
                }
                startProgressDialog();
                new Thread(addAnjianRun).start();
            }
        });

        rb_xzaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_type = "行政案件";
                Log.e("e","本点击"+a_type);
            }
        });
        rb_xsaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_type = "刑事案件";
                Log.e("e","本点击"+a_type);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anjian);
        ButterKnife.bind(this);
        btn_blReturn = (ImageView)findViewById(R.id.btn_blReturn);
        btn_blReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initWidgets();

    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case 200:
                    UtilTc.myToast(getApplicationContext(), "成功");
                    stopProgressDialog();
                    TcApp ia = (TcApp)getApplicationContext();
                    ia.sendHandleMsg(9988,KcqzMainList.stateHandler);
                    finish();
                    break;
                case 404:
                    UtilTc.myToast(getApplicationContext(), error);
                    stopProgressDialog();
                    break;
            }
        }

        ;
    };


    Runnable addAnjianRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String url_passenger = "http://61.176.222.166:8765/interface/addaj/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_Type",a_type));
            params.add(new BasicNameValuePair("PNUM", Values.USERNAME));
            params.add(new BasicNameValuePair("A_Name", tv_anjian_name.getText().toString()+"-临时笔录"));
            params.add(new BasicNameValuePair("PSTATE", "9"));
            Log.e("e","a_type is "+a_type);
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //登录
                        loginHandler.sendEmptyMessage(200);
                    } else if (code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        error = jb.getString("message");
                        loginHandler.sendEmptyMessage(404);
                    } else if (code.trim().equals("10002")) {
                        JSONObject jb = person.getJSONObject("data");
                        error = jb.getString("message");
                        loginHandler.sendEmptyMessage(404);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        error = jb.getString("message");
                        loginHandler.sendEmptyMessage(404);
                    }
                } else {
                    error = "服务器异常";
                    loginHandler.sendEmptyMessage(404);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = "服务器异常";
                loginHandler.sendEmptyMessage(404);
            }
        }
    };
}