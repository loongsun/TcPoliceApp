package com.tc.fragment.tableFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc.activity.Account;
import com.tc.application.R;
import com.tc.bean.ImageListBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PhothFragment extends Fragment {
    @BindView(R.id.photo_recycleview)
    RecyclerView photoRecycleview;
    @BindView(R.id.take_photo_tv)
    TextView takePhotoTv;
    Unbinder unbinder;


    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, null);
        unbinder = ButterKnife.bind(this, view);
        mImageList = new ArrayList<>();
        initView();
        return view;


    }

    private void initView() {

        photoRecycleview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mAdapter = new BackOrderAdapter(mImageList,getActivity());

        photoRecycleview.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.take_photo_tv)
    public void onViewClicked() {
    }


    static class BackOrderAdapter extends RecyclerView.Adapter<BackOrderAdapter.ViewHolder> {

        private ArrayList<ImageListBean> mDatas;
        private Context mContext;
        private Account mAccount;

        public BackOrderAdapter(ArrayList<ImageListBean> titles, Context context) {
            this.mDatas = titles;
            this.mContext = context;
            mAccount = Account.GetInstance();
        }

        @Override
        public int getItemCount() {

            return 1;
//            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            mGoodsList = new ArrayList<>();

            ViewHolder viewHolder = null;
            viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

//            holder.mPointTimeTv.setText(mDatas.get(position).getUpdated_at());
//            holder.mPointCountTv.setText(mDatas.get(position).getScore_count());
//            holder.mPointInfoTv.setText(mDatas.get(position).getInfo());

        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView mPointInfoTv, mPointTimeTv, mPointCountTv;

            public ViewHolder(View itemView) {
                super(itemView);
//                AutoUtils.autoSize(itemView);
//                mPointInfoTv = (TextView) itemView.findViewById(R.id.point_info_tv);
//                mPointTimeTv = (TextView) itemView.findViewById(R.id.point_time_tv);
//                mPointCountTv = (TextView) itemView.findViewById(R.id.point_count_tv);

            }
        }
    }
}
