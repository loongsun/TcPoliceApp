package com.tc.activity.item;

import com.baidu.mapapi.map.Text;
import com.sdses.bean.QyBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.view.CustomProgressDialog;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class QyQueryActivity extends Activity {
	private ImageView btn_qyReturn;
	private Button btn_qyName,btn_qyFr,btn_qyCode;
	private EditText et_qyName,et_qyFr,et_qyCode;
	private TextView tv_hint;
	private String whatCheck="";
	private String errorMessage = "";
	//返回信息
	JSONObject jbResult;
	private TextView tv_qyreName,tv_qyreLx,tv_qyreFr,tv_qyreZczb,tv_qyreDate,tv_qyreAddress,tv_qyrePhone;
	private CustomProgressDialog progressDialog = null;
	private final static int QY=1;
	//返回列表
	List<QyBean> qyReList=new ArrayList<QyBean>();
	//多条处理
	private Button btn_qyLast,btn_qyNext;
	private TextView tv_qyreCount;
	private int currentItem=1;
    private int maxItem;
	private void startProgressDialog(int type) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			switch (type) {
				case QY:
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
		//查询条件
		et_qyName=(EditText)findViewById(R.id.et_qyName);
		et_qyFr=(EditText)findViewById(R.id.et_qyFr);
		et_qyCode=(EditText)findViewById(R.id.et_qyCode);
		tv_hint=(TextView)findViewById(R.id.tv_hint);
		btn_qyReturn=(ImageView)findViewById(R.id.btn_qyReturn);
		btn_qyReturn.setOnClickListener(new OnClick());
		//查询按钮
		btn_qyName=(Button)findViewById(R.id.btn_qyName);
		btn_qyFr=(Button)findViewById(R.id.btn_qyFr);
		btn_qyCode=(Button)findViewById(R.id.btn_qyCode);
		btn_qyName.setOnClickListener(new OnClick());
		btn_qyFr.setOnClickListener(new OnClick());
		btn_qyCode.setOnClickListener(new OnClick());
		//返回信息
		tv_qyreName=(TextView)findViewById(R.id.tv_qyreName);
		tv_qyreLx=(TextView)findViewById(R.id.tv_qyreLx);
		tv_qyreFr=(TextView)findViewById(R.id.tv_qyreFr);
		tv_qyreZczb=(TextView)findViewById(R.id.tv_qyreZczb);
		tv_qyreDate=(TextView)findViewById(R.id.tv_qyreDate);
		tv_qyreAddress=(TextView)findViewById(R.id.tv_qyreAddress);
		tv_qyrePhone=(TextView)findViewById(R.id.tv_qyrePhone);
		//多条信息处理
		tv_qyreCount=(TextView)findViewById(R.id.tv_qyreCount);
		btn_qyLast=(Button)findViewById(R.id.btn_qyreLast);
		btn_qyNext=(Button)findViewById(R.id.btn_qyreNext);
		btn_qyLast.setOnClickListener(new OnClick());
		btn_qyNext.setOnClickListener(new OnClick());

	}
	//清除信息
	private void clearInfo(){
		currentItem=1;
		maxItem=1;
		tv_qyreCount.setText("1/1");
		tv_hint.setText("");
		tv_qyreName.setText("");
		tv_qyreLx.setText("");
		tv_qyreFr.setText("");
		tv_qyreZczb.setText("");
		tv_qyreDate.setText("");
		tv_qyreAddress.setText("");
		tv_qyrePhone.setText("");
	}

	
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_qyReturn:
				finish();
			 break;
				case R.id.btn_qyName://企业名称查询
					clearInfo();
					whatCheck="1";
					qyNameCheck();
					break;
				case R.id.btn_qyFr://法人查询
					clearInfo();
					whatCheck="2";
					frCheck();

					break;
				case R.id.btn_qyCode://企业编号查询
					clearInfo();
					whatCheck="3";
					qyCodeCheck();
					break;
				case R.id.btn_qyreLast:
					btnQyLast();
					break;
				case R.id.btn_qyreNext:
					btnQyNext();
					break;

			}
		}
	}
	//企业名称
	private void qyNameCheck(){
		String checkName=et_qyName.getText().toString().trim();
		if(checkName!=null&&!checkName.equals("")){
			startProgressDialog(QY);
			new Thread(qyRun).start();
		}else{
			tv_hint.setText("请填写企业名称");
		}
	}
	//法人查询
	private void frCheck(){
		String checkCon=et_qyFr.getText().toString().trim();
		UtilTc.showLog("checkCon  "+checkCon);
		if(checkCon!=null&&!checkCon.equals("")){
			startProgressDialog(QY);
			new Thread(qyRun).start();
		}else{
			tv_hint.setText("请填写法人姓名");
		}
	}
	//企业编码查询
	private void qyCodeCheck(){
		String checkCode=et_qyCode.getText().toString().trim();
		if(checkCode!=null&&!checkCode.equals("")){
			startProgressDialog(QY);
			new Thread(qyRun).start();
		}else{
			tv_hint.setText("请填写企业编码");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_qyquery);
		initWidgets();
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	Runnable qyRun =new Runnable() {
		@Override
		public void run() {
			String url_passenger ="http://61.176.222.166:8765/interface/getCompanyInfo/";
			HttpPost httpRequest =new HttpPost(url_passenger);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("limit","10"));
			if(whatCheck.equals("1")){
				params.add(new BasicNameValuePair("condition",et_qyName.getText().toString().trim()));
				params.add(new BasicNameValuePair("type",whatCheck));
				UtilTc.showLog("qy查询"+whatCheck+"-"+et_qyName.getText().toString().trim());
			}else if(whatCheck.equals("2")){
				params.add(new BasicNameValuePair("condition",et_qyFr.getText().toString().trim()));
				params.add(new BasicNameValuePair("type",whatCheck));
				UtilTc.showLog("qy查询"+whatCheck+"-"+et_qyFr.getText().toString().trim());
			}else if(whatCheck.equals("3")){
				params.add(new BasicNameValuePair("condition",et_qyCode.getText().toString().trim()));
				params.add(new BasicNameValuePair("type","3"));
				UtilTc.showLog("qy查询"+whatCheck+"-"+et_qyCode.getText().toString().trim());
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
						//showResult(jbResult);
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

	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			stopProgressDialog();
			switch (msg.what){
				case Values.SUCCESS_FORRESULR:
					UtilTc.showLog("SUCCESS_FORRESULR qy mHandler");
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


	private  void showResult(JSONObject jb){
		try{
			qyReList.clear();
			//String result=jb.getString("result");
			tv_hint.setText("已返回查询结果");
			UtilTc.showLog("1");
			JSONArray jt = jb.getJSONArray("coms");
			UtilTc.showLog("2");
			for(int i=0;i<jt.length();i++){
				JSONObject qyj=jt.getJSONObject(i);
				UtilTc.showLog("3");
				QyBean qy=new QyBean();
				qy.setQyName(qyj.getString("qymc"));
				qy.setQyLx(qyj.getString("qylx"));
				qy.setQyFr(qyj.getString("qyfr"));
				qy.setQyZczb(qyj.getString("zczb"));
				qy.setQyZcrq(qyj.getString("zcrq"));
				qy.setQyZcdz(qyj.getString("zcdz"));
				qy.setQyLxdh(qyj.getString("LXDH"));
				qyReList.add(qy);
			}
			//展示数据
			UtilTc.showLog("4");
			currentItem=1;
			maxItem=qyReList.size();
			UtilTc.showLog("5"+maxItem);
			tv_qyreCount.setText(currentItem+"/"+maxItem);
			showPageValue(qyReList.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//当页数据
	private void showPageValue(QyBean qyre){
		UtilTc.showLog(qyReList.size()+":"+qyReList.get(0).getQyName());
		tv_qyreName.setText(qyre.getQyName());
		tv_qyreLx.setText(qyre.getQyLx());
		tv_qyreFr.setText(qyre.getQyFr());
		tv_qyreZczb.setText(qyre.getQyZczb());
		tv_qyreDate.setText(qyre.getQyZcrq());
		tv_qyreAddress.setText(qyre.getQyZcdz());
		tv_qyrePhone.setText(qyre.getQyLxdh());
	}

	//last click
	private void btnQyLast(){
		if(currentItem==1){
			tv_hint.setText("已经是第一页");
		}else if(currentItem-1>=1){
			currentItem=currentItem-1;
			tv_qyreCount.setText(currentItem+"/"+maxItem);
			showPageValue(qyReList.get(currentItem-1));
		}else{
			tv_hint.setText("没有更多的展示数据");
		}
	}
	//next click
	private void btnQyNext(){
		if(currentItem==maxItem){
			tv_hint.setText("已经是最后一页");
		}else if(currentItem+1<=maxItem){
			currentItem+=1;
			tv_qyreCount.setText(currentItem+"/"+maxItem);
			showPageValue(qyReList.get(currentItem-1));
		}
	}


}
