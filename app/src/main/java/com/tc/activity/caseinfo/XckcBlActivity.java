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
    //�ɼ���
    private EditText et_kyAjbh, et_kyKydw, et_kyZpbgdw, et_kyZpsj, et_kySy, et_kyBeginTime, et_kyEndTime, et_kyXcdd,
            et_kyXcbhqk, et_kyWd, et_kySd, et_kyFx, et_kyZhr, et_kyZhrDw, et_kyZhrZw, et_kyKyqk, et_kyHzt, et_kyZx,
            et_kyLx, et_kyLy, et_kyBlr, et_kyZtr, et_kyZxr, et_kyLxr, et_kyLyr, et_kyTime;
    private RadioGroup rg_kyTq, rg_kyXctj, rg_kyGx;
    private ImageView btn_kcblReturn;
    private String name="";
    private void initWidgets() {
        //�ɼ����ֲ���
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
        //ѡ�񲿷�
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
                    startDateChooseDialog.setDateDialogTitle("��ʼʱ��");
                    startDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyEndTime:
                    DateWheelDialogN endDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyEndTime.setText(time);

                        }
                    });
                    endDateChooseDialog.setDateDialogTitle("����ʱ��");
                    endDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyTime:
                    DateWheelDialogN kyDateChooseDialog = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyTime.setText(time);

                        }
                    });
                    kyDateChooseDialog.setDateDialogTitle("����ʱ��");
                    kyDateChooseDialog.showDateChooseDialog();
                    break;
                case R.id.et_kyZpsj:
                    DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(XckcBlActivity.this, new DateWheelDialogN.DateChooseInterface() {
                        @Override
                        public void getDateTime(String time, boolean longTimeChecked) {
                            et_kyTime.setText(time);

                        }
                    });
                    kyDateChooseDialog3.setDateDialogTitle("ָ��ʱ��");
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
    //�ϴ���ť
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
            params.add(new BasicNameValuePair("XCKYDW","���Ե�λ"));
            params.add(new BasicNameValuePair("ZPBGDW","���浥λ"));
            params.add(new BasicNameValuePair("ZPSJ","2017-09-09 09:30"));
            params.add(new BasicNameValuePair("KYSY","����"));
            params.add(new BasicNameValuePair("XCKYKSSJ","2017-09-09 09:30"));
            params.add(new BasicNameValuePair("XCKYJSSJ","2017-09-10 09:30"));
            params.add(new BasicNameValuePair("BLR","��¼��"));
            params.add(new BasicNameValuePair("KYSJ","2017-09-09 09:30"));
            try{
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code"+httpResponse.getStatusLine().getStatusCode());
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String strResult= EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�"+strResult);
                    if(strResult==null||strResult.equals("")){
                  //      mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code=person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
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
