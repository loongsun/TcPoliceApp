package com.tc.activity.item;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
    private Button btn_save,btn_print,btn_add_ask;
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


        btn_add_ask = (Button) findViewById(R.id.btn_add_ask);
        btn_add_ask.setOnClickListener(new OnClick());

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
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    finish();
                    break;
                case R.id.btn_add_ask:
                    if(line == 11){
                        UtilTc.myToast(SenceNoteActivity.this,"最多可以添加十组问答！");
                    }else {
                        mLl_parent.addView(addView2(line));
                    }
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
//                    doOpenWord();
                    break;
            }
        }
    }
    private LinearLayout mLl_parent;
    private void showAll() {
        dateTimeDialog.hideOrShow();
    }
    EditText et_ask1,et_response1,
            et_ask2,et_response2,
            et_ask3,et_response3,
            et_ask4,et_response4,
            et_ask5,et_response5,
            et_ask6,et_response6,
            et_ask7,et_response7,
            et_ask8,et_response8,
            et_ask9,et_response9,
            et_ask10,et_response10

    ;
    int line = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bl);
        String  sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mLl_parent=(LinearLayout) findViewById(R.id.ll_parent);
        initWidgets();
        jqNum = getIntent().getStringExtra("name");
        file = new File(sdcardPath  + "/TC/wtxt/");
        if (!file.exists()){
            file.mkdir();
        }

//        demoPath = file.getPath()+ "/xwbl.doc";


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private View addView2(int size) {
        // TODO 动态添加布局(java方式)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//      LayoutInflater inflater1=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      LayoutInflater inflater2 = getLayoutInflater();
        LayoutInflater inflater3 = LayoutInflater.from(this);
        View view = inflater3.inflate(R.layout.block_gym_album_list_item, null);
        if(size ==1){
            et_ask1 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response1 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask1.setId(et_ask1.generateViewId());
            et_response1.setId(et_response1.generateViewId());
        }else  if(size ==2){
            et_ask2 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response2 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask2.setId(et_ask2.generateViewId());
            et_response2.setId(et_response2.generateViewId());
        }else  if(size ==3){
            et_ask3 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response3 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask3.setId(et_ask3.generateViewId());
            et_response3.setId(et_response3.generateViewId());
        }else  if(size ==4){
            et_ask4 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response4 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask4.setId(et_ask4.generateViewId());
            et_response4.setId(et_response4.generateViewId());
        }else  if(size ==5){
            et_ask5 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response5 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask5.setId(et_ask5.generateViewId());
            et_response5.setId(et_response5.generateViewId());
        }else  if(size ==6){
            et_ask6 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response6 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask6.setId(et_ask6.generateViewId());
            et_response6.setId(et_response6.generateViewId());
        }else  if(size ==7){
            et_ask7 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response7 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask7.setId(et_ask7.generateViewId());
            et_response7.setId(et_response7.generateViewId());
        }else  if(size ==8){
            et_ask8 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response8 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask8.setId(et_ask8.generateViewId());
            et_response8.setId(et_response8.generateViewId());
        }else  if(size ==9){
            et_ask9 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response9 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask9.setId(et_ask9.generateViewId());
            et_response9.setId(et_response9.generateViewId());
        }else  if(size ==10){
            et_ask10 = (EditText)view.findViewById(R.id.et_b_ask);
            et_response10 = (EditText)view.findViewById(R.id.et_b_response);
            et_ask10.setId(et_ask10.generateViewId());
            et_response10.setId(et_response10.generateViewId());
        }

        line++;
        view.setLayoutParams(lp);
        return view;
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

        if(et_ask1 != null && !et_ask1.getText().toString().equals("")) {
            map.put("$ASK1$", "问: "+et_ask1.getText().toString());
            map.put("$ANSER1$", "答: "+et_response1.getText().toString());
        }else{
            map.put("$ASK1$", "");
            map.put("$ANSER1$", "");
        }

        if(et_ask2 != null && !et_ask2.getText().toString().equals("")) {
            map.put("$ASK2$", "问: "+et_ask2.getText().toString());
            map.put("$ANSER2$", "答: "+et_response2.getText().toString());
        }else{
            map.put("$ASK2$", "");
            map.put("$ANSER2$", "");
        }
        if(et_ask3 != null && !et_ask3.getText().toString().equals("")) {
            map.put("$ASK3$", "问: "+et_ask3.getText().toString());
            map.put("$ANSER3$", "答: "+et_response3.getText().toString());
        }else{
            map.put("$ASK3$", "");
            map.put("$ANSER3$", "");
        }
        if(et_ask4 != null && !et_ask4.getText().toString().equals("")) {
            map.put("$ASK4$", "问: "+et_ask4.getText().toString());
            map.put("$ANSER4$", "答: "+et_response4.getText().toString());
        }else{
            map.put("$ASK4$", "");
            map.put("$ANSER4$", "");
        }

        if(et_ask5 != null && !et_ask5.getText().toString().equals("")) {
            map.put("$ASK5$", "问: "+et_ask5.getText().toString());
            map.put("$ANSER5$", "答: "+et_response5.getText().toString());
        }else{
            map.put("$ASK5$", "");
            map.put("$ANSER5$", "");
        }
        if(et_ask6 != null && !et_ask6.getText().toString().equals("")) {
            map.put("$ASK6$", "问: "+et_ask6.getText().toString());
            map.put("$ANSER6$", "答: "+et_response6.getText().toString());
        }else{
            map.put("$ASK6$", "");
            map.put("$ANSER6$", "");
        }

        if(et_ask7 != null && !et_ask7.getText().toString().equals("")) {
            map.put("$ASK7$", "问: "+et_ask7.getText().toString());
            map.put("$ANSER7$", "答: "+et_response7.getText().toString());
        }else{
            map.put("$ASK7$", "");
            map.put("$ANSER7$", "");
        }
        if(et_ask8 != null && !et_ask8.getText().toString().equals("")) {
            map.put("$ASK8$", "问: "+et_ask8.getText().toString());
            map.put("$ANSER8$", "答: "+et_response8.getText().toString());
        }else{
            map.put("$ASK8$", "");
            map.put("$ANSER8$", "");
        }
        if(et_ask9 != null && !et_ask9.getText().toString().equals("")) {
            map.put("$ASK9$", "问: "+et_ask9.getText().toString());
            map.put("$ANSER9$", "答: "+et_response9.getText().toString());
        }else{
            map.put("$ASK9$", "");
            map.put("$ANSER9$", "");
        }

        if(et_ask10 != null && !et_ask10.getText().toString().equals("")) {
            map.put("$ASK10$", "问: "+et_ask10.getText().toString());
            map.put("$ANSER10$", "答: "+et_response10.getText().toString());
        }else{
            map.put("$ASK10$", "");
            map.put("$ANSER10$", "");
        }


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
