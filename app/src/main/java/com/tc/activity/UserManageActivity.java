package com.tc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tc.application.R;

public class UserManageActivity extends Fragment{
	private ImageView btn_userReturn;
	private void inidWidgets(){
		btn_userReturn=(ImageView)this.getView().findViewById(R.id.btn_userReturn);
		btn_userReturn.setOnClickListener(new OnClick());
	}

	class OnClick implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.btn_userReturn:
					getActivity().finish();
					break;
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.fragment_5, null);

	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inidWidgets();
	}
}
