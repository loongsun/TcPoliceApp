package com.tc.activity.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
import com.tc.util.ConfirmDialog;
import com.tc.view.LsAmrActivity;
import com.tc.view.LsJpgActivity;
import com.tc.view.LsMp4Activity;
import com.tc.view.LsTxtAcitvity;

public class LsJqInfoActivity extends Activity{
	private TextView tv_jqName,tv_jqNum,tv_jqTime,tv_jqBjr;
	private PoliceStateListBean plb;
	private ListView lv_lsList;
	private CommonAdapter mCommonAdapter = new CommonAdapter();
	private List<String>listFiles=new ArrayList<String>();
	private ImageView btn_lsBack;

	//init
	private void initWidgets(){
		tv_jqName=(TextView)findViewById(R.id.tv_lsJqName);
		tv_jqNum=(TextView)findViewById(R.id.tv_lsjqId);
		tv_jqTime=(TextView)findViewById(R.id.tv_lsjqTime);
		tv_jqBjr=(TextView)findViewById(R.id.tv_lsjqbjr);
		lv_lsList=(ListView)findViewById(R.id.lv_lsList);
		btn_lsBack=(ImageView)findViewById(R.id.btn_lsBackls);
	}
	 public void onFinish(View v)
	 {
		 this.finish();
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_lsjqinfo);
		initWidgets();
		getData();
	}
	
	//init data
	private void getData(){
		plb=(PoliceStateListBean) getIntent().getSerializableExtra("lsjqInfo");
		tv_jqName.setText(plb.getJqName());
		tv_jqNum.setText(plb.getJqNum());
		tv_jqTime.setText(plb.getJqTime());
		tv_jqBjr.setText(plb.getBjrName());
		//????§Ò?????
		File file = new File(Values.ALLFILES);
		listFiles.clear();
		getFileName(file.listFiles(), plb.getJqNum());
		lv_lsList.setAdapter(mCommonAdapter);
	}
	private class CommonAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (listFiles!=null) {
				UtilTc.showLog("listFiles"+ listFiles.size());
				return listFiles.size();
			}
			UtilTc.showLog("????0??");
			return 0;
		}
		@Override
		public Object getItem(int position) {
			if (listFiles != null) {
				return listFiles.get(position);
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

				mView.setTag(holder);
			} else {
				holder = (ViewHolder) mView.getTag();
			}
			String ret = listFiles.get(position);
			UtilTc.showLog("ret       :"+ret);
			holder.tv_blTitle.setText(ret);
			//word??????
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					final ConfirmDialog confirmDialog = new ConfirmDialog(LsJqInfoActivity.this,"Delete it?", "ok", "chancel");
					confirmDialog.show();
					confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							// TODO Auto-generated method stub
							confirmDialog.dismiss();

							File file=null;
							String ret = listFiles.get(position);


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
									listFiles.remove(position);
									notifyDataSetChanged();
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

			//word?????
			holder.iv_edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					String ret = listFiles.get(position);
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

			return mView;
		}
		private class ViewHolder {
			TextView tv_blTitle;
			ImageView iv_delete,iv_edit;
			LinearLayout parentLayout;
		}
	}
	//WPS??
	private void doOpenWord(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "application/msword";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//???????¦Ä???OliveOffice??apk????
			Toast.makeText(this, "¦Ä??????", Toast.LENGTH_LONG).show();
			//?????www.olivephone.com/e.apk????????
		}
	}
	//WPS??
	private void doOpenPhoto(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "image/*";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//???????¦Ä???OliveOffice??apk????
			Toast.makeText(this, "¦Ä??????", Toast.LENGTH_LONG).show();
			//?????www.olivephone.com/e.apk????????
		}
	}
	//WPS??
	private void doOpenAudio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "audio/amr";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//???????¦Ä???OliveOffice??apk????
			Toast.makeText(this, "¦Ä??????", Toast.LENGTH_LONG).show();
			//?????www.olivephone.com/e.apk????????
		}
	}
	//WPS??
	private void doOpenVedio(String newPath){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "video/mp4";
		intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
		try{
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			//???????¦Ä???OliveOffice??apk????
			Toast.makeText(this, "¦Ä??????", Toast.LENGTH_LONG).show();
			//?????www.olivephone.com/e.apk????????
		}
	}

	private void getFileName(File[] files, String jqNum) {

		if (files != null)// nullPointer
		{
			for (File file : files) {
				if (file.isDirectory()) {
					getFileName(file.listFiles(), jqNum);
				} else {
					String fileName = file.getName();
					if (fileName.startsWith(jqNum) && fileName.endsWith(".doc")) {
						Log.e("e", "fileName"+fileName);
						listFiles.add(fileName);
						UtilTc.showLog("bltxt sss"+listFiles.size());
					} else if (fileName.startsWith(jqNum)
							&& fileName.endsWith(".mp4")) {
						listFiles.add(fileName);
					} else if (fileName.startsWith(jqNum)
							&& fileName.endsWith(".amr")) {
						listFiles.add(fileName);
					}else if(fileName.startsWith(jqNum)&&fileName.endsWith(".jpg")){
						Log.e("e", "fileName"+fileName);
						listFiles.add(fileName);
					}
				}
			}
		}
	}
	//?§Ò???
	private void  itemOnClick(int position){
		String mimeType=listFiles.get(position);
		if(mimeType.endsWith(".txt")){
			//??txt
			startActivity(new Intent(LsJqInfoActivity.this,LsTxtAcitvity.class).
					putExtra("txtName", mimeType));
		}else if(mimeType.endsWith(".amr")){
			//???????
			startActivity(new Intent(LsJqInfoActivity.this,LsAmrActivity.class).
					putExtra("amrName", mimeType));
		}else if(mimeType.endsWith(".mp4")){
			//???????
			startActivity(new Intent(LsJqInfoActivity.this,LsMp4Activity.class).
					putExtra("mp4Name", mimeType));
			
		}else if(mimeType.endsWith(".jpg")){
			//?????
			startActivity(new Intent(LsJqInfoActivity.this,LsJpgActivity.class).
					putExtra("jpgName", mimeType));
		}
				
	}
}
