package com.tc.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;

public class LsAmrActivity  extends Activity{
	private Button btn_start,btn_stop;
	private MediaPlayer mPlayer;
	private LinearLayout lin_amr;
	private SeekBar seekbar;
	private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突 
	private boolean ifplay = false;
	private boolean iffirst = false;
	private Timer mTimer;  
	private TimerTask mTimerTask; 
	String path;
	private void initWidgets(){
		lin_amr=(LinearLayout)findViewById(R.id.lin_amr);
		btn_start=(Button)findViewById(R.id.btn_amrStart);
		btn_stop=(Button)findViewById(R.id.btn_amrStop);
		seekbar=(SeekBar)findViewById(R.id.seekbar);
		lin_amr.setOnClickListener(new OnClick());
		btn_start.setOnClickListener(new OnClick());
		btn_stop.setOnClickListener(new OnClick());
		seekbar.setOnSeekBarChangeListener(new MySeekbar());
	}
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_amrStart:
				start();
				break;
			case R.id.btn_amrStop:
				stop();
				break;
			case R.id.lin_amr:
				finish();
				break;
			}
		}
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mTimer.cancel();
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_amr);
		initWidgets();
		mPlayer=new MediaPlayer();
		path=Values.PATH_RECORD+getIntent().getStringExtra("amrName");
	}
	
	public boolean start() {
        try {  
			 //设置要播放的文件
        
//    		String path=Values.PATH_RECORD+getIntent().getStringExtra("amrName");
//    		UtilTc.showLog("amr path:"+path);
//    		mPlayer.setDataSource(path);
//			 mPlayer.prepare();
//			 //播放
//			 mPlayer.start();		
        	if (mPlayer != null && !ifplay) {
        		btn_start.setText("暂停");
				if (!iffirst) {
					mPlayer.reset();
					try {
						mPlayer.setDataSource(path);
						mPlayer.prepare();// 准备
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					seekbar.setMax(mPlayer.getDuration());//设置进度条
					//----------定时器记录播放进度---------//  
			        mTimer = new Timer();  
			        mTimerTask = new TimerTask() {  
			            @Override  
			            public void run() {   
			                if(isChanging==true) { 
			                    return;  
			                }
			                seekbar.setProgress(mPlayer.getCurrentPosition());
			                UtilTc.showLog(""+mPlayer.getCurrentPosition()+"-"+mPlayer.getDuration());
			                if(mPlayer.getCurrentPosition()-mPlayer.getDuration()<30){
			                	mHandler.sendEmptyMessage(0x01);
			                }
			            }  
			        }; 
			        mTimer.schedule(mTimerTask, 0, 10); 
					iffirst=true;
				}
				mPlayer.start();// 开始
				ifplay = true;
			} else if (ifplay) {
				btn_start.setText("继续");
				mPlayer.pause();
				ifplay = false;
			}
        	
        	}catch(Exception e){
			 e.printStackTrace();
		 }
		return false;
	}
	public void stop() {
//		mPlayer.stop();
//		mPlayer.release();
//		mPlayer = null;
//		return false;
		if (ifplay) {
			mPlayer.seekTo(0);
		} else {
			mPlayer.reset();
			try {
				mPlayer.setDataSource(path);
				mPlayer.prepare();// 准备
				mPlayer.start();// 开始
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		if(mPlayer != null){
//			if(mPlayer.isPlaying()){
//				mPlayer.stop();
//			}
//			mPlayer.release();
//		}
	}
	class MySeekbar implements OnSeekBarChangeListener {
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			isChanging=true;  
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			mPlayer.seekTo(seekbar.getProgress());
			isChanging=false;  
		}

	}
	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			   case 0x01:
				   btn_start.setText("开始");
				break;
			}
			
		};
	};
}
