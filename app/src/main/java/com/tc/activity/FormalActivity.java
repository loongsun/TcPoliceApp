package com.tc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tc.application.R;
import com.tc.fragment.tableFragment.AnimeFragment;
import com.tc.fragment.tableFragment.MovieFragment;
import com.tc.fragment.tableFragment.PhothFragment;
import com.tc.fragment.tableFragment.VarietyFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class FormalActivity extends Activity implements OnClickListener {

	private Button movieBtn, tvBtn, animeBtn, varietyBtn;
	private List<Button> btnList = new ArrayList<Button>();
	private FragmentManager fm;
	private FragmentTransaction ft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formal_main);

		findById();

		// �M��ϵ�yĬ�J��movie
		fm = getFragmentManager();
		ft = fm.beginTransaction();

		setBackgroundColorById(R.id.movie_btn);
		ft.replace(R.id.fragment_content, new MovieFragment());
		ft.commit();
	}

	private void findById() {
		movieBtn = (Button) this.findViewById(R.id.movie_btn);
		tvBtn = (Button) this.findViewById(R.id.tv_btn);
		animeBtn = (Button) this.findViewById(R.id.anime_btn);
		varietyBtn = (Button) this.findViewById(R.id.variety_btn);
		movieBtn.setOnClickListener(this);
		tvBtn.setOnClickListener(this);
		animeBtn.setOnClickListener(this);
		varietyBtn.setOnClickListener(this);

		btnList.add(movieBtn);
		btnList.add(tvBtn);
		btnList.add(animeBtn);
		btnList.add(varietyBtn);
	}

	private void setBackgroundColorById(int btnId) {
		for (Button btn : btnList) {
			if (btn.getId() == btnId) {
				btn.setBackgroundColor(getResources().getColor(R.color.statemain));
			} else {
				btn.setBackgroundColor(getResources().getColor(R.color.backNoText));
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		switch (v.getId()) {

		case R.id.movie_btn:
			setBackgroundColorById(R.id.movie_btn);

			ft.replace(R.id.fragment_content, new MovieFragment());
			break;

		case R.id.tv_btn:
			setBackgroundColorById(R.id.tv_btn);
			ft.replace(R.id.fragment_content, new PhothFragment());
			break;

		case R.id.anime_btn:
			setBackgroundColorById(R.id.anime_btn);
			ft.replace(R.id.fragment_content, new AnimeFragment());
			break;

		case R.id.variety_btn:
			setBackgroundColorById(R.id.variety_btn);
			ft.replace(R.id.fragment_content, new VarietyFragment());
			break;

		default:
			break;
		}
		// ��Ҫ�����ύ
		ft.commit();
	}


	public void changeView(){
		fm = getFragmentManager();
		ft = fm.beginTransaction();

		setBackgroundColorById(R.id.variety_btn);
		ft.replace(R.id.fragment_content, new VarietyFragment());
	}

}
