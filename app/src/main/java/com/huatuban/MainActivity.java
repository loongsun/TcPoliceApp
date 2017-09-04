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
	
	/**������������*/
	private HuaBanView hbView;
	/**���ô�ϸ��Dialog*/
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
					Toast.makeText(MainActivity.this, "��ͼ�ɹ�", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, "��ͼʧ�ܣ�����sdcard�Ƿ����", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_re:
				Log.e("e", "�ػ�");
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
				shouWidth.setText("��ǰѡ�п�ȣ�"+(progress+1));
				paintWidth = progress+1;
			}
		});
		hbView = (HuaBanView)findViewById(R.id.huaBanView1);
		
		dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("���û��ʿ��").
				setView(dialogView).setPositiveButton("ȷ��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hbView.setPaintWidth(paintWidth);
					}
				}).setNegativeButton("ȡ��", null).create();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��Ӧ�ñ�����
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
		SubMenu colorSm = menu.addSubMenu(1, 1, 1, "ѡ�񻭱���ɫ");
		colorSm.add(2, 200, 200, "��ɫ");
		colorSm.add(2, 210, 210, "��ɫ");
		colorSm.add(2, 220, 220, "��ɫ");
		colorSm.add(2, 230, 230, "��ɫ");
		colorSm.add(2, 240, 240, "��ɫ");
		colorSm.add(2, 250, 250, "��ɫ");
		menu.add(1, 2, 2, "���û��ʴ�ϸ");
		SubMenu widthSm = menu.addSubMenu(1, 3, 3, "���û�����ʽ");
		widthSm.add(3, 300, 300, "��״����");
		widthSm.add(3, 301, 301, "��仭��");
		menu.add(1, 4, 4, "��ջ���");
		menu.add(1, 5, 5, "���滭��");
		menu.add(1, 6, 6, "�˳�Ӧ��");
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
				Toast.makeText(this, "��ͼ�ɹ�", 0).show();
			}else{
				Toast.makeText(this, "��ͼʧ�ܣ�����sdcard�Ƿ����", 0).show();
			}
			break;
		case 6:
			finish();
			break;
		}
		return true;
	}
	
	

}
