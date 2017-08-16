package com.tc.view;

import com.sdses.tool.Values;
import com.tc.application.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class LsMp4Activity extends Activity{
	private VideoView videoView;
	private Button btn_start,btn_stop;
	private String path="";
	private  void initWidgets(){
		videoView=(VideoView)findViewById(R.id.videoView);
		btn_start=(Button)findViewById(R.id.btn_mp4Start);
		btn_stop=(Button)findViewById(R.id.btn_mp4Stop);
		btn_start.setOnClickListener(new OnClick());
		btn_stop.setOnClickListener(new OnClick());
		 path=Values.PATH_CAMERA+getIntent().getStringExtra("mp4Name");

	}
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_mp4Start:
				start();
				break;
			case R.id.btn_mp4Stop:
				stop();
				break;
			}
			
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_mp4);
		initWidgets();
	}
	private void start(){
		videoView.setVideoPath(path);
		videoView.setMediaController(new MediaController(LsMp4Activity.this));
		videoView.seekTo(0);
		videoView.requestFocus();
		videoView.start();

	}
	private void stop(){
		videoView.pause();
	}
}
