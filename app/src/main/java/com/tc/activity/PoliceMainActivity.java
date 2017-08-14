package com.tc.activity;

import com.tc.activity.caseinfo.XczfListActivity;
import com.tc.application.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PoliceMainActivity extends Fragment{
	private ImageView btn_kcblReturn;
	private LinearLayout lin_xsaj,lin_xzaj;
	private void initWidgets(){
		btn_kcblReturn=(ImageView)this.getView().findViewById(R.id.btn_kcblReturn);
		btn_kcblReturn.setOnClickListener(new OnClick());
		lin_xsaj=(LinearLayout)this.getView().findViewById(R.id.lin_xsaj);
		lin_xzaj=(LinearLayout)this.getView().findViewById(R.id.lin_xzaj);
		lin_xsaj.setOnClickListener(new OnClick());
		lin_xzaj.setOnClickListener(new OnClick());
	}
	class OnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_kcblReturn:
				getActivity().finish();
				break;
				case R.id.lin_xsaj:
					startActivity(new Intent(getActivity(), XczfListActivity.class));
					break;
				case R.id.lin_xzaj:

					break;
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.fragment_3, null);		
		
	}	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initWidgets();
	}
}