package com.tc.activity.caseinfo;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.sdses.tool.UtilTc;
import com.tc.application.R;
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
import java.util.ArrayList;
import java.util.List;
public class XckcBlActivity extends Activity {
    //采集项
    private EditText et_kyAjbh, et_kyKydw, et_kyZpbgdw, et_kyZpsj, et_kySy, et_kyBeginTime, et_kyEndTime, et_kyXcdd,
            et_kyXcbhqk, et_kyWd, et_kySd, et_kyFx, et_kyZhr, et_kyZhrDw, et_kyZhrZw, et_kyKyqk, et_kyHzt, et_kyZx,
            et_kyLx, et_kyLy, et_kyBlr, et_kyZtr, et_kyZxr, et_kyLxr, et_kyLyr, et_kyTime;
    private RadioGroup rg_kyTq, rg_kyXctj, rg_kyGx;
    private ImageView btn_kcblReturn;
    private String name="";
    private void initWidgets() {
        //采集文字部分
        et_kyAjbh = (EditText) findViewById(R.id.et_kyAjbh);
        et_kyKydw = (EditText) findViewById(R.id.et_kyKydw);
        et_kyZpbgdw = (EditText) findViewById(R.id.et_kyZpbgdw);
        et_kyZpsj = (EditText) findViewById(R.id.et_kyZpsj);
        et_kySy = (EditText) findViewById(R.id.et_kySy);
        et_kyBeginTime = (EditText) findViewById(R.id.et_kyBeginTime);
        et_kyEndTime = (EditText) findViewById(R.id.et_kyEndTime);
        et_kyXcdd = (EditText) findViewById(R.id.et_kyXcdd);
        et_kyXcbhqk = (EditText) findViewById(R.id.et_kyXcbhqk);
        et_kyWd = (EditText) findViewById(R.id.et_kyWd);
        et_kySd = (EditText) findViewById(R.id.et_kySd);
        et_kyFx = (EditText) findViewById(R.id.et_kyFx);
        et_kyZhr = (EditText) findViewById(R.id.et_kyZhr);
        et_kyZhrDw = (EditText) findViewById(R.id.et_kyZhrDw);
        et_kyZhrZw = (EditText) findViewById(R.id.et_kyZhrZw);
        et_kyZx = (EditText) findViewById(R.id.et_kyZx);
        et_kyKyqk = (EditText) findViewById(R.id.et_kyKyqk);
        et_kyHzt = (EditText) findViewById(R.id.et_kyHzt);
        et_kyLx = (EditText) findViewById(R.id.et_kyLx);
        et_kyLy = (EditText) findViewById(R.id.et_kyLy);
        et_kyBlr = (EditText) findViewById(R.id.et_kyBlr);
        et_kyZtr = (EditText) findViewById(R.id.et_kyZtr);
        et_kyZxr = (EditText) findViewById(R.id.et_kyZxr);
        et_kyLxr = (EditText) findViewById(R.id.et_kyLxr);
        et_kyLyr = (EditText) findViewById(R.id.et_kyLyr);
        et_kyTime = (EditText) findViewById(R.id.et_kyTime);
        //选择部分
        rg_kyTq = (RadioGroup) findViewById(R.id.rg_kyTq);
        rg_kyXctj = (RadioGroup) findViewById(R.id.rg_kyXctj);
        rg_kyGx = (RadioGroup) findViewById(R.id.rg_kyGx);
        et_kyBeginTime.setOnClickListener(new Onclick());
        et_kyEndTime.setOnClickListener(new Onclick());
        et_kyTime.setOnClickListener(new Onclick());
        btn_kcblReturn=(ImageView)findViewById(R.id.btn_kcblReturn);
        btn_kcblReturn.setOnClickListener(new Onclick());
        name=getIntent().getStringExtra("name");
        et_kyAjbh.setText(name);
        et_kyZpsj.setOnClickListener(new Onclick());
    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.et_kyBeginTime:
                    UtilTc.showLog("et_kyBeginTime");
                    DateWheelDialogN startDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyBeginTime.setText(time);

                        }
                    });
                    startDateChooseDialog.setDateDialogTitle("开始时间");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyEndTime:
                    DateWheelDialogN endDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyEndTime.setText(time);

                        }
                    });
                    endDateChooseDialog.setDateDialogTitle("结束时间");
                    endDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyTime:
                    DateWheelDialogN kyDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyTime.setText(time);

                        }
                    });
                    kyDateChooseDialog.setDateDialogTitle("勘验时间");
                    kyDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyZpsj:
                    DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyTime.setText(time);

                        }
                    });
                    kyDateChooseDialog3.setDateDialogTitle("指派时间");
                    kyDateChooseDialog3.showDateChooseDialog();
                    break;
                case R.id.btn_kcblReturn:
                    finish();
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_xckcbl);
        initWidgets();
    }
    //上传按钮
    public void BtnUploadBL(View view) {
       // new Thread(uploadRun).start();
    }

    Runnable uploadRun=new Runnable(){
        @Override
        public void run() {
            String url_passenger ="http://61.176.222.166:8765/interface/xs/ADD_ZF_XS_XCKYBL.asp";
            HttpPost httpRequest =new HttpPost(url_passenger);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID","111"));
            params.add(new BasicNameValuePair("XCKYDW","测试单位"));
            params.add(new BasicNameValuePair("ZPBGDW","报告单位"));
            params.add(new BasicNameValuePair("ZPSJ","2017-09-09 09:30"));
            params.add(new BasicNameValuePair("KYSY","事由"));
            params.add(new BasicNameValuePair("XCKYKSSJ","2017-09-09 09:30"));
            params.add(new BasicNameValuePair("XCKYJSSJ","2017-09-10 09:30"));
            params.add(new BasicNameValuePair("BLR","笔录人"));
            params.add(new BasicNameValuePair("KYSJ","2017-09-09 09:30"));
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
                  //      mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
                    if(code.trim().equals("0")){
                    //    jsResult=person.getJSONObject("data");
                    //    mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    }else if(code.trim().equals("10003")){
                     //   JSONObject jb = person.getJSONObject("data");
                     //   errorMessage = jb.getString("message");
                   //     mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                }else{
                 //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            }catch(Exception e){
                e.printStackTrace();
              //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };
}
