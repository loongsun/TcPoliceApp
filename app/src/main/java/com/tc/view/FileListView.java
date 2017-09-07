package com.tc.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.activity.caseinfo.XsajHuaTuActivity;
import com.tc.application.R;
import com.tc.util.CaseUtil;
import com.tc.util.ConfirmDialog;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhao on 17-9-7.
 */

public class FileListView extends RelativeLayout {

    private static final String TAG = FileListView.class.getSimpleName();
    private final Context mContext;
    private ListView mListView;
    private ArrayList<String> mFileList;
    private FileListAdapter mFileAdapter;

    public FileListView(Context context) {
        this(context, null);
    }

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }


    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.file_list_view, this);
        mListView = (ListView) findViewById(R.id.listview_file);
    }

    public void setFileList(ArrayList<String> fileList) {
        if (fileList == null || fileList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        mFileList = fileList;
        mFileAdapter = new FileListAdapter();
        mListView.setAdapter(mFileAdapter);
        CaseUtil.setListViewHeight(mListView);
    }

    private class FileListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mFileList != null) {
                return mFileList.size();
            }
            return 0;

        }

        @Override
        public Object getItem(int position) {
            if (mFileList != null) {
                return mFileList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.e("e", "getView");
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bltxt, null);
                holder = new ViewHolder();
                holder.tv_blTitle = (TextView) convertView.findViewById(R.id.tv_blTitle);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                holder.iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);

                holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.lin_bl);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //word文件删除
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    final ConfirmDialog confirmDialog = new ConfirmDialog(mContext, "确定要删除吗?", "删除", "取消");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();

                            String filename = mFileList.get(position);
                            File file = new File(filename);

                            if (file.exists()) {
                                boolean isDel = file.delete();
                                if (isDel) {
                                    mFileList.remove(position);
                                    notifyDataSetChanged();
                                    CaseUtil.setListViewHeight(mListView);
                                }
                            }
                        }

                        @Override
                        public void doCancel() {
                            // TODO Auto-generated method stub
                            confirmDialog.dismiss();
                        }
                    });


                }
            });

            //word文件编辑
            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String filename = mFileList.get(position);
                    CaseUtil.doOpenWord(filename, mContext);
                }
            });


            String ret = mFileList.get(position);
            Log.i(TAG, "file name = " + ret);
            if (!TextUtils.isEmpty(ret)) {
                String fileName = ret.substring(ret.lastIndexOf("/"));
                holder.tv_blTitle.setText(fileName);
            }
            return convertView;
        }

    }

    private class ViewHolder {

        TextView tv_blTitle;
        LinearLayout parentLayout;
        ImageView iv_delete, iv_edit;
    }
}
