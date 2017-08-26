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
	// 文字信息显示
	private TextView tv_jqName, tv_jqNum, tv_jqTime, tv_jqPolice;
	// 采集信息数量
	private TextView tv_photoCount, tv_recordCount, tv_cameraCount,tv_docCount;
	// 笔录
	private ListView lv_bookmark;
	// 按钮
	private Button btn_photo, btn_record, btn_camera, btn_bookmark, btn_upload;
	private PoliceStateListBean plb;
	private String jqName, jqId, jqPolice, jqTime;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 2;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 3;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 4;// 结果
	// 录音
	MediaRecorder mediaRecorder;
	private String sdPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	File recordFile;
	// 文件列表
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
	//正在上传的文件
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




	// 进度框
	private void startProgressDialog(int type) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			switch (type) {
			case UPLOAD:
				progressDialog.setMessage("正在上传信息,请稍后");
				break;
			}
		}
		progressDialog.show();
	}
	// 取消进度框
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	private void initWidgets() {
		// 采集数量
		tv_photoCount = (TextView)findViewById(
				R.id.tv_photocount);
		tv_recordCount = (TextView)findViewById(
				R.id.tv_recordcount);
		tv_cameraCount = (TextView) findViewById(
				R.id.tv_cameracount);
		tv_docCount=(TextView)findViewById(R.id.tv_doccount) ;

		// 文字信息
		tv_jqName = (TextView) findViewById(R.id.tv_jqName);
		tv_jqNum = (TextView) findViewById(R.id.tv_jqId);
		tv_jqTime = (TextView) findViewById(R.id.tv_jqTime);
		tv_jqPolice = (TextView) findViewById(R.id.tv_jqPolice);

		//list
		lv_bookmark = (ListView) findViewById(R.id.lv_bookmark);
		lv_bookmark.setAdapter(mCommonAdapter);
		// 按钮
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
		//初始化数据库
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
				// 指定调用相机拍照后照片的储存路径
				cameraintent.putExtra(
						MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Values.PATH_PHOTO + jqId+"_" + UtilTc.getCurrentTime() + ".jpg")));
				startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
				break;
			case R.id.btn_record:
				if (!mIsRecording)
				{
					btn_record.setText("结束");
					UtilTc.myToast(getApplicationContext(), "请开始录音");
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
					btn_record.setText("录音");
					UtilTc.myToast(getApplicationContext(), "录音已保存");

					stopRecording();

						record.add(recordFile.getName());
						allList.add(recordFile.getName());
						tv_recordCount.setText(""+record.size());
						mCommonAdapter.notifyDataSetChanged();



						//getFilesInfo();
					tv_recordCount.setText(""+record.size());
//					btn_record.setText("开始");
				}
				break;
			case R.id.btn_bookmark:
				startBl();
				break;
			case R.id.btn_camera:
				startLx();
				//resume会刷新
				break;
			case R.id.btn_upload:// 上传
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
	//获取所有文件
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
				myFtp.connect("61.176.222.166", 21); // 连接
				myFtp.login("admin", "1234"); // 登录

				UtilTc.showLog("file count:"+allList.size());
				for(int i=0;i<allList.size();i++)
				{
				 //判断上传到哪个文件夹
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
				 
				    UtilTc.showLog("上传所有文件"+allList.get(i));
					File file = new File(currentPath+allList.get(i));
					fileCount = (int) file.length();

					mTotalSize = fileCount;
					currentFile=currentFilePaht+"/"+allList.get(i);
					MyFTPDataTransferListener listener = new MyFTPDataTransferListener();
					myFtp.upload(file, listener); // 上传
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

	// 笔录
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
    			UtilTc.myToast(getApplicationContext(), "暂无人员到现场执法");
    			startActivity(new Intent(getApplicationContext(),MainTabActivity.class));
    			
    		}else{
    			isShowPage=true;
    		}
        }else{
        	UtilTc.myToast(getApplicationContext(), "暂无人员到现场执法");
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
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
	 //resume()会刷新界面

			break;
		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData());
			break;
		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null)
				// setPicToView(data);
				sentPicToNext(data);
			break;
		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
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
				// 递增
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
		if (mediaRecorder != null) {//mMediaRecorder.getMaxAmplitude()值在1-12367之间
			try {//返回值在1-7之间
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
		// 判断，若当前文件已存在，则删除
		// if (recordFile.exists()) {
		// recordFile.delete();
		// }
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mediaRecorder.setOutputFile(recordFile.getAbsolutePath());

		try {
			// 准备好开始录音
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

	// -------------------------遍历文件
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

	// ----------------------------------------------上传
	Runnable uploadInfo = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url_passenger = "http://61.176.222.166:8765/interface/wupdate/";
			HttpPost httpRequest = new HttpPost(url_passenger);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//查询出出警时间和到达现场时间
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
				// 取得HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				Log.e("code", "code"
						+ httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					Log.e("e", "传回来的值是：" + strResult);
					// json 解析
					JSONTokener jsonParser = new JSONTokener(strResult);
					JSONObject person = (JSONObject) jsonParser.nextValue();
					String code = person.getString("error code");
					if (code.trim().equals("0")) {
						//上传成功
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
			case Values.SUCCESS_RECORDUPLOAD://上传成功 待办警情变为历史警情
				successForJq();
				break;
			case Values.ERROR_CONNECT:
				UtilTc.myToastForContent(getApplicationContext());
				break;
			case Values.ERROR_OTHER:
				UtilTc.myToast(getApplicationContext(), "其它错误:"
						+ errorMessage);
				break;
			case Values.SUCCESS_UPLOAD:
				UtilTc.showLog("文件上传成功");
				stopProgressDialog();
				new Thread(uploadInfo).start();
				//改变警情状态
				break;
			case Values.ERROR_UPLOAD:
				UtilTc.showLog("文件上传失败");
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
	
	//改变警情状态
	private void successForJq(){
		ContentValues mContentValues = new ContentValues();
		mContentValues.clear();
		mContentValues.put("flag", 1);
		mApp.getmDota().update(Values.DB_JQINFO, 
				mContentValues, "dbjqNum=?",new String[]{plb.getJqNum()});
		startActivity(new Intent(SenceExcute.this,MainTabActivity.class));
		finish();
	}
	//WPS查看
	private void doOpenWord(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "application/msword";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//检测到系统尚未安装OliveOffice的apk程序
			Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
			//请先到www.olivephone.com/e.apk下载并安装
		}
	}
	//WPS查看
	private void doOpenPhoto(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "image/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//检测到系统尚未安装OliveOffice的apk程序
			Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
			//请先到www.olivephone.com/e.apk下载并安装
		}
	}
	//WPS查看
	private void doOpenAudio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "audio/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//检测到系统尚未安装OliveOffice的apk程序
			Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
			//请先到www.olivephone.com/e.apk下载并安装
		}
	}
	//WPS查看
	private void doOpenVedio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "vedio/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//检测到系统尚未安装OliveOffice的apk程序
			Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
			//请先到www.olivephone.com/e.apk下载并安装
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
			UtilTc.showLog("返回0了");
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

			//word文件删除
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					final ConfirmDialog confirmDialog = new ConfirmDialog(mContent, "确定要删除吗?", "删除", "取消");
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

			//word文件编辑
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
		public void completed() {// 上传成功
			// TODO Auto-generated method stub
			UtilTc.showLog("currentFile:"+currentFile);
			UtilTc.showLog("currentFile 后3位"+currentFile.substring(currentFile.length()-3,currentFile.length()));
			mediaFormat=currentFile.substring(currentFile.length()-3,currentFile.length());
			if(mediaFormat.equals("doc")){
				mediaType="文档";
			}else if(mediaFormat.equals("amr")){
				mediaType="音频";
			}else if(mediaFormat.equals("mp4")){
				mediaType="视频";
			}else if(mediaFormat.equals("jpg")){
				mediaType="图片";
			}
			new Thread(media).start();

			Message msg;
			msg = Message.obtain();
			msg.what = Values.SUCCESS_UPLOAD;
			mHandler.sendMessage(msg);
		}
		@Override
		public void failed() {// 上传失败
			// TODO Auto-generated method stub
			Message msg;
			msg = Message.obtain();
			msg.what = Values.ERROR_UPLOAD;
			mHandler.sendMessage(msg);
		}
		@Override
		public void started() {// 上传开始
			// TODO Auto-generated method stub
			Message msg;
			msg = Message.obtain();
			msg.what = 2;
			mHandler.sendMessage(msg);
		}
		@Override
		public void transferred(int length) {// 上传过程监听
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

	//上传媒体信息
	Runnable media=new Runnable() {
		@Override
		public void run() {
			String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
			HttpPost httpRequest = new HttpPost(url_passenger);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//查询出出警时间和到达现场时间
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
				// 取得HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				Log.e("code", "code"
						+ httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					Log.e("e", "上传媒体的值是：" + strResult);
					// json 解析
					JSONTokener jsonParser = new JSONTokener(strResult);
					JSONObject person = (JSONObject) jsonParser.nextValue();
					String code = person.getString("error code");
					if (code.trim().equals("0")) {
						//上传成功
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