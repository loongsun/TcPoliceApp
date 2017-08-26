package com.tc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.tc.activity.casemain.KcqzMainList;
import com.tc.application.R;
import com.tc.fragment.FormalSurveyFragment;
import com.tc.fragment.MyMarkFragment;

/**
 * @author yangyu
 *	�����������Զ���TabHost
 */
public class MainTabActivity extends FragmentActivity{	
	//����FragmentTabHost����
	private FragmentTabHost mTabHost;
	//����һ������
	private LayoutInflater layoutInflater;
	//�������������Fragment����
	private Class fragmentArray[] = {PoliceStateActivity.class,FormalSurveyFragment.class,MyMarkFragment.class,InfoQueryActivity.class,
			KcqzMainList.class,UserManageActivity.class};
	//������������Ű�ťͼƬ
	private int mImageViewArray[] = {R.drawable.tab_home_btn,R.drawable.tab_xskc_btn,R.drawable.tab_yjbz_btn,
			R.drawable.tab_message_btn, R.drawable.tab_kcqz_btn,R.drawable.tab_user_btn};
	//Tabѡ�������
	private String mTextviewArray[] = {"ִ��ȡ֤", "���¿���","һͼ��ע","��Ϣ��ѯ", "�ֳ���¼","�û�����"};
	public static PoliceStateListBean plb;
	private int tabPage=0;
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        initView();
        getIntentInfo();
    }
	
	private void getIntentInfo()
	{
		plb=(PoliceStateListBean) getIntent().getSerializableExtra("jqInfo");
		tabPage=getIntent().getIntExtra("tab", 0);
		if(tabPage!=0)
		{
			mTabHost.setCurrentTab(tabPage);
			UtilTc.showLog(plb.getJqName());
		}
	
	}
	/**
	 * 
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);
		//ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
		for(int i = 0; i < count; i++)
		{
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť��ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}
	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setTextColor(view.getResources().getColor(R.color.Black));
		textView.setText(mTextviewArray[index]);
		return view;
	}
}
