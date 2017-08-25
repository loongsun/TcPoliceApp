package com.tc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tc.application.R;
import com.tc.bean.ImageListBean;
import com.tc.view.DateWheelDialogN;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.navisdk.BNaviModuleManager.getActivity;

/**
 * 作者：陈鹤 on 2017/8/25.
 * 版本：v1.0
 */

public class FormalNewActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.take_photo_tv)
    TextView takePhotoTv;
    @BindView(R.id.operate_next_tv)
    TextView operateNextTv;
    @BindView(R.id.photo_recycleview)
    RecyclerView photoRecycleview;
    @BindView(R.id.one_start_time)
    EditText oneStartTime;
    @BindView(R.id.one_ending_time)
    EditText oneEndingTime;
    @BindView(R.id.one_end_time)
    EditText oneEndTime;
    @BindView(R.id.one_save_time)
    EditText oneSaveTime;
    @BindView(R.id.three_get_time)
    EditText threeGetTime;
    @BindView(R.id.anjianname)
    EditText anjianname;
    @BindView(R.id.anjiannum)
    EditText anjiannum;
    private Button movieBtn, tvBtn, animeBtn, varietyBtn;
    private ScrollView mFragmentOne;
    private LinearLayout mFragmentTwo, mFragmentThree, mFragmentFour;
    private List<Button> btnList = new ArrayList<Button>();

    int num = 0;

    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;
    private Account mAccount;
    private int posId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formallnew);
        ButterKnife.bind(this);
        mImageList = new ArrayList<>();
        mAccount = Account.GetInstance();

        findById();
        setBackgroundColorById(R.id.movie_btn);
        initView();
    }

    private void initView() {

        String name = getIntent().getStringExtra("name");
        anjiannum.setText(name);


        anjianname.setText(getIntent().getStringExtra("anjianname"));

        photoRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new BackOrderAdapter(mImageList, getActivity());
//        mAdapter = new BackOrderAdapter(mAccount.getmImageList(),getActivity());

        photoRecycleview.setAdapter(mAdapter);
    }

    private void findById() {
        movieBtn = (Button) this.findViewById(R.id.movie_btn);
        tvBtn = (Button) this.findViewById(R.id.tv_btn);
        animeBtn = (Button) this.findViewById(R.id.anime_btn);
        varietyBtn = (Button) this.findViewById(R.id.variety_btn);

        mFragmentOne = (ScrollView) findViewById(R.id.fragment_one);
        mFragmentTwo = (LinearLayout) findViewById(R.id.fragment_two);
        mFragmentThree = (LinearLayout) findViewById(R.id.fragment_three);
        mFragmentFour = (LinearLayout) findViewById(R.id.fragment_four);


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
        posId = btnId;

        for (Button btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundColor(getResources().getColor(R.color.statemain));
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.backNoText));
            }
        }

        switch (btnId) {
            case R.id.movie_btn:
                mFragmentOne.setVisibility(View.VISIBLE);
                mFragmentTwo.setVisibility(View.GONE);
                mFragmentThree.setVisibility(View.GONE);
                mFragmentFour.setVisibility(View.GONE);
                break;

            case R.id.tv_btn:
                mFragmentOne.setVisibility(View.GONE);
                mFragmentTwo.setVisibility(View.VISIBLE);
                mFragmentThree.setVisibility(View.GONE);
                mFragmentFour.setVisibility(View.GONE);
                break;


            case R.id.anime_btn:
                mFragmentOne.setVisibility(View.GONE);
                mFragmentTwo.setVisibility(View.GONE);
                mFragmentThree.setVisibility(View.VISIBLE);
                mFragmentFour.setVisibility(View.GONE);
                break;


            case R.id.variety_btn:
                mFragmentOne.setVisibility(View.GONE);
                mFragmentTwo.setVisibility(View.GONE);
                mFragmentThree.setVisibility(View.GONE);
                mFragmentFour.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.movie_btn:
                setBackgroundColorById(R.id.movie_btn);
                break;

            case R.id.tv_btn:
                setBackgroundColorById(R.id.tv_btn);
                break;

            case R.id.anime_btn:
                setBackgroundColorById(R.id.anime_btn);
                break;

            case R.id.variety_btn:
                setBackgroundColorById(R.id.variety_btn);
                operateNextTv.setText("提交");
                break;


            default:
                break;
        }
    }

    @OnClick({R.id.take_photo_tv, R.id.operate_next_tv, R.id.one_start_time, R.id.one_ending_time,
            R.id.one_end_time, R.id.one_save_time, R.id.three_get_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_photo_tv:

                Intent cameraintent2 = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraintent2, 2);

                break;
            case R.id.operate_next_tv:

                switch (posId) {
                    case R.id.movie_btn:
                        setBackgroundColorById(R.id.tv_btn);
                        break;

                    case R.id.tv_btn:
                        setBackgroundColorById(R.id.anime_btn);
                        break;

                    case R.id.anime_btn:
                        setBackgroundColorById(R.id.variety_btn);
                        operateNextTv.setText("提交");
                        break;

                    case R.id.variety_btn:
//                        Toast.makeText(this, "保存", Toast.LENGTH_SHORT).show();
                        operateNextTv.setText("提交");
                        finish();
                        break;


                }
//                Toast.makeText(this, "下一步", Toast.LENGTH_SHORT).show();
                break;

            case R.id.one_start_time:
                DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneStartTime.setText(time);
                    }
                });
                kyDateChooseDialog3.setDateDialogTitle("案发时间");
                kyDateChooseDialog3.showDateChooseDialog();
                break;
            case R.id.one_ending_time:
                DateWheelDialogN kyDateChooseDialog4 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneEndingTime.setText(time);
                    }
                });
                kyDateChooseDialog4.setDateDialogTitle("结案时间");
                kyDateChooseDialog4.showDateChooseDialog();
                break;
            case R.id.one_end_time:
                DateWheelDialogN kyDateChooseDialog5 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneEndTime.setText(time);
                    }
                });
                kyDateChooseDialog5.setDateDialogTitle("终止时间");
                kyDateChooseDialog5.showDateChooseDialog();
                break;

            case R.id.one_save_time:
                DateWheelDialogN kyDateChooseDialog6 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneSaveTime.setText(time);
                    }
                });
                kyDateChooseDialog6.setDateDialogTitle("保护时间");
                kyDateChooseDialog6.showDateChooseDialog();
                break;

            case R.id.three_get_time:
                DateWheelDialogN kyDateChooseDialog7 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        threeGetTime.setText(time);
                    }
                });
                kyDateChooseDialog7.setDateDialogTitle("提取时间");
                kyDateChooseDialog7.showDateChooseDialog();
                break;

            default:
                break;
        }
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
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp" + num + ".jpg");
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
                Log.e("图片路径afterPhoto2", "" + photo);
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
                Log.e("图片路径afterPhoto1", "" + photo);

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
            Log.e("图片路径", mDatas.get(position).getImageUrl());


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
