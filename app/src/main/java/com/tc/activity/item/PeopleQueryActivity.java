package com.tc.activity.item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.global.Utils;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.OnClientCallback;
import com.kaer.sdk.nfc.NfcReadClient;
import com.kaer.sdk.utils.CardCode;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.PreferData;
import com.tc.application.R;
import com.tc.util.FileUtil;
import com.tc.view.CustomProgressDialog;
import com.zkteco.android.IDReader.IDPhotoHelper;
import com.zkteco.android.IDReader.PowerOperate;
import com.zkteco.android.IDReader.WLTService;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.module.idcard.IDCardReader;
import com.zkteco.android.biometric.module.idcard.IDCardReaderFactory;
import com.zkteco.android.biometric.module.idcard.exception.IDCardReaderException;
import com.zkteco.android.biometric.module.idcard.meta.IDCardInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;


public class PeopleQueryActivity  extends BaseActivity implements OnClientCallback {

	private static final String TAG = PeopleQueryActivity.class.getSimpleName();
	private static final int REQUEST_CODE_CAMERA = 102;
	private boolean overflag=true;
	private Button btn_handquery,btn_NFC;
	private String numValue="";
	private IDCardReader idCardReader = null;
	PowerOperate mPowerOperate = new PowerOperate();
	private boolean mbStop = false;
	private WorkThread workThread = null;
	private CustomProgressDialog progressDialog = null;
	private final static int QUERY=1;
	//���֤��Ϣ
	private TextView tv_nameValue,tv_sexValue,tv_nationalValue,
			tv_birValue,tv_addressValue,tv_idNumValue,tv_issueValue,
			tv_vaildDateValue;
	private TextView tv_hint;
	private ImageView im_head;
	private String  errorMessage="";
	//���ؽ��
	private TextView tv_repResult,tv_repName,tv_repSex,tv_repBir,tv_repAddress,tv_repRylx,tv_repLxdw,
			tv_repLxr,tv_repLxFs,proTv;
	private JSONObject jsResult;
	private Button mAutoQueryBtn;
	private boolean mHasToken;
	private LinearLayout mResultLayout;
	private ImageView mBackImg;

	private void startProgressDialog(int type) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			switch (type) {
				case QUERY:
					progressDialog.setMessage("���ڲ�ѯ,���Ժ�");
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

		proTv=(TextView)findViewById(R.id.tvjd) ;
		proTv.setText("��������δ���ӣ����Ժ�...");
		proBar = (ProgressBar) findViewById(R.id.progressBarNFC);
		proBar.setMax(100);

		mResultLayout = (LinearLayout)findViewById(R.id.layout_result_info);
		btn_handquery=(Button)findViewById(R.id.btn_handQuery);
		btn_handquery.setOnClickListener(new OnClick());
		btn_NFC=(Button)findViewById(R.id.btn_NFCQuery);
		//-----------id2
		tv_nameValue=(TextView)findViewById(R.id.tv_nameValue);
		tv_sexValue=(TextView)findViewById(R.id.tv_sexValue);
		tv_nationalValue=(TextView)findViewById(R.id.tv_nationalValue);
		tv_birValue=(TextView)findViewById(R.id.tv_birValue);
		tv_addressValue=(TextView)findViewById(R.id.tv_addressValue);
		tv_idNumValue=(TextView)findViewById(R.id.tv_idNumValue);
		tv_issueValue=(TextView)findViewById(R.id.tv_issueValue);
		tv_vaildDateValue=(TextView)findViewById(R.id.tv_vaildDateValue);
		im_head=(ImageView)findViewById(R.id.im_head);
		tv_hint=(TextView)findViewById(R.id.tv_hint);
		//���ز���
		tv_repName=(TextView)findViewById(R.id.tv_zrepName);
		tv_repSex=(TextView)findViewById(R.id.tv_zrepSex);
		tv_repBir=(TextView)findViewById(R.id.tv_zrepBir);
		tv_repAddress=(TextView)findViewById(R.id.tv_zrepAddress);
		tv_repRylx=(TextView)findViewById(R.id.tv_zrepRylx);
		tv_repLxdw=(TextView)findViewById(R.id.tv_zrepLxDw);
		tv_repLxr=(TextView)findViewById(R.id.tv_zrepLxr);
		tv_repLxFs=(TextView)findViewById(R.id.tv_zrepLxfs);
		mBackImg = (ImageView)findViewById(R.id.btn_peoplequeryReturn);
		mBackImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		mAutoQueryBtn = (Button) findViewById(R.id.btn_auto_query);
		mAutoQueryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mHasToken){
					Intent intent = new Intent(PeopleQueryActivity.this, CameraActivity.class);
					intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(PeopleQueryActivity
							.this).getAbsolutePath());
					intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
					startActivityForResult(intent,REQUEST_CODE_CAMERA);
				}else{
					Toast.makeText(PeopleQueryActivity.this,"����ִ����֤�����Ե�",Toast.LENGTH_LONG).show();
				}
			}
		});


	}
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.btn_handQuery:
					startActivity(new Intent(PeopleQueryActivity.this,HandQueryActivity.class));
					break;
				default:
					break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peoplecheck);
		initWidgets();
		clearInfo();
//		InitID2();
		initQrCode();
	}

	private NfcReadClient mNfcReadClient;
	private PreferData preferData;
	private ProgressBar proBar;
	private ReadAsync async;
	private NfcAdapter mAdapter;

    public void onNFC(View v)
	{
		if(btn_NFC.getText().equals("����NFC"))
		{
			openNFC();
			btn_NFC.setText("�ر�NFC");
		}
		else
		{
			btn_NFC.setText("����NFC");
			closeNFC();
		}
	}
	private void openNFC()
	{
		mNfcReadClient = NfcReadClient.getInstance();
		if (!mNfcReadClient.checkNfcEnable(PeopleQueryActivity.this))
			Toast.makeText(this, "��֧��NFC����δ����", Toast.LENGTH_SHORT).show();

		preferData = new PreferData(this);
		mNfcReadClient.setClientCallback(this);
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int result = mNfcReadClient.init(PeopleQueryActivity.this, ip, port, account, password, Utils.getIsWss(PeopleQueryActivity.this));
				mHandler.obtainMessage(600, result, result).sendToTarget();
			}
		}).start();

		// �������
		mAdapter = NfcAdapter.getDefaultAdapter(PeopleQueryActivity.this);
		if (mAdapter == null)
		{
			Log.e("test","�ֻ���֧��NFC����");
		} else if (!mAdapter.isEnabled()) {
			Log.e("test","�ֻ�δ��nfc");
			new android.support.v7.app.AlertDialog.Builder(PeopleQueryActivity.this).setTitle("�Ƿ��NFC")
					.setPositiveButton("ǰ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startActivity(new Intent("android.settings.NFC_SETTINGS"));
						}
					}).setNegativeButton("��", null).create().show();
		}
		else
			{
			mNfcReadClient.enableDispatch(PeopleQueryActivity.this);

		}
	}
	@Override
	protected void onNewIntent(final Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

			// ���߳�ͬ������
			async = new ReadAsync();
			async.execute(intent);

	}
	private void closeNFC()
	{
		mNfcReadClient.disableDispatch(PeopleQueryActivity.this);
		mNfcReadClient.disconnect();
	}
	private void clearInfo() {
		proBar.setProgress(0);
		im_head.setVisibility(View.GONE);
		tv_nameValue.setText("");
		tv_sexValue.setText(""); // �Ա�
		tv_nationalValue.setText(""); // ����
		tv_birValue.setText(""); // ����������
		tv_addressValue.setText(""); // סַ
		tv_idNumValue.setText(""); // ��ݺ���
		tv_issueValue.setText(""); // ǩ������
		tv_vaildDateValue.setText(""); // ��Ч����
	}

	@Override
	public void preExcute(long arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void updateProgress(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("arg0.progress=" + arg0);
		mHandler.obtainMessage(100, arg0, arg0).sendToTarget();
	}
	@Override
	public void onConnectChange(int arg0) {
		// TODO Auto-generated method stub
		mHandler.obtainMessage(400, arg0, arg0).sendToTarget();
	}
	class ReadAsync extends AsyncTask<Intent, Integer, IDCardItem>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			clearInfo();
		}

		@Override
		protected IDCardItem doInBackground(Intent... params) {
			// TODO Auto-generated method stub
			Intent intent = params[0];
			IDCardItem item = mNfcReadClient.readCardWithIntent(intent);
			return item;
		}

		@Override
		protected void onPostExecute(IDCardItem result)
		{
			// TODO Auto-g enerated method stub
			super.onPostExecute(result);
			updateResult(result);
		}
	}
	private void updateResult(IDCardItem arg0)
	{
		if (arg0.retCode != 1) {
			clearInfo();
		}
		if (arg0.retCode == 1)
		{
			updateViewID2(arg0);
			preferData.writeData("NfcPhoneReadOk", true); // ��Ǵ��ֻ����Զ�ȡ��

		}
		else
		{
			Toast.makeText(getApplicationContext(), CardCode.errorCodeDescription(arg0.retCode),Toast.LENGTH_SHORT).show();
		}
	}
	private void updateViewID2(IDCardItem item)
	{
		tv_nameValue.setText(item.partyName);
		tv_sexValue.setText(item.gender); // �Ա�
		tv_nationalValue.setText(item.nation); // ����

		tv_birValue.setText(item.bornDay); // ����������
		tv_addressValue.setText(item.certAddress); // סַ
		tv_idNumValue.setText(item.certNumber); // ��ݺ���
		tv_issueValue.setText(item.certOrg); // ǩ������
		tv_vaildDateValue.setText(item.effDate+"--"+item.expDate);

				Bitmap bitmap=item.picBitmap;
				if (null != bitmap)
				{
					im_head.setImageBitmap(bitmap);
					im_head.setVisibility(View.VISIBLE);
				}

			//��ʼ�˲�
			overflag=false;
			numValue=item.certNumber;
			startProgressDialog(QUERY);
			new Thread(checkRunnable).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG,"requestCode="+requestCode+",resultcode="+resultCode);
		if(requestCode==REQUEST_CODE_CAMERA){
			if(resultCode == Activity.RESULT_OK){
				if(data!=null){
					String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
					String filePath = FileUtil.getSaveFile(this).getAbsolutePath();
					if(!TextUtils.isEmpty(contentType)){
						if(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)){
							recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
						}
					}
				}
			}
		}
	}

	private void recIDCard(final String idCardSideFront, String filePath) {
		IDCardParams params = new IDCardParams();
		params.setImageFile(new File(filePath));
		params.setIdCardSide(idCardSideFront);
		params.setDetectDirection(true);
		params.setImageQuality(20);
		OCR.getInstance().recognizeIDCard(params, new OnResultListener<IDCardResult>() {
			@Override
			public void onResult(IDCardResult idCardResult) {
//				if(idCardResult!=null){
//					showMessage(idCardResult.toString());
//				}
				showToast("ʶ��ɹ�");
				parseInfo(idCardResult);
			}

			@Override
			public void onError(OCRError ocrError) {
				showMessage(ocrError.getMessage());
			}
		});
	}

	private void parseInfo(IDCardResult idResult) {
		if(idResult==null){
			return;
		}
		Word name = idResult.getName();
		Word gender = idResult.getGender();
		Word ethnic = idResult.getEthnic();
		Word birthday = idResult.getBirthday();
		Word address = idResult.getAddress();
		Word idNumber = idResult.getIdNumber();
		tv_nameValue.setText(name.toString());
		tv_sexValue.setText(gender.toString()); // �Ա�
		tv_nationalValue.setText(ethnic.toString()); // ����
		tv_birValue.setText(birthday.toString()); // ����������
		tv_addressValue.setText(address.toString()); // סַ
		tv_idNumValue.setText(idNumber.toString()); // ��ݺ���
		tv_issueValue.setText(""); // ǩ������
		tv_vaildDateValue.setText(""); // ��Ч����
		numValue = idNumber.toString();
//		numValue = "210303197010121618";
		new Thread(checkRunnable).start();
	}

	private void initQrCode(){
		OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {

			@Override
			public void onResult(AccessToken accessToken) {
				Log.i(TAG,"onResult = "+accessToken);
//				Toast.makeText(PeopleQueryActivity.this, "success", Toast.LENGTH_SHORT).show();
//				showMessage(accessToken.getAccessToken());
//				showToast("��֤�ɹ�");
				mHasToken = true;

			}

			@Override
			public void onError(OCRError ocrError) {
				if(ocrError!=null){
					ocrError.printStackTrace();
				}
				showMessage(ocrError.getMessage());
				mHasToken = false;
//				Toast.makeText(PeopleQueryActivity.this, ocrError.getMessage(), Toast.LENGTH_SHORT).show();
			}
		},PeopleQueryActivity.this.getApplicationContext());
	}

	private void showToast(final String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PeopleQueryActivity.this,msg,Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showMessage(final String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(PeopleQueryActivity.this).setMessage(msg);
				dialog.setTitle("ocr�Ľ��");
				dialog.setPositiveButton("Ok",null);
				dialog.show();
			}
		});
	}


	private void InitID2() {

		startIDCardReader();
		if (!openDevices()) {
			tv_hint.setText("�����豸��ʧ�ܣ�");
		} else {
			tv_hint.setText("�����豸�򿪳ɹ���");
			setPromptText("��ſ�...");
			try {
				String samid = idCardReader.getSAMID(Values.idPort);
				Log.e("samid=", "" + samid);
			} catch (Exception e) {

			}
		}
	}
	private void startIDCardReader() {
		// Start fingerprint sensor
		Map idrparams = new HashMap();
		idrparams.put(ParameterHelper.PARAM_SERIAL_SERIALNAME, Values.idSerialName);
		idrparams.put(ParameterHelper.PARAM_SERIAL_BAUDRATE, Values.idBaudrate);
		idCardReader = IDCardReaderFactory.createIDCardReader(this, TransportType.SERIALPORT, idrparams);

	}
	private boolean openDevices() {

		boolean bRet = false;
		RFID_ON();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			idCardReader.open(Values.idPort);
		} catch (IDCardReaderException e) {
			e.printStackTrace();
			bRet = false;
			//mtSetGPIOValue(64, false);
			return bRet;
		}

		bRet = true;
		mbStop = false;
		workThread = new WorkThread();
		workThread.start();// �߳�����
		return bRet;

	}
	public void RFID_ON()
	{
		Power_ON();
		this.mPowerOperate.enable_3_3Volt();
		this.mPowerOperate.enableRfid_5Volt();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(btn_NFC.getText().equals("�ر�NFC"))
			mNfcReadClient.disconnect();
	}

	@Override
	protected void onStop() {
		mbStop=true;
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(btn_NFC.getText().equals("�ر�NFC"))
			mNfcReadClient.disableDispatch(PeopleQueryActivity.this);
	}

	@Override
	protected void onResume() {
		mbStop=false;
		super.onResume();


	}

	public void Power_ON()
	{
		this.mPowerOperate.enablePremise_5Volt();
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (!mbStop) {
				runOnUiThread(new Runnable()
				{
					public void run() {
						{
							if (!ReadCardInfo())
							{
								//setPromptText("��ſ�...");
							}
							else
							{
								if(!overflag)
								{
									//	setPromptText("�ϴβ���δ���...");
									return;
								}
								setPromptText("�����ɹ�");
							}
						}
					}
				});

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private boolean ReadCardInfo()
	{
		try {
			if (!idCardReader.findCard(Values.idPort) || !idCardReader.selectCard(Values.idPort))
			{
				UtilTc.showLog("û��Ѱ����");
				return  false;
			}
		} catch (IDCardReaderException e) {
			e.printStackTrace();
			return  false;
		}
		try {
			clearInfo();
			tv_hint.setText("���ڶ���...");
			final IDCardInfo idCardInfo = new IDCardInfo();
			boolean bReadCard = false;
			long nTickSet = System.currentTimeMillis();
			while (System.currentTimeMillis()-nTickSet<2000)
			{
				bReadCard = idCardReader.readCard(Values.idPort, 1, idCardInfo);
				if (bReadCard) break;
			}
			if (bReadCard)
			{
				showAll1(idCardInfo);
				return true;
			}

		} catch (IDCardReaderException e) {
			e.printStackTrace();
		}

		return false;
	}



	private void setPromptText(String strPrompt)
	{
		tv_hint.setText(strPrompt);
	}

	private void showAll1(IDCardInfo idCardInfo)
	{
		tv_nameValue.setText(idCardInfo.getName().trim());
		tv_sexValue.setText(idCardInfo.getSex().trim()); // �Ա�
		tv_nationalValue.setText(idCardInfo.getNation()); // ����
		if(idCardInfo.getNation().contains("ά���"))
		{
			Toast.makeText(this, "�ص��ע������Ա��������˲�", Toast.LENGTH_SHORT).show();
		}
		tv_birValue.setText(idCardInfo.getBirth()); // ����������
		tv_addressValue.setText(idCardInfo.getAddress()); // סַ
		tv_idNumValue.setText(idCardInfo.getId()); // ��ݺ���
		tv_issueValue.setText(idCardInfo.getDepart()); // ǩ������
		tv_vaildDateValue.setText(idCardInfo.getValidityTime());
		if (idCardInfo.getPhoto() != null)
		{
			byte[] buf = new byte[WLTService.imgLength];
			if (1 == WLTService.wlt2Bmp(idCardInfo.getPhoto(), buf)) {
				Bitmap bitmap = IDPhotoHelper.Bgr2Bitmap(buf);
				if (null != bitmap)
				{
					im_head.setImageBitmap(bitmap);
					im_head.setVisibility(View.VISIBLE);
				}
			}
			//��ʼ�˲�
			overflag=false;
			numValue=idCardInfo.getName().trim();
			//	numValue="210105197010121610";
			startProgressDialog(QUERY);
			new Thread(checkRunnable).start();

		}
	}

	Runnable checkRunnable =new Runnable() {
		@Override
		public void run() {
			//����˲�
			String url_passenger ="http://61.176.222.166:8765/interface/idnumcheck/";
			HttpPost httpRequest =new HttpPost(url_passenger);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idnum",numValue));
			try{
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
				httpRequest.setEntity(formEntity);
				//ȡ��HTTP response
				HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
				Log.i(TAG, "code"+httpResponse.getStatusLine().getStatusCode());
				if(httpResponse.getStatusLine().getStatusCode()==200){
					String strResult= EntityUtils.toString(httpResponse.getEntity());
					Log.i(TAG, "��������ֵ�ǣ�"+strResult);
					//json ����
					JSONTokener jsonParser = new JSONTokener(strResult);
					JSONObject person = (JSONObject) jsonParser.nextValue();
					String code=person.getString("error code");
					//{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
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
	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			stopProgressDialog();
			overflag=true;
			switch (msg.what){
				case Values.SUCCESS_FORRESULR:
					showResult(jsResult);
					break;
				case Values.ERROR_CONNECT:
					tv_hint.setText("����ʧ��,��������ͷ�����");
					break;
				case Values.ERROR_OTHER:
					tv_hint.setText(errorMessage);
					break;
				case 100:
					proTv.setText(msg.arg1 + " %");
					proBar.setProgress(msg.arg1);
					break;
				case 200:
					break;
				case 400:
					Log.e("400",""+(msg.arg1 == 1 ? "������������" : "�������ѶϿ�"));
					if(msg.arg1==1)
					{
						proBar.setBackgroundColor(Color.GREEN);
						proTv.setText("����������");
					}
					else
						proBar.setBackgroundColor(Color.GRAY);
					break;
				case 600:
					break;
			}
			super.handleMessage(msg);
		}
	};
	private  void showResult(JSONObject jb){
		try{
			mResultLayout.setVisibility(View.VISIBLE);
			tv_hint.setText(""+jb.optString("result"));
			JSONObject jt = jb.getJSONObject("person");
			tv_repName.setText(""+jt.optString("name"));
			tv_repSex.setText(""+jt.opt("Sex"));
			tv_repBir.setText(""+jt.opt("birthday"));
			tv_repAddress.setText(""+jt.opt("address"));
			tv_repRylx.setText(""+jt.opt("rylx"));
			tv_repLxdw.setText(""+jt.opt("lxdw"));
			tv_repLxr.setText(""+jt.opt("lxr"));
			tv_repLxFs.setText(""+jt.opt("lxfs"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
