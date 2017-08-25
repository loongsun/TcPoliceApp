package com.tc.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.FormalNewActivity;
import com.tc.application.R;
import com.tc.view.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;


public class FormalSurveyFragment extends Fragment {
    private ImageView btn_kcqzReturn;
    private ListView lv_kcqz;
    private CommonAdapter mCommonAdapter = new CommonAdapter();
    private List<PoliceStateListBean> stateList = new ArrayList<PoliceStateListBean>();
    private CustomProgressDialog progressDialog = null;

    String errorMessage = "";

    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
            progressDialog.setMessage("???????????,?????");
        }
        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }


    private void initWidgets() {
        btn_kcqzReturn = (ImageView) this.getView().findViewById(R.id.btn_kcqzReturn);
        btn_kcqzReturn.setOnClickListener(new OnClick());
        lv_kcqz = (ListView) this.getView().findViewById(R.id.lv_kcqzList);
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_formal_survey, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidgets();

        initData();
    }

    private void initData() {
        stateList.clear();
        new Thread(ssjqRun).start();


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
                holder.icon=(ImageView)mView.findViewById(R.id.item_img_zfqz);
                holder.tv_jqPosition = (TextView) mView.findViewById(R.id.tv_address);

                holder.parentLayout = (LinearLayout) mView.findViewById(R.id.lin_jqInfo);

                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }

//            holder.tv_jqName.setText(ret.getJqName() + "(" + ret.getWtype() + ")");
//            holder.tv_jqTime.setText(ret.getJqTime());
//            holder.tv_jqPosition.setText("???????" + ret.getJqNum());

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Log.e("e", "onClick");
                    if (stateList.get(position).getWtype().equals("???°???")) {
                        startActivity(new Intent(getActivity(), FormalNewActivity.class)
//                                    startActivity(new Intent(getActivity(), FormalActivity.class)
                                        .putExtra("name", stateList.get(position).getJqNum())
                                        .putExtra("anjianname", stateList.get(position).getJqName())
                        );
                    } else {
//                            startActivity(new Intent(getActivity(), FormalActivity.class)
                        startActivity(new Intent(getActivity(), FormalNewActivity.class)
                                .putExtra("name", stateList.get(position).getJqNum())
                                .putExtra("anjianname", stateList.get(position).getJqName())
                        );
                    }

                }
            });

            PoliceStateListBean ret = stateList.get(position);
            holder.tv_jqName.setText(ret.getJqName());
            holder.tv_jqTime.setText(ret.getJqTime());
            holder.tv_jqPosition.setText(ret.getJqPosition());

            if(ret.getWtype().equals("刑事案件"))
            {
                BitmapDrawable drawable=getDrawableFromId(R.drawable.icon_xs);
                holder.icon.setImageDrawable(drawable);
            }
            else
            {
                BitmapDrawable drawable=getDrawableFromId(R.drawable.icon_xz);
                holder.icon.setImageDrawable(drawable);
            }
            return mView;
        }

        private class ViewHolder {
            TextView tv_jqName, tv_jqTime, tv_jqPosition;
            LinearLayout parentLayout;
            ImageView icon;
        }
    }

    private BitmapDrawable getDrawableFromId(int id){  //这个id就是类似R.drawable.lander_firing
        Resources res = getResources();
        return (BitmapDrawable)res.getDrawable(id);
    }

    // ??????????
    Runnable ssjqRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            stateList = new ArrayList<PoliceStateListBean>();
            stateList.clear();
            String url_passenger = "http://61.176.222.166:8765/interface/OneMapLable/GetLables.asp";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("pnum", Values.USERNAME));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // ???HTTP response
                HttpClient client = new DefaultHttpClient(); // ?????
                client.getParams().setParameter(
                        CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                // ??????
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
                    // Log.e("e", "????????????" + strResult);
                    // json ????
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    Log.e("e", "????????" + code);
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
                            pl.setWtype(jqob.getString("wtype"));
                            stateList.add(pl);
                        }
                        if (stateList != null) {
                            stateHandler
                                    .sendEmptyMessage(Values.SUCCESS_SSJQ);
                        }
                    } else if (code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        stateHandler.sendEmptyMessage(Values.ERROR_NOUSER);

                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = "????????";
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
    };


    Handler stateHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            stopProgressDialog();
            switch (msg.what) {
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getActivity().getApplicationContext());
                    break;
                case Values.SUCCESS_SSJQ:
                    lv_kcqz.setAdapter(mCommonAdapter);
                    break;
                case Values.ERROR_NOUSER:
                    UtilTc.myToast(getActivity().getApplicationContext(), errorMessage);
                    break;
                case Values.ERROR_OTHER:
                    UtilTc.myToast(getActivity().getApplicationContext(), "????????:"
                            + errorMessage);
                    break;
            }
        }

        ;
    };
}
