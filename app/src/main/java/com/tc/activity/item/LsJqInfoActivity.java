package com.tc.activity.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;
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
		btn_lsBack=(ImageView)findViewById(R.id.btn_lsBack);
	}

	class OnClick implements  OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.btn_lsBack:
					finish();
					break;
			}
		}
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
		//获取列表数据
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
			UtilTc.showLog("返回0了");
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
				holder.parentLayout = (LinearLayout) mView
						.findViewById(R.id.lin_bl);
				holder.parentLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.e("e", "onClick");
						//列表点击  区分文件类型
						itemOnClick(position);
						
					}
				});
				mView.setTag(holder);
			} else {
				holder = (ViewHolder) mView.getTag();
			}
			String ret = listFiles.get(position);
			UtilTc.showLog("ret       :"+ret);
			holder.tv_blTitle.setText(ret);
			return mView;
		}
		private class ViewHolder {
			TextView tv_blTitle;
			LinearLayout parentLayout;
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
					if (fileName.contains(jqNum) && fileName.endsWith(".txt")) {
						Log.e("e", "fileName"+fileName);
						listFiles.add(fileName);
						UtilTc.showLog("bltxt sss"+listFiles.size());
					} else if (fileName.contains(jqNum)
							&& fileName.endsWith(".mp4")) {
						listFiles.add(fileName);
					} else if (fileName.contains(jqNum)
							&& fileName.endsWith(".amr")) {
						listFiles.add(fileName);
					}else if(fileName.contains(jqNum)&&fileName.endsWith(".jpg")){
						Log.e("e", "fileName"+fileName);
						listFiles.add(fileName);
					}
				}
			}
		}
	}
	//列表点击
	private void  itemOnClick(int position){
		String mimeType=listFiles.get(position);
		if(mimeType.endsWith(".txt")){
			//查看txt
			startActivity(new Intent(LsJqInfoActivity.this,LsTxtAcitvity.class).
					putExtra("txtName", mimeType));
		}else if(mimeType.endsWith(".amr")){
			//播放录音
			startActivity(new Intent(LsJqInfoActivity.this,LsAmrActivity.class).
					putExtra("amrName", mimeType));
		}else if(mimeType.endsWith(".mp4")){
			//播放视频
			startActivity(new Intent(LsJqInfoActivity.this,LsMp4Activity.class).
					putExtra("mp4Name", mimeType));
			
		}else if(mimeType.endsWith(".jpg")){
			//查看照片
			startActivity(new Intent(LsJqInfoActivity.this,LsJpgActivity.class).
					putExtra("jpgName", mimeType));
		}
				
	}
}
