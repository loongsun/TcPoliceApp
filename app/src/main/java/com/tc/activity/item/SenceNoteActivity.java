package com.tc.activity.item;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;

import java.io.File;
import java.io.IOException;

public class SenceNoteActivity extends Activity {
    private EditText et_ask_address, et_ask_username;
    private Button btn_save;
    private String jqNum;
    private ImageView btn_blReturn;

    private void initWidgets() {
        et_ask_address = (EditText) findViewById(R.id.et_ask_address);
        et_ask_username = (EditText) findViewById(R.id.et_ask_username);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new OnClick());
        btn_blReturn = (ImageView) findViewById(R.id.btn_blReturn);
        btn_blReturn.setOnClickListener(new OnClick());

    }

    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_save:
                    saveContent();
                    finish();
                    break;
                case R.id.btn_blReturn:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bl);
        initWidgets();
        jqNum = getIntent().getStringExtra("name");
    }

    private void saveContent() {
//        if (et_blTitle.getText().toString().trim().equals("") && et_blContent.getText().toString().trim().equals("")) {
//            UtilTc.myToast(getApplicationContext(), "标题或内容不能为空");
//        } else {
//            //保存内容 txt格式
//            writeBlToSdCard();
//        }
    }

    public void writeBlToSdCard() {
        String fileName = Values.PATH_BOOKMARK + jqNum + "_" + UtilTc.getCurrentTime() + ".txt";
        UtilTc.showLog("fileName" + fileName);
        File file = new File(fileName);
        if (!file.exists()) {
            Log.e("e", "文件不存在创建");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

        }
        try {
//            FileOutputStream fout = new FileOutputStream(fileName, true);
//            fout.write("标题:".getBytes());
//            fout.write(et_blTitle.getText().toString().getBytes());
//            fout.write("\n".getBytes());
//            fout.write("内容:".getBytes());
//            fout.write(et_blContent.getText().toString().getBytes());
//            fout.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
