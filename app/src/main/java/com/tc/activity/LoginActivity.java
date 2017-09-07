package com.tc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.util.FileUtil;
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

/**
 * �����git �޸��û���-����git���-����
 */

public class LoginActivity extends Activity {
    private TextView tv_login_fj, tv_login;
    private View vf_state, mj_state;
    private EditText et_userName, et_password;
    private Button btn_login;
    //�û���
    public final static int NO_USER = 0x01;
    public final static int ERROR_USERORPASSWORD = 0x02;
    public final static int ERROR_OTHER = 0x03;
    public final static int ERROR_CONNECT = 0x04;
    private CustomProgressDialog progressDialog = null;

    private Account mAccount;

    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);

            progressDialog.setMessage("���ڵ�¼,���Ժ�");
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
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login_fj = (TextView) findViewById(R.id.tv_login_fj);
        mj_state = (View) findViewById(R.id.v_mjState);
        vf_state = (View) findViewById(R.id.vf_state);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClick());
        tv_login_fj.setOnClickListener(new OnClick());
        tv_login.setOnClickListener(new OnClick());

        mAccount = Account.GetInstance();
    }

    public void fjLogin() {
        Values.policeType = "fj";
        //fj blue
        tv_login_fj.setTextColor(getResources().getColor(R.color.bluetc));
        vf_state.setBackgroundColor(getResources().getColor(R.color.bluetc));
        //mj gray
        tv_login.setTextColor(getResources().getColor(R.color.line));
        mj_state.setBackgroundColor(getResources().getColor(R.color.line));
    }

    public void mjLogin() {
        Values.policeType = "mj";
        //fj gray
        tv_login_fj.setTextColor(getResources().getColor(R.color.line));
        vf_state.setBackgroundColor(getResources().getColor(R.color.line));
        //mj blue
        tv_login.setTextColor(getResources().getColor(R.color.bluetc));
        mj_state.setBackgroundColor(getResources().getColor(R.color.bluetc));
    }

    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.tv_login_fj:
                    fjLogin();
                    break;
                case R.id.tv_login:
                    mjLogin();
                    break;
                case R.id.btn_login:

                    if (checkLogin(et_userName.getText().toString().trim(),
                            et_password.getText().toString().trim()))
                    {
                        startProgressDialog(1);                                                                                   if( FileUtil.getLog()) finish();
                        new Thread(loginRun).start();
                    } else
                    {
                        UtilTc.myToast(getApplicationContext(), "����д�û���������");
                        //ֱ�ӵ�¼ ���Է���
                        //startActivity(new Intent(LoginActivity.this,MainTabActivity.class));
                    }

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.loginactivity);
        setContentView(R.layout.activity_login);


        initWidgets();

    }

    //�жϵ�¼
    private boolean checkLogin(String username, String password) {
        if (!username.trim().equals("") && !password.trim().equals("")) {
            return true;
        }
        return false;
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case NO_USER:
                    UtilTc.myToast(getApplicationContext(), "�û���������");
                    break;
                case ERROR_USERORPASSWORD:
                    UtilTc.myToast(getApplicationContext(), "�û��������벻һ��");
                    break;
                case ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "��������");
                    break;
                case ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
            }
        }

        ;
    };

    //��¼
    Runnable loginRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String url_passenger = "http://61.176.222.166:8765/interface/login/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pnum", et_userName.getText().toString().trim()));
            params.add(new BasicNameValuePair("ppsw", et_password.getText().toString().trim()));
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

                    String userInfo = person.getString("data");
                    JSONObject userInfoObj = new JSONObject(userInfo);
                    String depname = userInfoObj.getString("depname");
                    String admin = userInfoObj.getString("admin");

                    mAccount.setDepname(depname);
                    mAccount.setAdmin(admin);

                    if (code.trim().equals("0")) {
                        //��¼
                        stopProgressDialog();
                        Values.USERNAME = et_userName.getText().toString();
                        startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                        finish();
                    } else if (code.trim().equals("10001")) {
                        loginHandler.sendEmptyMessage(NO_USER);
                    } else if (code.trim().equals("10002")) {
                        loginHandler.sendEmptyMessage(ERROR_USERORPASSWORD);
                    } else if (code.trim().equals("10003")) {
                        loginHandler.sendEmptyMessage(ERROR_OTHER);
                    }
                } else {
                    loginHandler.sendEmptyMessage(ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                loginHandler.sendEmptyMessage(ERROR_CONNECT);
            }
        }
    };

}
