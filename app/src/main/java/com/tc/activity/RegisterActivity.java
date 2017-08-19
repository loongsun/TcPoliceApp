package com.tc.activity;

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
import android.widget.Toast;

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
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 杨工测试git 修改用户名-测试git插件-ceshi
 */

public class RegisterActivity extends Activity {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.btn_policestateReturn)
    ImageView btnPolicestateReturn;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.v_mjState)
    View vMjState;
    @BindView(R.id.tv_login_fj)
    TextView tvLoginFj;
    @BindView(R.id.vf_state)
    View vfState;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.v_user)
    View vUser;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.v_password)
    View vPassword;
    @BindView(R.id.iv_password1)
    ImageView ivPassword1;
    @BindView(R.id.et_password1)
    EditText etPassword1;
    @BindView(R.id.v_password1)
    View vPassword1;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private TextView tv_login_fj, tv_login;
    private View vf_state, mj_state;
    private EditText et_userName, et_password, et_password1;
    private Button btn_login;
    //用户名
    public final static int NO_USER = 0x01;
    public final static int ERROR_USERORPASSWORD = 0x02;
    public final static int ERROR_OTHER = 0x03;
    public final static int ERROR_CONNECT = 0x04;
    private CustomProgressDialog progressDialog = null;


    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);

            progressDialog.setMessage("正在注册,请稍后");
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
        et_password1 = (EditText) findViewById(R.id.et_password1);
        btn_login.setOnClickListener(new OnClick());
        tv_login_fj.setOnClickListener(new OnClick());
        tv_login.setOnClickListener(new OnClick());
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

    @butterknife.OnClick(R.id.btn_policestateReturn)
    public void onViewClicked() {
        finish();
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
                            et_password.getText().toString().trim())) {
                        if (et_password.getText().toString().trim().equals(et_password1.getText().toString().trim())) {
                            startProgressDialog(1);
                            new Thread(loginRun).start();
                        } else {
                            Toast.makeText(RegisterActivity.this, "两次密码不一致请确认", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UtilTc.myToast(getApplicationContext(), "请填写用户名或密码");
                        //直接登录 测试方便
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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        initWidgets();

    }

    //判断登录
    private boolean checkLogin(String username, String password) {
        if (!username.trim().equals("") && !password.trim().equals("")) {
            return true;
        }
        return false;
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case NO_USER:
                    UtilTc.myToast(getApplicationContext(), "用户名不存在");
                    break;
                case ERROR_USERORPASSWORD:
                    UtilTc.myToast(getApplicationContext(), "用户名与密码不一致");
                    break;
                case ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "其它错误");
                    break;
                case ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
            }
        }

        ;
    };
    //	添加用户（注册）
//	http://61.176.222.166:8765/interface/addUser/?pnum=user001&ppsw=000&ptype=0&pdepname=终端添加用户
//
    //添加用户
    Runnable loginRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String url_passenger = "http://61.176.222.166:8765/interface/addUser/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pnum", et_userName.getText().toString().trim()));
            params.add(new BasicNameValuePair("ppsw", et_password.getText().toString().trim()));
            params.add(new BasicNameValuePair("ptype", "0"));
            params.add(new BasicNameValuePair("pdepname", "终端添加用户"));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("添加用户返回值", "传回来的值是：" + strResult);
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //登录
                        stopProgressDialog();
                        Values.USERNAME = et_userName.getText().toString();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
