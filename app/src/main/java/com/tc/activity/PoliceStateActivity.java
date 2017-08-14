package com.tc.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.item.LsJqInfoActivity;
import com.tc.activity.item.MarkerMapActivity;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.view.CustomProgressDialog;

public class PoliceStateActivity extends Fragment {
	private ListView lv_police;
	private List<PoliceStateListBean> stateList = new ArrayList<PoliceStateListBean>();
	private CommonAdapter mCommonAdapter = new CommonAdapter();
	// 警情互换
	private TextView tv_ssjq, tv_dbjq, tv_lsjq;
	private View v_ssjq, v_dbjq, v_lsjq;
	private String errorMessage = "";
	private TcApp mApp;
	private boolean isHaveList = false;
	private ImageView btn_policestateReturn;
	private boolean isRun=true;
	private CustomProgressDialog progressDialog = null;

	private void startProgressDialog(int type) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());
			progressDialog.setMessage("正在同步警情,请稍后");
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	@Override
	public void onStop() {
		isRun=false;
		super.onStop();
	}


	// 初始化控件
	private void initWidgets() {
		lv_police = (ListView) this.getView().findViewById(R.id.lv_police);
		tv_ssjq = (TextView) this.getView().findViewById(R.id.tv_ssjq);
		tv_dbjq = (TextView) this.getView().findViewById(R.id.tv_dbjq);
		tv_lsjq = (TextView) this.getView().findViewById(R.id.tv_lsjq);
		v_ssjq = (View) this.getView().findViewById(R.id.v_state_ssjq);
		v_dbjq = (View) this.getView().findViewById(R.id.v_state_dbjq);
		v_lsjq = (View) this.getView().findViewById(R.id.v_state_lsjq);
		tv_ssjq.setOnClickListener(new OnClick());
		tv_dbjq.setOnClickListener(new OnClick());
		tv_lsjq.setOnClickListener(new OnClick());
		Values.JQ_STATESBTN = 1;
		btn_policestateReturn = (ImageView) this.getView().findViewById(
				R.id.btn_policestateReturn);
		btn_policestateReturn.setOnClickListener(new OnClick());
	}

	// 点击变色
	private void ssjqOnClick() {
		Values.JQ_STATESBTN = 1;
		v_ssjq.setVisibility(View.VISIBLE);
		v_dbjq.setVisibility(View.GONE);
		v_lsjq.setVisibility(View.GONE);
	}

	// db jq
	private void dbjqOnClick() {
		Values.JQ_STATESBTN = 2;
		v_dbjq.setVisibility(View.VISIBLE);
		v_lsjq.setVisibility(View.GONE);
		v_ssjq.setVisibility(View.GONE);
	}

	private void lsjqOnClick() {
		Values.JQ_STATESBTN = 3;
		v_dbjq.setVisibility(View.GONE);
		v_lsjq.setVisibility(View.VISIBLE);
		v_ssjq.setVisibility(View.GONE);
	}

	// 待办警情数据
	private void jqData(String states) {
		isHaveList = mApp.getmDota().jq_query(states);
	}

	class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_ssjq:
				startProgressDialog(1);
				ssjqOnClick();
				new Thread(ssjqRun).start();
				break;
			case R.id.tv_dbjq:
				UtilTc.showLog("dbssq"); // dbjq处理
				dbjqOnClick();
				jqData("0");
				stateList = Values.dbjqList;
				if (stateList.size() <= 0 || !isHaveList) {
					mCommonAdapter.notifyDataSetChanged();
					UtilTc.myToast(getActivity().getApplicationContext(),
							"暂无待办警情");
				} else {
					mCommonAdapter.notifyDataSetChanged();
				}
				break;
			case R.id.tv_lsjq:
				lsjqOnClick();
				jqData("1");
				stateList = Values.lsjqList;
				if (stateList.size() <= 0 || !isHaveList) {
					mCommonAdapter.notifyDataSetChanged();
					UtilTc.myToast(getActivity().getApplicationContext(),
							"暂无历史警情");
				} else {
					mCommonAdapter.notifyDataSetChanged();
				}
				break;
			case R.id.btn_policestateReturn:
				getActivity().finish();
				break;
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		isRun=true;
		tv_ssjq.callOnClick();
		Values.JQ_STATESBTN = 1;
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.policestateactivity, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 申请资源
		initWidgets();
		// new Thread(ssjqRun).start();
		// 初始化数据库
		mApp = (TcApp) getActivity().getApplication();
		mApp.initDatabase();
	}

	// 申请警情
	Runnable ssjqRun = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRun) {
				stateList = new ArrayList<PoliceStateListBean>();
				stateList.clear();
				String url_passenger = "http://61.176.222.166:8765/interface/warning/";
				HttpPost httpRequest = new HttpPost(url_passenger);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("pnum", Values.USERNAME));
				try {
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
							params, "UTF-8");
					httpRequest.setEntity(formEntity);
					// 取得HTTP response
					HttpClient client = new DefaultHttpClient(); // 请求超时
					client.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					// 读取超时
					client.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, 5000);
					HttpResponse httpResponse = client.execute(httpRequest);
					// HttpResponse httpResponse = new DefaultHttpClient().
					// execute(httpRequest);
					Log.e("code", "code"
							+ httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String strResult = EntityUtils.toString(httpResponse
								.getEntity());
						// Log.e("e", "传回来的值是：" + strResult);
						// json 解析
						JSONTokener jsonParser = new JSONTokener(strResult);
						JSONObject person = (JSONObject) jsonParser.nextValue();
						String code = person.getString("error code");
						Log.e("e", "返回状态：" + code);
						if (code.trim().equals("0")) {
							JSONObject jb = person.getJSONObject("data");
							JSONArray jqInfo = jb.getJSONArray("winfo");
							for (int i = 0; i < jqInfo.length(); i++) {
								JSONObject jqob = jqInfo.getJSONObject(i);
								PoliceStateListBean pl = new PoliceStateListBean();
								pl.setJqName(jqob.getString("wname"));
								pl.setJqPosition(jqob.getString("wadress"));
								pl.setJqNum(jqob.getString("wnum"));
								pl.setJqTime(jqob.getString("wtime"));
								pl.setWx(jqob.getString("wx"));
								pl.setWy(jqob.getString("wy"));
								pl.setBjrName(jqob.getString("wperson"));
								stateList.add(pl);
							}
							if (stateList != null) {
								stateHandler
										.sendEmptyMessage(Values.SUCCESS_SSJQ);
							}
						} else if (code.trim().equals("10001")) {
							stateHandler.sendEmptyMessage(Values.ERROR_NOUSER);

						} else if (code.trim().equals("10003")) {
							JSONObject jb = person.getJSONObject("data");
							errorMessage = jb.getString("message");
							stateHandler.sendEmptyMessage(Values.ERROR_OTHER);
						}
					} else {
						stateHandler.sendEmptyMessage(Values.ERROR_CONNECT);
					}
					UtilTc.showLog("ssjq");
					SystemClock.sleep(30000);
				} catch (Exception e) {
					e.printStackTrace();
					SystemClock.sleep(30000);
					stateHandler.sendEmptyMessage(Values.ERROR_CONNECT);
				}
			}
		}
	};

	Handler stateHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			stopProgressDialog();
			switch (msg.what) {
			case Values.ERROR_CONNECT:
				UtilTc.myToastForContent(getActivity().getApplicationContext());
				break;
			case Values.SUCCESS_SSJQ:
				lv_police.setAdapter(mCommonAdapter);
				break;
			case Values.ERROR_NOUSER:
				UtilTc.myToast(getActivity().getApplicationContext(), "用户不存在");
				break;
			case Values.ERROR_OTHER:
				UtilTc.myToast(getActivity().getApplicationContext(), "其它错误:"
						+ errorMessage);
				break;
			}
		};
	};

	// 初始化数据
	private void initData() {
		PoliceStateListBean pl = new PoliceStateListBean();
		pl.setJqName("案情1");
		pl.setJqPosition("山东省济南市高新区");
		pl.setJqTime("2017-03-03");
		stateList.add(pl);

		PoliceStateListBean pl1 = new PoliceStateListBean();
		pl1.setJqName("案情2");
		pl1.setJqPosition("山东省济南市高新区");
		pl1.setJqTime("2017-03-03");
		stateList.add(pl1);
	}

	private void intentToMap(int position) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("ssjqInfo",
				(Serializable) stateList.get(position));
		intent.setClass(getActivity(), MarkerMapActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private class CommonAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (stateList != null) {
				return stateList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (stateList != null) {
				return stateList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			View mView = convertView;
			if (mView == null) {
				mView = LayoutInflater.from(
						getActivity().getApplicationContext()).inflate(
						R.layout.item_policestate_list, null);
				holder = new ViewHolder();
				holder.tv_jqName = (TextView) mView.findViewById(R.id.tv_name);
				holder.tv_jqTime = (TextView) mView.findViewById(R.id.tv_time);
				holder.tv_jqPosition = (TextView) mView
						.findViewById(R.id.tv_address);
				holder.parentLayout = (LinearLayout) mView
						.findViewById(R.id.lin_jqInfo);
				// holder.parentLayout.setTag(R.id.checkout_listitem_layout,
				// position);
				holder.parentLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.e("e", "onClick");
						if (Values.JQ_STATESBTN == 2) {
							String result = mApp.getmDota().jq_ToMapQuery(
									stateList.get(position).getJqNum());
							if (result.equals("0")) {
								startActivity(new Intent(getActivity()
										.getApplicationContext(),
										SenceExcute.class).putExtra(
										"dbjqsence", stateList.get(position)
												.getJqNum()));

							} else {
								intentToMap(position);
							}
						} else if (Values.JQ_STATESBTN == 1) {
							// 判断案件编号
							if (isRepeatJq(position)) {
								UtilTc.myToast(getActivity()
										.getApplicationContext(),
										"有相同案件编号待办警情,请先处理");
							} else {
								intentToMap(position);
							}
						} else if (Values.JQ_STATESBTN == 3) {
							// 历史警情
							intentToLs(position);
						}
					}
				});
				mView.setTag(holder);
			} else {
				holder = (ViewHolder) mView.getTag();
			}
			PoliceStateListBean ret = stateList.get(position);
			holder.tv_jqName.setText(ret.getJqName());
			holder.tv_jqTime.setText(ret.getJqTime());
			holder.tv_jqPosition.setText(ret.getJqPosition());
			return mView;
		}

		private class ViewHolder {
			TextView tv_jqName, tv_jqTime, tv_jqPosition;
			LinearLayout parentLayout;
		}
	}

	// 是否同名待办警情
	private boolean isRepeatJq(int location) {
		mApp.getmDota().jq_queryOne("0", stateList.get(location).getJqNum());
		if (Values.dbjqList.size() > 0) {
			return true;
		}
		return false;
	}

	// 历史警情
	private void intentToLs(int position) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("lsjqInfo",
				(Serializable) stateList.get(position));
		intent.setClass(getActivity(), LsJqInfoActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
