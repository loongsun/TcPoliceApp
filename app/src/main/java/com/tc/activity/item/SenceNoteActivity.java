package com.tc.activity.item;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.tool.DateTimeDialog;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.application.R;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SenceNoteActivity extends Activity implements  DateTimeDialog.MyOnDateSetListener {
    private EditText et_ask_address, et_ask_username;
    private EditText et_ask_workadd, et_record_username,et_record_workadd;
    private EditText et_b_ask_phone,et_b_ask_card,et_b_ask_address,et_b_ask_home,et_b_ask_birth
            ,et_b_ask_username,et_b_ask_sex,et_b_ask_degree,et_b_ask_response;
    private Button btn_save,btn_print;
    private String jqNum;
    LinearLayout mouse_starttime,mouse_endtime,starttime,endtime;
    TextView et_mouse_ask_starttime,et_mouse_ask_endtime,et_ask_starttime,et_ask_endtime;
    private ImageView btn_blReturn;
    // 模板文集地址
//    private static String demoPath =  "";
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy年MM月dd日hh时mm分 aa");
    String SD = "";
    File file;
    private DateTimeDialog dateTimeDialog;
    int whichTimeSelect = 1;
    // 创建生成的文件地址
    private static String newPath = "";
    private void initWidgets() {
        et_ask_address = (EditText) findViewById(R.id.et_ask_address);
        et_ask_username = (EditText) findViewById(R.id.et_ask_username);

        et_ask_workadd = (EditText) findViewById(R.id.et_ask_workadd);
        et_record_username = (EditText) findViewById(R.id.et_record_username);
        et_record_workadd = (EditText) findViewById(R.id.et_record_workadd);

        et_b_ask_phone = (EditText) findViewById(R.id.et_b_ask_phone);
        et_b_ask_card = (EditText) findViewById(R.id.et_b_ask_card);
        et_b_ask_address = (EditText) findViewById(R.id.et_b_ask_address);
        et_b_ask_home = (EditText) findViewById(R.id.et_b_ask_home);
        et_b_ask_birth = (EditText) findViewById(R.id.et_b_ask_birth);
        et_b_ask_username = (EditText) findViewById(R.id.et_b_ask_username);
        et_b_ask_sex = (EditText) findViewById(R.id.et_b_ask_sex);
        et_b_ask_degree = (EditText) findViewById(R.id.et_b_ask_degree);

        et_b_ask_response = (EditText) findViewById(R.id.et_b_ask_response);


        mouse_starttime = (LinearLayout) findViewById(R.id.mouse_starttime);
        mouse_endtime = (LinearLayout) findViewById(R.id.mouse_endtime);
        starttime = (LinearLayout) findViewById(R.id.starttime);
        endtime = (LinearLayout) findViewById(R.id.endtime);


        et_mouse_ask_starttime = (TextView) findViewById(R.id.et_mouse_ask_starttime);
        et_mouse_ask_endtime = (TextView) findViewById(R.id.et_mouse_ask_endtime);
        et_ask_starttime = (TextView) findViewById(R.id.et_ask_starttime);
        et_ask_endtime = (TextView) findViewById(R.id.et_ask_endtime);

        dateTimeDialog = new DateTimeDialog(this, null, this);

        mouse_starttime.setOnClickListener(new OnClick());
        mouse_endtime.setOnClickListener(new OnClick());
        starttime.setOnClickListener(new OnClick());
        endtime.setOnClickListener(new OnClick());

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new OnClick());

        btn_print = (Button) findViewById(R.id.btn_print);
        btn_print.setOnClickListener(new OnClick());



        btn_blReturn = (ImageView) findViewById(R.id.btn_blReturn);
        btn_blReturn.setOnClickListener(new OnClick());

    }

    @Override
    public void onDateSet(Date date) {

        if(whichTimeSelect == 1){
            et_mouse_ask_starttime.setText(mFormatter.format(date) + "");
        }else if(whichTimeSelect == 2){
            et_mouse_ask_endtime.setText(mFormatter.format(date) + "");
        }else if(whichTimeSelect == 3){
            et_ask_starttime.setText(mFormatter.format(date) + "");
        }else if(whichTimeSelect == 4){
            et_ask_endtime.setText(mFormatter.format(date) + "");
        }
    }

    class OnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_save:
//                    saveContent();
//                    finish();
                    try {
                        String fileName = Values.PATH_BOOKMARK + jqNum + "_" + UtilTc.getCurrentTime() + ".doc";

                        newPath = fileName;
                        InputStream inputStream = getAssets().open("xwbl.doc");
//                        FileUtil.writeFile(new File(demoPath), inputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    doScan();
//                    finish();
                    break;

                case R.id.btn_blReturn:
                    finish();
                    break;
                case R.id.mouse_starttime:
                    whichTimeSelect = 1;
                    showAll();
                    break;
                case R.id.mouse_endtime:
                    whichTimeSelect = 2;
                    showAll();
                    break;
                case R.id.starttime:
                    whichTimeSelect = 3;
                    showAll();
                    break;
                case R.id.endtime:
                    whichTimeSelect = 4;
                    showAll();
                    break;
                case R.id.btn_print:
                    doOpenWord();
                    break;
            }
        }
    }

    private void showAll() {
        dateTimeDialog.hideOrShow();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bl);
        String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        initWidgets();
        jqNum = getIntent().getStringExtra("name");
        file = new File(sdcardPath  + "/TC/wtxt/");
        if (!file.exists()){
            file.mkdir();
        }

//        demoPath = file.getPath()+ "/xwbl.doc";


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

    private void doScan(){
        //获取模板文件
//        File demoFile=new File(demoPath);
        //创建生成的文件
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();

        map.put("$ADDRESS$", et_ask_address.getText().toString());
        map.put("$ASK_USERNAME$", et_ask_username.getText().toString());
        map.put("$ASK_WORKADD$", et_ask_workadd.getText().toString());
        map.put("$RECORD_USERNAME$", et_record_username.getText().toString());
        map.put("$RECORD_WORKADD$", et_record_workadd.getText().toString());
        map.put("$B_ASK_PHONE$", et_b_ask_phone.getText().toString());
        map.put("$B_ASK_CARD$", et_b_ask_card.getText().toString());
        map.put("$B_ASK_ADDRESS$", et_b_ask_address.getText().toString());
        map.put("$B_ASK_HOME$", et_b_ask_home.getText().toString());
        map.put("$BIRTH$", et_b_ask_birth.getText().toString());
        map.put("$B_ASK_USERNAME$", et_b_ask_username.getText().toString());
        map.put("$SEX$", et_b_ask_sex.getText().toString());
        map.put("$DEGREE$", et_b_ask_degree.getText().toString());
        map.put("$PHONE$", et_b_ask_phone.getText().toString());
        map.put("$RESPONSE$", et_b_ask_response.getText().toString());

        map.put("$MOUSE_END$", et_mouse_ask_endtime.getText().toString());
        map.put("$MOUSE_START$", et_mouse_ask_starttime.getText().toString());
        map.put("$START$", et_ask_starttime.getText().toString());
        map.put("$END$", et_ask_endtime.getText().toString());



        writeDoc(newFile,map);
        //查看
//        doOpenWord();
    }
    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            //检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(this, "未找到软件", Toast.LENGTH_LONG).show();
            //请先到www.olivephone.com/e.apk下载并安装
        }
    }
    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     * */
    public void writeDoc( File newFile ,Map<String, String> map)
    {
        try
        {
            InputStream in = getAssets().open("xwbl.doc");
//            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // 替换文本内容
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
