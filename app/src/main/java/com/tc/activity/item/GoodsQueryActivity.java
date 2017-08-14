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
    //查询条件显示栏
    private EditText et_codeCamera, et_wpCode, et_wpName;
    //扫码
    private final static int SCANNIN_GREQUEST_CODE = 1;
    //返回数据
    private TextView tv_wpreName, tv_wpreCode, tv_wpreSccj, tv_wpreLb, tv_wpreDate, tv_wpreSpgb;
    private JSONObject jbResult;
    List<WpBean> wpReList = new ArrayList<WpBean>();

    //多条处理
    private Button btn_wpLast, btn_wpNext;
    private TextView tv_wpreCount;
    private int currentItem = 1;
    private int maxItem;
    private CustomProgressDialog progressDialog = null;
    private TextView tv_hint;

    //进度条
    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在查询,请稍后");
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
        //查询项
        btn_wpCamera = (Button) findViewById(R.id.btn_wpCamera);
        btn_wpCode = (Button) findViewById(R.id.btn_wpCode);
        btn_wpName = (Button) findViewById(R.id.btn_wpName);
        btn_wpCamera.setOnClickListener(new OnClick());
        btn_wpCode.setOnClickListener(new OnClick());
        btn_wpName.setOnClickListener(new OnClick());
        //查询条件
        et_codeCamera = (EditText) findViewById(R.id.et_codeCamera);
        et_wpCode = (EditText) findViewById(R.id.et_wpCode);
        et_wpName = (EditText) findViewById(R.id.et_wpName);
        //返回信息
        tv_wpreName = (TextView) findViewById(R.id.tv_wpreName);
        tv_wpreCode = (TextView) findViewById(R.id.tv_wpreCode);
        tv_wpreSccj = (TextView) findViewById(R.id.tv_wpreSccj);
        tv_wpreLb = (TextView) findViewById(R.id.tv_wpreLb);
        tv_wpreDate = (TextView) findViewById(R.id.tv_wpreDate);
        tv_wpreSpgb = (TextView) findViewById(R.id.tv_wpreSpgb);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        //多条信息处理
        tv_wpreCount = (TextView) findViewById(R.id.tv_wpreCount);
        btn_wpLast = (Button) findViewById(R.id.btn_wpreLast);
        btn_wpNext = (Button) findViewById(R.id.btn_wpreNext);
        btn_wpLast.setOnClickListener(new OnClick());
        btn_wpNext.setOnClickListener(new OnClick());

    }

    private void clearInfo() {
        //清除信息
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
                //数据处理
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
    //物品名称查询
    private void goodNameCheck(){
        whatCheck="2";
        clearInfo();
        String goodsName=et_wpName.getText().toString().trim();
        if(UtilTc.checkConditionOk(goodsName)){
            //go
            startProgressDialog();
            new Thread(goodsRun).start();
        }else{
            tv_hint.setText("请填写物品名称");
        }

    }
    //物品条码查询
    private void goodCodeCheck(){
        whatCheck="1";
        clearInfo();
        String goodCode=et_wpCode.getText().toString().trim();
        if(UtilTc.checkConditionOk(goodCode)){
            startProgressDialog();
            new Thread(goodsRun).start();
        }else{
            tv_hint.setText("请填写物品条码");
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
                    //显示扫描到的内容
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
                UtilTc.showLog("qy查询" + whatCheck + "-" + et_wpCode.getText().toString());
            } else if (whatCheck.equals("2")) {
                params.add(new BasicNameValuePair("condition", et_wpName.getText().toString()));
                params.add(new BasicNameValuePair("type", whatCheck));
                UtilTc.showLog("qy查询" + whatCheck + "-" + et_wpName.getText().toString().trim());
            }
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "传回来的值是：" + strResult);
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
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
                    tv_hint.setText("连接失败,请检查网络和服务器");
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
            tv_hint.setText("已返回查询结果");
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
            //展示数据
            currentItem = 1;
            maxItem = wpReList.size();
            UtilTc.showLog("5" + maxItem);
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //当页数据
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
            tv_hint.setText("已经是第一页");
        } else if (currentItem - 1 >= 1) {
            currentItem = currentItem - 1;
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(currentItem - 1));
        } else {
            tv_hint.setText("没有更多的展示数据");
        }
    }

    //next click
    private void btnQyNext() {
        if (currentItem == maxItem) {
            tv_hint.setText("已经是最后一页");
        } else if (currentItem + 1 <= maxItem) {
            currentItem += 1;
            tv_wpreCount.setText(currentItem + "/" + maxItem);
            showPageValue(wpReList.get(currentItem - 1));
        }
    }
}
