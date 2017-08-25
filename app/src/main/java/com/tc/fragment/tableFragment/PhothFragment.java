package com.tc.fragment.tableFragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.activity.Account;
import com.tc.application.R;
import com.tc.bean.ImageListBean;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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

    int num = 0;

    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;
    private Account mAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, null);
        unbinder = ButterKnife.bind(this, view);
        mImageList = new ArrayList<>();
        mAccount = Account.GetInstance();
        initView();
        return view;


    }

    private void initView() {

        photoRecycleview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mAdapter = new BackOrderAdapter(mImageList,getActivity());
//        mAdapter = new BackOrderAdapter(mAccount.getmImageList(),getActivity());


        photoRecycleview.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.take_photo_tv)
    public void onViewClicked() {

        Intent cameraintent2 = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraintent2, 2);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                afterPhoto(data);
                break;
            case 2:// 当选择拍照时调用
                afterPhoto2(data);
                break;


        }
    }

    private void afterPhoto2(Intent data) {
        num++;
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp"+ num + ".jpg");
            if (photo == null) {
//                iv_2.setImageResource(R.drawable.icon_photo);
            } else {

//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    mOrderList.add(new onLineOrderBean(dataArray.getJSONObject(i)));
//
//               }

                mImageList.add(new ImageListBean(Environment.getExternalStorageDirectory() + "/" + "xczp" + num + ".jpg"));
                mAccount.setmImageList(mImageList);
                mAdapter.notifyDataSetChanged();

//                mImageList.add() = new ImageListBean();
                Log.e("图片路径afterPhoto2",""+photo);
//                iv_2.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void afterPhoto(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp1" + ".jpg");
            if (photo == null) {
//                iv_1.setImageResource(R.drawable.icon_photo);
            } else {
                Log.e("图片路径afterPhoto1",""+photo);

//                iv_1.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

//            return 1;
            return mDatas == null ? 0 : mDatas.size();
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

//            Picasso.with(mContext).load(mDatas.get(position).getImageUrl()).into(holder.Image);
            holder.Image.setImageURI(Uri.parse(mDatas.get(position).getImageUrl()));
            Log.e("图片路径",mDatas.get(position).getImageUrl());


        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView Image;

            public ViewHolder(View itemView) {
                super(itemView);

                Image = (ImageView) itemView.findViewById(R.id.item_image);
//                AutoUtils.autoSize(itemView);
            }
        }
    }
}
