package com.tc.activity.item;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialog;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class HandQueryActivity extends Activity {
    private Button btn_faceCheck;
    private ImageView btn_handqueryReturn;
    //查询
    private Button btn_id2Check, btn_hzCheck,btn_moreChck;
    private CustomProgressDialog progressDialog = null;
    private final static int QUERY=1;
    private EditText et_id2Num,et_hzNum;
    private  String numValue="";
    private String  errorMessage="";
    //返回结果
    private TextView tv_repResult,tv_repName,tv_repSex,tv_repBir,tv_repAddress,tv_repRylx,tv_repLxdw,
    tv_repLxr,tv_repLxFs;
    private JSONObject jsResult;
    private TextView tv_hint;

    //组合查询条件
    private EditText et_handName,et_handBir,et_handDqgh;
    private RadioGroup rg_sex;
    private RadioButton rb_man,rb_women;

    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case QUERY:
                    progressDialog.setMessage("正在查询,请稍后");
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
    private void initWidgets(){
        btn_faceCheck = (Button) findViewById(R.id.btn_faceCheck);
        btn_handqueryReturn = (ImageView) findViewById(R.id.btn_handqueryReturn);
        btn_handqueryReturn.setOnClickListener(new OnClick());
        btn_faceCheck.setOnClickListener(new OnClick());
        btn_moreChck=(Button)findViewById(R.id.btn_moreChck);
        btn_moreChck.setOnClickListener(new OnClick());
        //check
        btn_id2Check = (Button) findViewById(R.id.btn_handId2);
        btn_id2Check.setOnClickListener(new OnClick());
        btn_hzCheck = (Button) findViewById(R.id.btn_handHz);
        btn_hzCheck.setOnClickListener(new OnClick());
        et_id2Num=(EditText)findViewById(R.id.et_id2Num);
        et_hzNum=(EditText)findViewById(R.id.et_hzNum);
        //返回参数
        tv_repName=(TextView)findViewById(R.id.tv_repName);
        tv_repSex=(TextView)findViewById(R.id.tv_repSex);
        tv_repBir=(TextView)findViewById(R.id.tv_repBir);
        tv_repAddress=(TextView)findViewById(R.id.tv_repAddress);
        tv_repRylx=(TextView)findViewById(R.id.tv_repRylx);
        tv_repLxdw=(TextView)findViewById(R.id.tv_repLxDw);
        tv_repLxr=(TextView)findViewById(R.id.tv_repLxr);
        tv_repLxFs=(TextView)findViewById(R.id.tv_repLxfs);
        //组合查询
        et_handName=(EditText)findViewById(R.id.et_handName);
        et_handBir=(EditText)findViewById(R.id.et_handleBir);
        et_handBir.setOnClickListener(new OnClick());
        et_handDqgh=(EditText)findViewById(R.id.et_handleDqgh);
        rg_sex=(RadioGroup)findViewById(R.id.rg_handSex);
        rb_man=(RadioButton)findViewById(R.id.rb_handMan);
        rb_women=(RadioButton)findViewById(R.id.rb_handWomen);
        tv_hint=(TextView)findViewById(R.id.tv_hint);


    }
    //清空数据
    private void clearInfo(){
        tv_repName.setText("");
        tv_repSex.setText("");
        tv_repBir.setText("");
        tv_repAddress.setText("");
        tv_repRylx.setText("");
        tv_repLxdw.setText("");
        tv_repLxr.setText("");
        tv_repLxFs.setText("");

    }

    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_handId2:
                    clearInfo();
                    numValue="";
                    numValue=et_id2Num.getText().toString().trim();
                    if(isCheck()){
                        startProgressDialog(QUERY);
                        new Thread(id2Run).start();
                    }else{
                        tv_hint.setText("身份证号码不能为空");
                    }
                    break;
                case R.id.btn_handHz:
                    clearInfo();
                    numValue="";
                    numValue=et_hzNum.getText().toString().trim();
                    if(isCheck()){
                        startProgressDialog(QUERY);
                        new Thread(id2Run).start();
                    }else{
                        tv_hint.setText("护照号码不能为空");
                    }
                    break;
                case R.id.btn_moreChck:
                    //组合查询
                    clearInfo();
                    if(moreConditionCheck()){
                        startProgressDialog(QUERY);
                        new Thread(moreRun).start();
                    }
                    break;
                case R.id.btn_faceCheck:
                   startActivity(new Intent(HandQueryActivity.this,FaceCheckNActivity.class));
                    break;
                case R.id.btn_handqueryReturn:
                    finish();
                    break;
                case R.id.et_handleBir:
                    UtilTc.showLog("onclick for briselect");
                    DateWheelDialog startDateChooseDialog = new DateWheelDialog(HandQueryActivity.this, new DateWheelDialog.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_handBir.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("出生日期");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplehandquery);
        initWidgets();
        clearInfo();
    }
    private boolean isCheck(){
        if(numValue.trim().equals("")){
            return false;
        }
        return true;
    }

    private boolean moreConditionCheck(){
        if(et_handName.getText().toString().trim().equals("")){
            tv_hint.setText("请填写姓名");
            return false;
        }else if(rg_sex.getCheckedRadioButtonId()==-1|| getRbValue().equals("")){
            tv_hint.setText("请选择性别");
            return false;
        }else if(et_handBir.getText().toString().trim().equals("")){
            tv_hint.setText("请选择出生日期");
            return  false;
        }
        return true;
    }

    Runnable id2Run =new Runnable() {
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/idnumcheck/";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idnum",numValue));
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
                        jsResult=person.getJSONObject("data");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                }else{
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };

    Runnable moreRun =new Runnable() {
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/comcheck/";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",et_handName.getText().toString().trim()));
            params.add(new BasicNameValuePair("sex",getRbValue()));
            params.add(new BasicNameValuePair("birthday",et_handBir.getText().toString()));
            params.add(new BasicNameValuePair("province",et_handDqgh.getText().toString()));

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
                        jsResult=person.getJSONObject("data");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                }else{
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };

    android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what){
                case Values.SUCCESS_FORRESULR:
                    showResult(jsResult);
                    break;
                case Values.ERROR_CONNECT:
                    tv_hint.setText("连接失败,请检查网络和服务器");
                    break;
                case Values.ERROR_OTHER:
                    tv_hint.setText(errorMessage);
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    tv_hint.setText("服务器没有返回值");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private  void showResult(JSONObject jb){
        try{
            tv_hint.setText(""+jb.getString("result"));
            JSONObject jt = jb.getJSONObject("person");
            tv_repName.setText(""+jt.get("name"));
            tv_repSex.setText(""+jt.get("Sex"));
            tv_repBir.setText(""+jt.get("birthday"));
            tv_repAddress.setText(""+jt.get("address"));
            tv_repRylx.setText(""+jt.get("rylx"));
            tv_repLxdw.setText(""+jt.get("lxdw"));
            tv_repLxr.setText(""+jt.get("lxr"));
            tv_repLxFs.setText(""+jt.get("lxfs"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //获取rg值
    private String  getRbValue() {
        String  result="";
        for (int i = 0; i < rg_sex.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rg_sex.getChildAt(i);
            if (rb.isChecked()) {
               result=""+rb.getText();
            }
        }
        return result;
    }



}
