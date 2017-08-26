package com.tc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.item.RecorderVideoActivity;
import com.tc.activity.item.SenceNoteActivity;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.util.ConfirmDialog;
import com.tc.view.CustomProgressDialog;
import com.tc.view.VoiceDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class SenceExcute extends Activity  {

	private static final String TAG = SenceExcute.class.getSimpleName();
	private FTPClient myFtp;
	private int fileCount = 0;
	private int mTotalSize = 0;
	int i = 0;
	private ImageView btn_back;
	private LinearLayout lin_photo, lin_record, lin_camera;
	// ������Ϣ��ʾ
	private TextView tv_jqName, tv_jqNum, tv_jqTime, tv_jqPolice;
	// �ɼ���Ϣ����
	private TextView tv_photoCount, tv_recordCount, tv_cameraCount,tv_docCount;
	// ��¼
	private ListView lv_bookmark;
	// ��ť
	private Button btn_photo, btn_record, btn_camera, btn_bookmark, btn_upload;
	private PoliceStateListBean plb;
	private String jqName, jqId, jqPolice, jqTime;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 2;// ����
	private static final int PHOTO_REQUEST_GALLERY = 3;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 4;// ���
	// ¼��
	MediaRecorder mediaRecorder;
	private String sdPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	File recordFile;
	// �ļ��б�
	private List<String> allList = new ArrayList<String>();

	private List<String> bltxt = new ArrayList<String>();
	private List<String> camera = new ArrayList<String>();
	private List<String> record = new ArrayList<String>();
	private List<String> photoList=new ArrayList<String>();


	private String errorMessage = "";
	private TcApp mApp;
	private boolean isShowPage=false;
	private CommonAdapter mCommonAdapter = new CommonAdapter(this);
	private CustomProgressDialog progressDialog = null;
	private final static int UPLOAD=1;
	//�����ϴ����ļ�
	private String currentFile="";
	private String currentFilePaht="";
	private String 						mediaType="";
	private String mediaFormat="";
	private VoiceDialog mVoiceDialog;
	private boolean mIsRecording = false;




	public String[] allFiles;
	private String SCAN_PATH ;
	private static final String FILE_TYPE="*/*";
	private MediaScannerConnection conn;




	// ���ȿ�
	private void startProgressDialog(int type) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			switch (type) {
			case UPLOAD:
				progressDialog.setMessage("�����ϴ���Ϣ,���Ժ�");
				break;
			}
		}
		progressDialog.show();
	}
	// ȡ�����ȿ�
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	private void initWidgets() {
		// �ɼ�����
		tv_photoCount = (TextView)findViewById(
				R.id.tv_photocount);
		tv_recordCount = (TextView)findViewById(
				R.id.tv_recordcount);
		tv_cameraCount = (TextView) findViewById(
				R.id.tv_cameracount);
		tv_docCount=(TextView)findViewById(R.id.tv_doccount) ;

		// ������Ϣ
		tv_jqName = (TextView) findViewById(R.id.tv_jqName);
		tv_jqNum = (TextView) findViewById(R.id.tv_jqId);
		tv_jqTime = (TextView) findViewById(R.id.tv_jqTime);
		tv_jqPolice = (TextView) findViewById(R.id.tv_jqPolice);

		//list
		lv_bookmark = (ListView) findViewById(R.id.lv_bookmark);
		lv_bookmark.setAdapter(mCommonAdapter);
		// ��ť
		btn_photo = (Button) findViewById(R.id.btn_photo);
		btn_record = (Button) findViewById(R.id.btn_record);
		btn_camera = (Button) findViewById(R.id.btn_camera);
		btn_bookmark = (Button) findViewById(R.id.btn_bookmark);
		btn_upload = (Button) findViewById(R.id.btn_upload);

		btn_photo.setOnClickListener(new OnClick());
		btn_record.setOnClickListener(new OnClick());
		btn_camera.setOnClickListener(new OnClick());
		btn_bookmark.setOnClickListener(new OnClick());
		btn_upload.setOnClickListener(new OnClick());
		btn_back=(ImageView)findViewById(R.id.btn_senceExcuteBack);
		btn_back.setOnClickListener(new OnClick());
		//��ʼ�����ݿ�
		mApp = (TcApp)getApplication();
		mApp.initDatabase();
	}

	class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_photo:
				Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// ָ������������պ���Ƭ�Ĵ���·��
				cameraintent.putExtra(
						MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Values.PATH_PHOTO + jqId+"_" + UtilTc.getCurrentTime() + ".jpg")));
				startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
				break;
			case R.id.btn_record:
				if (!mIsRecording)
				{
					btn_record.setText("����");
					UtilTc.myToast(getApplicationContext(), "�뿪ʼ¼��");
					if(mVoiceDialog ==null){
						mVoiceDialog = new VoiceDialog(SenceExcute.this);
					}

					mVoiceDialog.showRecordingDialog();
					mVoiceDialog.getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							btn_record.performClick();
//							Toast.makeText(SenceExcute.this,"cancle",Toast.LENGTH_SHORT).show();
						}
					});
					startRecording();
					mHandler.obtainMessage(Values.MSG_REFRSH_RECORD).sendToTarget();
				} else
					{
					btn_record.setText("¼��");
					UtilTc.myToast(getApplicationContext(), "¼���ѱ���");

					stopRecording();

						record.add(recordFile.getName());
						allList.add(recordFile.getName());
						tv_recordCount.setText(""+record.size());
						mCommonAdapter.notifyDataSetChanged();



						//getFilesInfo();
					tv_recordCount.setText(""+record.size());
//					btn_record.setText("��ʼ");
				}
				break;
			case R.id.btn_bookmark:
				startBl();
				break;
			case R.id.btn_camera:
				startLx();
				//resume��ˢ��
				break;
			case R.id.btn_upload:// �ϴ�
				startProgressDialog(UPLOAD);
				SendFile sf = new SendFile();
				sf.start();
				break;
			case R.id.btn_senceExcuteBack:
				startActivity(new Intent(SenceExcute.this,MainTabActivity.class));
				finish();
				break;
			default:
				break;
			}
		}
	}
	//��ȡ�����ļ�
//	private void getAllList()
//
//	{
//		if(bltxt.size()>0)
//		allList.addAll(bltxt);
//		if(photoList.size()>0)
//		allList.addAll(photoList);
//		if(camera.size()>0)
//		allList.addAll(camera);
//		if(record.size()>0)
//		allList.addAll(record);
//	}
	
	public class SendFile extends Thread {
		private String currentPath="";
		@Override
		public void run() {
			try {
				myFtp = new FTPClient();
				myFtp.connect("61.176.222.166", 21); // ����
				myFtp.login("admin", "1234"); // ��¼

				UtilTc.showLog("file count:"+allList.size());
				for(int i=0;i<allList.size();i++)
				{
				 //�ж��ϴ����ĸ��ļ���
				 if(allList.get(i).endsWith(".doc")){
					 myFtp.changeDirectory("../");
					 myFtp.changeDirectory("wtxt");
					 currentPath=Values.PATH_BOOKMARK;
					 currentFilePaht="/wtxt";
				 }else if(allList.get(i).endsWith(".amr")){
					 myFtp.changeDirectory("../");
					 myFtp.changeDirectory("wwave");
					 currentPath=Values.PATH_RECORD;
					 currentFilePaht="/wwave";
				 }else if(allList.get(i).endsWith(".mp4")){
					 myFtp.changeDirectory("../");
					 myFtp.changeDirectory("wvideo");
					 currentPath=Values.PATH_CAMERA;
					 currentFilePaht="/wvideo";

				 }else if(allList.get(i).endsWith(".jpg")){
					 myFtp.changeDirectory("../");
					 myFtp.changeDirectory("wphoto");
					 currentPath=Values.PATH_PHOTO;
					 currentFilePaht="/wphoto";
				 }
				 
				    UtilTc.showLog("�ϴ������ļ�"+allList.get(i));
					File file = new File(currentPath+allList.get(i));
					fileCount = (int) file.length();

					mTotalSize = fileCount;
					currentFile=currentFilePaht+"/"+allList.get(i);
					MyFTPDataTransferListener listener = new MyFTPDataTransferListener();
					myFtp.upload(file, listener); // �ϴ�
				}
			} catch (Exception e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
			}

		}
	}

	private void startLx() {
		Intent intent = new Intent();
		intent.putExtra("name", plb.getJqNum());
		intent.setClass(SenceExcute.this, RecorderVideoActivity.class);
		startActivity(intent);
	}

	// ��¼
	private void startBl() {
		Intent intent = new Intent();
		intent.putExtra("name", plb.getJqNum());
		intent.setClass(SenceExcute.this, SenceNoteActivity.class);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		UtilTc.showLog("onresum****************************in the senceExcute");
		refreshList();
		super.onResume();
	}
    private void refreshList()
	{
		if(isShowPage)
		{
			allList.clear();

			photoList.clear();
			camera.clear();
			record.clear();
			bltxt.clear();


			getFilesInfo();

			tv_photoCount.setText(""+photoList.size());
			tv_cameraCount.setText(""+camera.size());
			tv_recordCount.setText(""+record.size());
			tv_docCount.setText(""+bltxt.size());

			mCommonAdapter.notifyDataSetChanged();

		}
	}
	private void getFilesInfo() {
		File file = new File(Values.ALLFILES);
		getFileName(file.listFiles(), plb.getJqNum());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.senceexcute);
		initWidgets();
		isShowPage();
	}


	private void setTextTitle(){
		tv_jqName.setText(jqName);
		tv_jqNum.setText(jqId);
		tv_jqTime.setText(jqTime);
		tv_jqPolice.setText(jqPolice);
	}
	
	
    private void isShowPage(){

    	mApp.getmDota().jq_queryOne("0", getIntent().getStringExtra("dbjqsence"));
        String size=""+Values.dbjqList.size();
        if(Values.dbjqList.size()>0)
        {
            plb= Values.dbjqList.get(0);
        	jqName = plb.getJqName();
    		jqId = plb.getJqNum();
    		jqTime = plb.getJqTime();
    		jqPolice=plb.getBjrName();
    		String result=mApp.getmDota().jq_ToMapQuery(jqId);
    		UtilTc.showLog("result:"+result);
    		setTextTitle();
    		if(!result.equals("0")){
    			isShowPage=false;
    			UtilTc.myToast(getApplicationContext(), "������Ա���ֳ�ִ��");
    			startActivity(new Intent(getApplicationContext(),MainTabActivity.class));
    			
    		}else{
    			isShowPage=true;
    		}
        }else{
        	UtilTc.myToast(getApplicationContext(), "������Ա���ֳ�ִ��");
			startActivity(new Intent(getApplicationContext(),MainTabActivity.class));
			finish();
        }
    }

	private void getjqInfo() {
		plb = (PoliceStateListBean) getIntent()
				.getSerializableExtra("jqInfo");
		jqName = plb.getJqName();
		jqId = plb.getJqNum();
		jqTime = plb.getJqTime();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case PHOTO_REQUEST_TAKEPHOTO:// ��ѡ������ʱ����
	 //resume()��ˢ�½���

			break;
		case PHOTO_REQUEST_GALLERY:// ��ѡ��ӱ��ػ�ȡͼƬʱ
			// ���ǿ��жϣ������Ǿ��ò����������¼��õ�ʱ��㲻�ᱨ�쳣����ͬ
			if (data != null)
				startPhotoZoom(data.getData());
			break;
		case PHOTO_REQUEST_CUT:// ���صĽ��
			if (data != null)
				// setPicToView(data);
				sentPicToNext(data);
			break;
		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private void sentPicToNext(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if (photo == null) {
			} else {
				// ����
				tv_photoCount.setText(""
						+ Integer.parseInt(tv_photoCount.getText().toString()
								.trim() + 1));
			}
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
				byte[] photodata = baos.toByteArray();
				System.out.println(photodata.toString());
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public int getVoiceLevel(int maxLevel) {
		if (mediaRecorder != null) {//mMediaRecorder.getMaxAmplitude()ֵ��1-12367֮��
			try {//����ֵ��1-7֮��
				int value =  maxLevel * mediaRecorder.getMaxAmplitude() / 12368 + 1;
				Log.i(TAG,"max value = "+mediaRecorder.getMaxAmplitude());
				Log.i(TAG,"getVoiceLevel "+value);
				return value;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "getVoiceLevel ", e);
			}
		}
		return 1;
	}


	private void startRecording() {
		mIsRecording = true;
		recordFile = new File(Values.PATH_RECORD + plb.getJqNum()+"_"
				+ UtilTc.getCurrentTime() + ".amr");
		mediaRecorder = new MediaRecorder();
		// �жϣ�����ǰ�ļ��Ѵ��ڣ���ɾ��
		// if (recordFile.exists()) {
		// recordFile.delete();
		// }
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mediaRecorder.setOutputFile(recordFile.getAbsolutePath());

		try {
			// ׼���ÿ�ʼ¼��
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			Log.e(TAG,"startRecording",e);
		} catch (Exception e) {
			Log.e(TAG,"startRecording",e);
		}
	}

	private void stopRecording() {
		mIsRecording = false;
		if (recordFile != null && mediaRecorder!=null) {
			mediaRecorder.stop();
			mediaRecorder.release();
		}
		if(mHandler.hasMessages(Values.MSG_REFRSH_RECORD)){
			mHandler.removeMessages(Values.MSG_REFRSH_RECORD);
		}
		if(mVoiceDialog !=null){
			mVoiceDialog.dismissDialog();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mIsRecording){
			stopRecording();
		}
	}

	// -------------------------�����ļ�
	private void getFileName(File[] files, String jqNum) {

		if (files != null)// nullPointer
		{
			for (File file : files) {
				if (file.isDirectory()) {
					getFileName(file.listFiles(), jqNum);
				}
				else
					{
					String fileName = file.getName();

					if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
						Log.e("e", "fileName"+fileName);
						bltxt.add(fileName);
						allList.add(fileName);
						UtilTc.showLog("bltxt sss"+bltxt.size());
					} else if (fileName.contains(jqNum)
							&& fileName.endsWith(".mp4")) {
						camera.add(fileName);
						allList.add(fileName);
					} else if (fileName.contains(jqNum)
							&& fileName.endsWith(".amr")) {
						allList.add(fileName);
						record.add(fileName);
					}else if(fileName.contains(jqNum)&&fileName.endsWith(".jpg")){
						Log.e("e", "fileName"+fileName);
						allList.add(fileName);
						photoList.add(fileName);
					}
				}
			}
		}
	}

	// ----------------------------------------------�ϴ�
	Runnable uploadInfo = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url_passenger = "http://61.176.222.166:8765/interface/wupdate/";
			HttpPost httpRequest = new HttpPost(url_passenger);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//��ѯ������ʱ��͵����ֳ�ʱ��
			mApp.getmDota().jq_queryTime(plb.getJqNum());
			Log.e("1231312", "pnum"+plb.getJqNum()+ Values.POLICE_GOTIME);
			params.add(new BasicNameValuePair("wnum", plb.getJqNum()));
			params.add(new BasicNameValuePair("wstate", "2"));
			params.add(new BasicNameValuePair("starttime", Values.POLICE_GOTIME));
			params.add(new BasicNameValuePair("endtime",
					Values.POLICE_ARRIVETIME));
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						params, "UTF-8");
				httpRequest.setEntity(formEntity);
				// ȡ��HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				Log.e("code", "code"
						+ httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					Log.e("e", "��������ֵ�ǣ�" + strResult);
					// json ����
					JSONTokener jsonParser = new JSONTokener(strResult);
					JSONObject person = (JSONObject) jsonParser.nextValue();
					String code = person.getString("error code");
					if (code.trim().equals("0")) {
						//�ϴ��ɹ�
						mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
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
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Values.SUCCESS_RECORDUPLOAD://�ϴ��ɹ� ���쾯���Ϊ��ʷ����
				successForJq();
				break;
			case Values.ERROR_CONNECT:
				UtilTc.myToastForContent(getApplicationContext());
				break;
			case Values.ERROR_OTHER:
				UtilTc.myToast(getApplicationContext(), "��������:"
						+ errorMessage);
				break;
			case Values.SUCCESS_UPLOAD:
				UtilTc.showLog("�ļ��ϴ��ɹ�");
				stopProgressDialog();
				new Thread(uploadInfo).start();
				//�ı侯��״̬
				break;
			case Values.ERROR_UPLOAD:
				UtilTc.showLog("�ļ��ϴ�ʧ��");
				stopProgressDialog();
				break;
			case Values.MSG_REFRSH_RECORD:
				  if(mediaRecorder!=null && mVoiceDialog!=null){
					  mVoiceDialog.updateVoiceLevel(getVoiceLevel(7));
				  }
				mHandler.sendEmptyMessageDelayed(Values.MSG_REFRSH_RECORD,100);
					break;

			}
		};
	};
	
	//�ı侯��״̬
	private void successForJq(){
		ContentValues mContentValues = new ContentValues();
		mContentValues.clear();
		mContentValues.put("flag", 1);
		mApp.getmDota().update(Values.DB_JQINFO, 
				mContentValues, "dbjqNum=?",new String[]{plb.getJqNum()});
		startActivity(new Intent(SenceExcute.this,MainTabActivity.class));
		finish();
	}
	//WPS�鿴
	private void doOpenWord(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "application/msword";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//��⵽ϵͳ��δ��װOliveOffice��apk����
			Toast.makeText(this, "δ�ҵ����", Toast.LENGTH_LONG).show();
			//���ȵ�www.olivephone.com/e.apk���ز���װ
		}
	}
	//WPS�鿴
	private void doOpenPhoto(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "image/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//��⵽ϵͳ��δ��װOliveOffice��apk����
			Toast.makeText(this, "δ�ҵ����", Toast.LENGTH_LONG).show();
			//���ȵ�www.olivephone.com/e.apk���ز���װ
		}
	}
	//WPS�鿴
	private void doOpenAudio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "audio/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//��⵽ϵͳ��δ��װOliveOffice��apk����
			Toast.makeText(this, "δ�ҵ����", Toast.LENGTH_LONG).show();
			//���ȵ�www.olivephone.com/e.apk���ز���װ
		}
	}
	//WPS�鿴
	private void doOpenVedio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "vedio/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//��⵽ϵͳ��δ��װOliveOffice��apk����
			Toast.makeText(this, "δ�ҵ����", Toast.LENGTH_LONG).show();
			//���ȵ�www.olivephone.com/e.apk���ز���װ
		}
	}

	//bl list
	private class CommonAdapter extends BaseAdapter {

		Activity mContent ;
		public CommonAdapter(Activity mCtx){
			mContent =mCtx;
		}
		@Override
		public int getCount() {
			Log.i(TAG,"GET COUNT");
			if (allList!=null) {
				UtilTc.showLog(" bltxt.size()"+ bltxt.size());
				return allList.size();
			}
			UtilTc.showLog("����0��");
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (allList != null) {
				return allList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Log.e("e", "getView");
			ViewHolder holder = null;
			View mView = convertView;
			if (mView == null) {
				mView = LayoutInflater.from(
						getApplicationContext()).inflate(
						R.layout.item_bltxt, null);
				holder = new ViewHolder();
				holder.tv_blTitle = (TextView) mView.findViewById(R.id.tv_blTitle);
				holder.iv_delete = (ImageView) mView.findViewById(R.id.iv_delete);
				holder.iv_edit = (ImageView) mView.findViewById(R.id.iv_edit);

				holder.parentLayout = (LinearLayout) mView
						.findViewById(R.id.lin_bl);
				holder.parentLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.e("e", "onClick");
				
					}
				});
				mView.setTag(holder);
			} else {
				holder = (ViewHolder) mView.getTag();
			}

			//word�ļ�ɾ��
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					final ConfirmDialog confirmDialog = new ConfirmDialog(mContent, "ȷ��Ҫɾ����?", "ɾ��", "ȡ��");
					confirmDialog.show();
					confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							// TODO Auto-generated method stub
							confirmDialog.dismiss();

							File file=null;
							String ret = allList.get(position);


							if (ret.endsWith(".doc")) {
								file = new File(Values.PATH_BOOKMARK+ret);

							} else if (  ret.endsWith(".mp4")) {
								file = new File(Values.PATH_CAMERA+ret);
							} else if (ret.endsWith(".amr"))
							{
								file = new File(Values.PATH_RECORD+ret);
							}else if ( ret.endsWith(".jpg")){
								file = new File(Values.PATH_PHOTO+ret);
							}


							if(file.exists())
							{
								boolean isDel = file.delete();
								if(isDel)
								{
									refreshList();
								}
							}
						}

						@Override
						public void doCancel() {
							// TODO Auto-generated method stub
							confirmDialog.dismiss();
						}
					});


				}
			});

			//word�ļ��༭
			holder.iv_edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					String ret = allList.get(position);
					if (ret.endsWith(".doc")) {

						doOpenWord(Values.PATH_BOOKMARK+ret);

					} else if (  ret.endsWith(".mp4"))
					{
						doOpenVedio(Values.PATH_CAMERA+ret);

					} else if (ret.endsWith(".amr"))
					{
						doOpenAudio( Values.PATH_RECORD+ret);

					}else if ( ret.endsWith(".jpg")){
						doOpenPhoto(Values.PATH_PHOTO+ret);

					}


				}
			});
			String ret = allList.get(position);
			UtilTc.showLog("ret       :"+ret);
			holder.tv_blTitle.setText(ret);
			return mView;
		}
		private class ViewHolder {
			TextView tv_blTitle;
			LinearLayout parentLayout;
			ImageView iv_delete,iv_edit;
		}
	}
	private class MyFTPDataTransferListener implements FTPDataTransferListener {
		@Override
		public void aborted() {
			// TODO Auto-generated method stub
		}
		@Override
		public void completed() {// �ϴ��ɹ�
			// TODO Auto-generated method stub
			UtilTc.showLog("currentFile:"+currentFile);
			UtilTc.showLog("currentFile ��3λ"+currentFile.substring(currentFile.length()-3,currentFile.length()));
			mediaFormat=currentFile.substring(currentFile.length()-3,currentFile.length());
			if(mediaFormat.equals("doc")){
				mediaType="�ĵ�";
			}else if(mediaFormat.equals("amr")){
				mediaType="��Ƶ";
			}else if(mediaFormat.equals("mp4")){
				mediaType="��Ƶ";
			}else if(mediaFormat.equals("jpg")){
				mediaType="ͼƬ";
			}
			new Thread(media).start();

			Message msg;
			msg = Message.obtain();
			msg.what = Values.SUCCESS_UPLOAD;
			mHandler.sendMessage(msg);
		}
		@Override
		public void failed() {// �ϴ�ʧ��
			// TODO Auto-generated method stub
			Message msg;
			msg = Message.obtain();
			msg.what = Values.ERROR_UPLOAD;
			mHandler.sendMessage(msg);
		}
		@Override
		public void started() {// �ϴ���ʼ
			// TODO Auto-generated method stub
			Message msg;
			msg = Message.obtain();
			msg.what = 2;
			mHandler.sendMessage(msg);
		}
		@Override
		public void transferred(int length) {// �ϴ����̼���
			int progress = length;
			i = i + length;
			System.out.println("mTotalSize=" + mTotalSize);
			System.out.println("length=" + length);
			System.out.println("i=" + i);
			Message msg;
			msg = Message.obtain();
			msg.what = 1;
			msg.obj = progress;
			mHandler.sendMessage(msg);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			startActivity(new Intent(SenceExcute.this,MainTabActivity.class));
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	//�ϴ�ý����Ϣ
	Runnable media=new Runnable() {
		@Override
		public void run() {
			String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
			HttpPost httpRequest = new HttpPost(url_passenger);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//��ѯ������ʱ��͵����ֳ�ʱ��
			mApp.getmDota().jq_queryTime(plb.getJqNum());
			Log.e("1231312", "pnum"+plb.getJqNum()+ Values.POLICE_GOTIME);
			params.add(new BasicNameValuePair("A_ID", plb.getJqNum()));
			params.add(new BasicNameValuePair("A_type", mediaType));
			params.add(new BasicNameValuePair("A_Format",mediaFormat));
			params.add(new BasicNameValuePair("A_MM",
					currentFile));
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						params, "UTF-8");
				httpRequest.setEntity(formEntity);
				// ȡ��HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				Log.e("code", "code"
						+ httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					Log.e("e", "�ϴ�ý���ֵ�ǣ�" + strResult);
					// json ����
					JSONTokener jsonParser = new JSONTokener(strResult);
					JSONObject person = (JSONObject) jsonParser.nextValue();
					String code = person.getString("error code");
					if (code.trim().equals("0")) {
						//�ϴ��ɹ�
					//	mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
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
		
}