package com.tc.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.sdses.tool.Values;
import com.tc.application.R;

public class LsJpgActivity extends Activity{
	private ImageView iv_jpg;
	class OnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.iv_lsJpg:
				finish();
				
				break;
			}
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_jpg);
		show();
	}
	private void show(){
		iv_jpg=(ImageView)findViewById(R.id.iv_lsJpg);
		iv_jpg.setOnClickListener(new OnClick());
		String path=Values.PATH_PHOTO+getIntent().getStringExtra("jpgName");
		Bitmap bm=BitmapFactory.decodeFile(path);
		if(bm!=null){
			iv_jpg.setImageBitmap(bm);
		}
	}
}
