package com.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tc.application.R;

/**
 * Created by zhao on 17-8-20.
 */

public class MarkDialog extends Dialog {

    private final Context mContext;
    private EditText mEdtName;
    private EditText mEdtNote;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnClickHandler mOnClickHandler;

    public MarkDialog(Context context) {
        super(context, R.style.dialog);
//        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_mark_layout,null);
        setContentView(view);
        mEdtName = (EditText)view.findViewById(R.id.edt_name);
        mEdtNote = (EditText)view.findViewById(R.id.edt_note);
        mBtnCancel = (Button)view.findViewById(R.id.btn_cancel);
        mBtnOk = (Button)view.findViewById(R.id.btn_ok);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkDialog.this.dismiss();
                if(mOnClickHandler !=null){
                    mOnClickHandler.onCancel();
                }
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEdtName.getText().toString();
                String note = mEdtNote.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(note)){
                    Toast.makeText(mContext,"info can not be null",Toast.LENGTH_SHORT).show();
                }else{
                    MarkDialog.this.dismiss();
                    if(mOnClickHandler!=null){
                        mOnClickHandler.onOk(name,note);
                    }
                }

            }
        });
        this.show();
    }

    public void setOnClickHandler(OnClickHandler onClickHandler){
        this.mOnClickHandler = onClickHandler;
    }


    public interface OnClickHandler{
        void onCancel();
        void onOk(String name, String note);
    }


}
