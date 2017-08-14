package com.tc.activity.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qr_codescan.MipcaActivityCapture;
import com.sdses.bean.QyBean;
import com.sdses.bean.WpBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.view.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class GoodsQueryActivity extends Activity {
    private ImageView btn_goodsReturn;
    private Button btn_wpCamera, btn_wpCode, btn_wpName;
    private String whatCheck = "";
    private String errorMessage = "";
    //��ѯ������ʾ��
    private EditText et_codeCamera, et_wpCode, et_wpName;
    //ɨ��
    private final static int SCANNIN_GREQUEST_CODE = 1;
    //��������
    private TextView tv_wpreName, tv_wpreCode, tv_wpreSccj, tv_wpreLb, tv_wpreDate, tv_wpreSpgb;
    private JSONObject jbResult;
    List<WpBean> wpReList = new ArrayList<WpBean>();

    //��������
    private Button btn_wpLast, btn_wpNext;
    private TextView tv_wpreCount;
    private int currentItem = 1;
    private int maxItem;
    private CustomProgressDialog progressDialog = null;
    private TextView tv_hint;

    //������
    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("���ڲ�ѯ,���Ժ�");
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
        btn_goodsReturn = (ImageView) findViewById(R.id.btn_goodsReturn);
        btn_goodsReturn.setOnClickListener(new OnClick());
        //��ѯ��
        btn_wpCamera = (Button) findViewById(R.id.btn_wpCamera);
        btn_wpCode = (Button) findViewById(R.id.btn_wpCode);
        btn_wpName = (Button) findViewById(R.id.btn_wpName);
        btn_wpCamera.setOnClickListener(new OnClick());
        btn_wpCode.setOnClickListener(new OnClick());
        btn_wpName.setOnClickListener(new OnClick());
        //��ѯ����
        et_codeCamera = (EditText) findViewById(R.id.et_codeCamera);
        et_wpCode = (EditText) findViewById(R.id.et_wpCode);
        et_wpName = (EditText) findViewById(R.id.et_wpName);
        //������Ϣ
        tv_wpreName = (TextView) findViewById(R.id.tv_wpreName);
        tv_wpreCode = (TextView) findViewById(R.id.tv_wpreCode);
        tv_wpreSccj = (TextView) findViewById(R.id.tv_wpreSccj);
        tv_wpreLb = (TextView) findViewById(R.id.tv_wpreLb);
        tv_wpreDate = (TextView) findViewById(R.id.tv_wpreDate);
        tv_wpreSpgb = (TextView) findViewById(R.id.tv_wpreSpgb);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        //������Ϣ����
        tv_wpreCount = (TextView) findViewById(R.id.tv_wpreCount);
        btn_wpLast = (Button) findViewById(R.id.btn_wpreLast);
        btn_wpNext = (Button) findViewById(R.id.btn_wpreNext);
        btn_wpLast.setOnClickListener(new OnClick());
        btn_wpNext.setOnClickListener(new OnClick());

    }

    private void clearInfo() {
        //�����Ϣ
        currentItem = 1;
        maxItem = 1;
        tv_wpreCount.setText("1/1");
        tv_wpreName.setText("");
        tv_wpreCode.setText("");
        tv_wpreSccj.setText("");
        tv_wpreDate.setText("");
        tv_wpreLb.setText("");
        tv_wpreSpgb.setText("");
    }


    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_goodsReturn:
                    finish();
                    break;
                case R.id.btn_wpCamera:
                    prepare();
                    break;
                case R.id.btn_wpCode:
                    goodCodeCheck();
                    break;
                case R.id.btn_wpName:
                    goodNameCheck();
                    break;
                //���ݴ���
                case R.id.btn_wpreLast:
                    btnQyLast();
                    break;
                case R.id.btn_wpreNext:
                    btnQyNext();
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_goodsquery);
        initWidgets();
        clearInfo();

    }
    //��Ʒ���Ʋ�ѯ
    private void goodNameCheck(){
        whatCheck="2";
        clearInfo();
        String goodsName=et_wpName.getText().toString().trim();
        if(UtilTc.checkConditionOk(goodsName)){
            //go
            startProgressDialog();
            new Thread(goodsRun).start();
        }else{
            tv_hint.setText("����д��Ʒ����");
        }

    }
    //��Ʒ�����ѯ
    private void goodCodeCheck(){
        whatCheck="1";
        clearInfo();
        String goodCode=et_wpCode.getText().toString().trim();
        if(UtilTc.checkConditionOk(goodCode)){
            startProgressDialog();
            new Thread(goodsRun).start();
        }else{
            tv_hint.setText("����д��Ʒ����");
        }
    }

    private void prepare() {
        Intent intent = new Intent();
        intent.setClass(GoodsQueryActivity.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //��ʾɨ�赽������
                    UtilTc.showLog(bundle.getString("result"));
                    et_wpCode.setText(bundle.getString("result"));
                }
                break;
        }
    }

    Runnable goodsRun = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/getGoods/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("limit","10"));
            if (whatCheck.equals("1")) {
                params.add(new BasicNameValuePair("condition", et_wpCode.getText().toString()));
                params.add(new BasicNameValuePair("type", whatCheck));
                UtilTc.showLog("qy��ѯ" + whatCheck + "-" + et_wpCode.getText().toString());
            } else if (whatCheck.equals("2")) {
                params.add(new BasicNameValuePair("condition", et_wpName.getText().toString()));
                params.add(new BasicNameValuePair("type", whatCheck));
                UtilTc.showLog("qy��ѯ" + whatCheck + "-" + et_wpName.getText().toString().trim());
            }
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�" + strResult);
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if (code.trim().equals("0")) {
                        jbResult = person.getJSONObject("data");
                        //showResult(jbResult);
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case Values.SUCCESS_FORRESULR:
                    showResult(jbResult);
                    break;
                case Values.ERROR_CONNECT:
                    tv_hint.setText("����ʧ��,��������ͷ�����");
                    break;
                case Values.ERROR_OTHER:
                    tv_hint.setText(errorMessage);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showResult(JSONObject jb) {
        try {
            wpReList.clear();
            //String result=jb.getString("result");
            tv_hint.setText("�ѷ��ز�ѯ���");
            JSONArray jt = jb.getJSONArray("goods");
            for (int i = 0; i < jt.length(); i++) {
                JSONObject qyj = jt.getJSONObject(i);
                WpBean qy = new WpBean();
                qy.setWpreCode(qyj.getString("wptm"));
                qy.setWpreName(qyj.getString("wpmc"));
                qy.setWpreSccj(qyj.getString("sccj"));
                qy.setWpreWplb(qyj.getString("wplb"));
                qy.setWpreScrq(qyj.getString("scrq"));
                qy.setWpreSpgb(qyj.getString("spgb"));
                wpReList.add(qy);
            }
            //չʾ����
            currentItem = 1;
            maxItem = wpReList.size();
            UtilTc.showLog("5" + maxItem);
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //��ҳ����
    private void showPageValue(WpBean wpre) {
        tv_wpreName.setText(wpre.getWpreName());
        tv_wpreCode.setText(wpre.getWpreCode());
        tv_wpreSccj.setText(wpre.getWpreSccj());
        tv_wpreLb.setText(wpre.getWpreWplb());
        tv_wpreDate.setText(wpre.getWpreScrq());
        tv_wpreSpgb.setText(wpre.getWpreSpgb());
    }

    //last click
    private void btnQyLast() {
        if (currentItem == 1) {
            tv_hint.setText("�Ѿ��ǵ�һҳ");
        } else if (currentItem - 1 >= 1) {
            currentItem = currentItem - 1;
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(currentItem - 1));
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
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(currentItem - 1));
        }
    }
}
