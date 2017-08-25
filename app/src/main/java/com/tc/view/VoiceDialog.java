package com.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.application.R;

public class VoiceDialog {
	
	private Dialog mDialog;
	
	private ImageView mIcon;
	private ImageView mVideo;
	private TextView mLable;
	private Context mContext;

	public VoiceDialog(Context context) {

		mContext = context;
	}
	
	public void showRecordingDialog(){
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_recorder,null);
		mDialog.setContentView(view);
		mIcon = (ImageView) mDialog.findViewById(R.id.img_record);
		mVideo = (ImageView) mDialog.findViewById(R.id.img_voice);
		mLable = (TextView) mDialog.findViewById(R.id.tv_dialogcontent);
		mDialog.show();
	}
	
	
	public void recoding(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.recorder);
			mLable.setText("��ָ�ϻ���ȡ������");
			
		}
		
	}
	
	
	public void wantToCancel(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
//			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("�ɿ���ָ��ȡ������");
		}
	}
	
	public void tooShort(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
//			mIcon.setImageResource(R.drawable.voice_to_short);
//			mLable.setText("¼��ʱ�����");
		}
		
	}
	
	public void dismissDialog(){
		if (mDialog != null&& mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog =null;
		}
		
	}
	
	/**
	 * ¼���ĵȼ�
	 * @param level
	 */
	public void updateVoiceLevel(int level){
		if (mDialog != null&& mDialog.isShowing()) {
//			mIcon.setVisibility(View.VISIBLE);
//			mVideo.setVisibility(View.VISIBLE);
//			mLable.setVisibility(View.VISIBLE);
			
			//������ԴID��̬����ͼƬ
			int resid = mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
			mVideo.setImageResource(resid);
			//mVideo.setImageResource(R.drawable.call_interface_hands_free);
		
		}
		
	}
	
}
