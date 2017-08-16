package com.tc.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;

public class LsTxtAcitvity extends Activity{
	
	private TextView tv_showInfo;
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.tv_lsTxt:
				finish();
				break;
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_lstxt);
		tv_showInfo=(TextView)findViewById(R.id.tv_lsTxt);
		tv_showInfo.setOnClickListener(new OnClick());
		show();
	}
	private void show(){
		String path=Values.PATH_BOOKMARK+getIntent().getStringExtra("txtName");
        try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);  
            String str = "";   
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
            	str = str+mimeTypeLine;
    		} 
            UtilTc.showLog("str"+str);
            tv_showInfo.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
