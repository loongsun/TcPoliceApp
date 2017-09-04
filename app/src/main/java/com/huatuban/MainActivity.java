package com.huatuban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.application.R;

public class MainActivity extends Activity {
	
	/**画画的内容区*/
	private HuaBanView hbView;
	/**设置粗细的Dialog*/
	private AlertDialog dialog;
	private View dialogView;
	private TextView shouWidth;
	private SeekBar widthSb;
	private int paintWidth;
	private Button btn_save,btn_re;
	class BtnOnClick implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_save:
				if(SaveViewUtil.saveScreen(hbView)){
					Toast.makeText(MainActivity.this, "截图成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, "截图失败，请检查sdcard是否可用", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_re:
				Log.e("e", "重画");
				hbView.clearScreen();
				break;
			}
			
		}
		
	}
	
	private void initView(){
		dialogView = getLayoutInflater().inflate(R.layout.dialog_width_set, null);
		shouWidth = (TextView) dialogView.findViewById(R.id.textView1);
		widthSb = (SeekBar) dialogView.findViewById(R.id.seekBar1);
		widthSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
				shouWidth.setText("当前选中宽度："+(progress+1));
				paintWidth = progress+1;
			}
		});
		hbView = (HuaBanView)findViewById(R.id.huaBanView1);
		
		dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("设置画笔宽度").
				setView(dialogView).setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hbView.setPaintWidth(paintWidth);
					}
				}).setNegativeButton("取消", null).create();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉应用标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_huban_main);
		btn_save=(Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new BtnOnClick());
		btn_re=(Button)findViewById(R.id.btn_re);
		btn_re.setOnClickListener(new BtnOnClick());
		initView();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu colorSm = menu.addSubMenu(1, 1, 1, "选择画笔颜色");
		colorSm.add(2, 200, 200, "红色");
		colorSm.add(2, 210, 210, "绿色");
		colorSm.add(2, 220, 220, "蓝色");
		colorSm.add(2, 230, 230, "紫色");
		colorSm.add(2, 240, 240, "黄色");
		colorSm.add(2, 250, 250, "黑色");
		menu.add(1, 2, 2, "设置画笔粗细");
		SubMenu widthSm = menu.addSubMenu(1, 3, 3, "设置画笔样式");
		widthSm.add(3, 300, 300, "线状画笔");
		widthSm.add(3, 301, 301, "填充画笔");
		menu.add(1, 4, 4, "清空画布");
		menu.add(1, 5, 5, "保存画布");
		menu.add(1, 6, 6, "退出应用");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int index = item.getItemId();
		switch(index){
		case 200:
			hbView.setColor(Color.BLACK);
			break;
		case 210:
			hbView.setColor(Color.GREEN);
			break;
		case 220:
			hbView.setColor(Color.BLUE);
			break;
		case 230:
			hbView.setColor(Color.MAGENTA);
			break;
		case 240:
			hbView.setColor(Color.YELLOW);
			break;
		case 250:
			hbView.setColor(Color.BLACK);
			break;
		case 2:
			dialog.show();
			break;
		case 300:
			hbView.setStyle(HuaBanView.PEN);
			break;
		case 301:
			hbView.setStyle(HuaBanView.PAIL);
			break;
		case 4:
			hbView.clearScreen();
			break;
		case 5:
			if(SaveViewUtil.saveScreen(hbView)){
				Toast.makeText(this, "截图成功", 0).show();
			}else{
				Toast.makeText(this, "截图失败，请检查sdcard是否可用", 0).show();
			}
			break;
		case 6:
			finish();
			break;
		}
		return true;
	}
	
	

}
