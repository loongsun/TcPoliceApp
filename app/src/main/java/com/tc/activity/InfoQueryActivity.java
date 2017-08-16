package com.tc.activity;
import com.sdses.tool.UtilTc;
import com.tc.activity.item.CarCheckActivity;
import com.tc.activity.item.GoodsQueryActivity;
import com.tc.activity.item.PeopleQueryActivity;
import com.tc.activity.item.QyQueryActivity;
import com.tc.application.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InfoQueryActivity extends Fragment {
	private LinearLayout lin_peopleCheck, iv_carcheck, lin_wpCheck,
			lin_qyCheck;
	private ImageView btn_infoqueryReturn;

	private void initWidgets() {
		lin_peopleCheck = (LinearLayout) this.getView().findViewById(
				R.id.lin_peopleQuery);
		iv_carcheck = (LinearLayout) this.getView().findViewById(
				R.id.lin_carQuery);
		lin_wpCheck = (LinearLayout) this.getView().findViewById(
				R.id.lin_wpQuery);
		lin_qyCheck = (LinearLayout) this.getView().findViewById(
				R.id.lin_qyQuery);
		btn_infoqueryReturn=(ImageView)this.getView().findViewById(R.id.btn_infoqueryReturn);
		btn_infoqueryReturn.setOnClickListener(new OnClick());
		lin_peopleCheck.setOnClickListener(new OnClick());
		iv_carcheck.setOnClickListener(new OnClick());
		lin_wpCheck.setOnClickListener(new OnClick());
		lin_qyCheck.setOnClickListener(new OnClick());

	}

	class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.lin_peopleQuery:
				startActivity(new Intent(getActivity(), PeopleQueryActivity.class));
				break;
			case R.id.lin_carQuery:
				startActivity(new Intent(getActivity(), CarCheckActivity.class));
				break;
			case R.id.lin_wpQuery:
				startActivity(new Intent(getActivity(),GoodsQueryActivity.class));
				break;
			case R.id.lin_qyQuery:
                startActivity(new Intent(getActivity(),QyQueryActivity.class));
				break;
			case R.id.btn_infoqueryReturn:
				UtilTc.showLog("btn_infoqueryReturn");
				getActivity().finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_infoquery, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initWidgets();
	}
}
