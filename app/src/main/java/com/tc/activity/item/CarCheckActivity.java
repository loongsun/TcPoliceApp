package com.tc.activity.item;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.LoginActivity;
import com.tc.activity.MainTabActivity;
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
import java.util.Locale;

public class CarCheckActivity extends Activity {
    private ImageView iv_carReturn;
    private Spinner m_spinAreaShort;
    private Spinner m_spinChatShort;
    private EditText m_edtCarNum;
    private Button m_btnQueryByCarNum;
    private EditText m_edtEngineNum;
    private Button m_btnQueryByEngineNum;
    private EditText m_edtFrameNum;
    private Button m_btnQueryByFrameNum;
    private String m_strCarNum = "";
    private CustomProgressDialog progressDialog = null;
    private final static int CARNUM=1;
    private RadioGroup rg_car;
    private RadioButton rb_blue,rb_yellow;
    private String carType="";
    private String errorMessage = "";
    //返回结果
    private TextView tv_reCph,tv_reCpzl,tv_reCsys,tv_reFdjh,tv_reCjhm,tv_carResult;
    JSONObject jbResult;
    private String whatCheck="";
    //信息提示
    private TextView tv_hint;
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case CARNUM:
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
    private void initWidgets() {
        iv_carReturn = (ImageView) findViewById(R.id.btn_carReturn);
        iv_carReturn.setOnClickListener(new OnClick());
        m_edtCarNum = (EditText) findViewById(R.id.EditTextCarNum);
        m_btnQueryByCarNum = (Button) findViewById(R.id.carinfo_btnquerycarnum);
        m_btnQueryByCarNum.setOnClickListener(new OnClick());
        m_edtEngineNum = (EditText) findViewById(R.id.et_fdjh);
        m_btnQueryByEngineNum = (Button) findViewById(R.id.carinfo_btnqueryenginenum);
        m_btnQueryByEngineNum.setOnClickListener(new OnClick());
        m_edtFrameNum = (EditText) findViewById(R.id.et_cjhm);
        m_btnQueryByFrameNum = (Button) findViewById(R.id.carinfo_btnqueryframenum);
        m_btnQueryByFrameNum.setOnClickListener(new OnClick());
        rg_car=(RadioGroup)findViewById(R.id.rg_car);
        rb_blue=(RadioButton)findViewById(R.id.rb_carblue);
        rb_yellow=(RadioButton)findViewById(R.id.rb_caryellow);
        rb_yellow.setOnClickListener(new OnClick());
        rb_blue.setOnClickListener(new OnClick());
        UtilTc.showLog("car id"+rg_car.getCheckedRadioButtonId());
        //返回结果
        tv_reCph=(TextView)findViewById(R.id.tv_carResultNum);
        tv_reCpzl=(TextView)findViewById(R.id.tv_carResultZL);
        tv_reCsys=(TextView)findViewById(R.id.tv_carColor);
        tv_reFdjh=(TextView)findViewById(R.id.tv_fdjh);
        tv_reCjhm=(TextView)findViewById(R.id.tv_cjhm);
        tv_carResult=(TextView)findViewById(R.id.tv_carResult);
        tv_hint=(TextView)findViewById(R.id.tv_hint);
    }
    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_carReturn:
                    finish();
                    break;
                case R.id.carinfo_btnquerycarnum://车牌号
                    whatCheck="1";
                    clearInfo();
                    if(carNumCheck()){
                        startProgressDialog(CARNUM);
                        new Thread(carRun).start();
                    }
                    break;
                case R.id.carinfo_btnqueryenginenum://发动机号
                    whatCheck="2";
                    clearInfo();
                    if(fdjhCheck()){
                        startProgressDialog(CARNUM);
                        new Thread(carRun).start();
                    }
                    break;
                case R.id.carinfo_btnqueryframenum://车架号码
                    whatCheck="3";
                    clearInfo();
                    if(cjhmCheck()){
                        startProgressDialog(CARNUM);
                        new Thread(carRun).start();
                    }
                    break;
            }
        }
    }
    private boolean carNumCheck(){
        if (!getCarNumAndType()) {
            return false;
        }
        if(rg_car.getCheckedRadioButtonId()==-1|| getRbValue().equals("")){
            tv_hint.setText("请选择车牌类型");
            return false;
        }
        return true;
    }
    private boolean fdjhCheck(){
        String checkfdjh=m_edtEngineNum.getText().toString().trim();
        if(rg_car.getCheckedRadioButtonId()==-1|| getRbValue().equals("")){
            tv_hint.setText("请选择车牌类型");
            return false;
        }
        if(checkfdjh!=null&&!checkfdjh.equals("")){
            return true;
        }else{
            tv_hint.setText("请填写发动机号");
        }
            return false;
    }
    private boolean cjhmCheck(){
        String cjhm=m_edtFrameNum.getText().toString().trim();
        if(rg_car.getCheckedRadioButtonId()==-1|| getRbValue().equals("")){
            tv_hint.setText("请选择车牌类型");
            return false;
        }
        if(cjhm!=null&&!cjhm.equals("")){
            return  true;
        }else{
            tv_hint.setText("请填写车架号码");
        }
        return false;
    }
    Runnable carRun=new Runnable() {
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/carcheck/";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hpzl",getRbValue()));
            if(whatCheck.equals("1")){
                params.add(new BasicNameValuePair("condition",m_strCarNum));
                params.add(new BasicNameValuePair("type",whatCheck));
                UtilTc.showLog("车牌号码查询"+whatCheck);
            }else if(whatCheck.equals("2")){
                params.add(new BasicNameValuePair("condition",m_edtEngineNum.getText().toString().trim()));
                params.add(new BasicNameValuePair("type",whatCheck));
                UtilTc.showLog("发动机号查询"+whatCheck);

            }else if(whatCheck.equals("3")){
                params.add(new BasicNameValuePair("condition",m_edtFrameNum.getText().toString().trim()));
                params.add(new BasicNameValuePair("type",whatCheck));
                UtilTc.showLog("车架号码"+whatCheck);
            }

            try{
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code"+httpResponse.getStatusLine().getStatusCode());
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String strResult= EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "传回来的值是："+strResult);
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                     //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
                    if(code.trim().equals("0")){
                        jbResult = person.getJSONObject("data");
                       // showResult(jb);
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

    private void clearInfo(){
        tv_reCph.setText("");
        tv_reCpzl.setText("");
        tv_reCsys.setText("");
        tv_reFdjh.setText("");
        tv_reCjhm.setText("");
    }
    private  void showResult(JSONObject jb){
        try{
            String result=jb.getString("result");
            tv_hint.setText(""+jb.getString("result"));
            JSONObject jt = jb.getJSONObject("car");
            tv_reCph.setText(""+jt.get("hphm"));
            tv_reCpzl.setText(""+jt.get("hpzl"));
            tv_reCsys.setText(""+jt.get("csys"));
            tv_reFdjh.setText(""+jt.get("fdjh"));
            tv_reCjhm.setText(""+jt.get("cjhm"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carcheck);
        initWidgets();
        clearInfo();
        initAreaShortSpinner();
        initChatShortSpinner();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAreaShortSpinner() {
        m_spinAreaShort = (Spinner) findViewById(R.id.spinnerareashot);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Area_Array, R.layout.jerry_spinner);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        m_spinAreaShort.setAdapter(adapter);
    }

    private void initChatShortSpinner() {
        m_spinChatShort = (Spinner) findViewById(R.id.spinnerchatshort);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.ShortArea_Array, R.layout.jerry_spinner);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        m_spinChatShort.setAdapter(adapter);
    }

    private boolean getCarNumAndType() {
        if (m_edtCarNum.getText() == null) {
            tv_hint.setText("请输入车牌号码");
            return false;
        }
        String strCarNumTem = UtilTc.filterForObject(m_edtCarNum.getText().toString());
        if (strCarNumTem.isEmpty()) {
            tv_hint.setText("请输入车牌号码");
            return false;
        }

        strCarNumTem = strCarNumTem.toUpperCase(Locale.getDefault());
        if (strCarNumTem.length() < 5) {
            tv_hint.setText("车牌号码长度有误!请重新输入");
            return false;
        }
        String strAreaTem = m_spinAreaShort.getSelectedItem().toString();
        String strShortAreaTem = m_spinChatShort.getSelectedItem().toString();
        m_strCarNum = strAreaTem + strShortAreaTem + strCarNumTem;
        return true;
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what){
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

    //获取rg值
    private String  getRbValue() {
        String  result="";
        for (int i = 0; i < rg_car.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rg_car.getChildAt(i);
            if (rb.isChecked()) {
                result=""+rb.getText();
            }
        }
        return result;
    }
}
