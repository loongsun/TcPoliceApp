package com.tc.activity;

/**
 * Created by 123 on 2017/8/17.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tc.application.R;
import com.tc.client.FileUtil;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class AskRecordActivity extends Activity {
    // ģ���ļ���ַ
    private static String demoPath =  "";
    String SD = "";
    // �������ɵ��ļ���ַ
    private static String newPath = "";
    private Button btn,btns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        btns = (Button) findViewById(R.id.btns);

        String sdPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String path = sdPath + "/TC/wtxt";
        demoPath = path+"/xwbl.doc";
        newPath = path+"/xwbl_copy.doc";

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    InputStream inputStream = getAssets().open("xwbl.doc");
                    Toast.makeText(AskRecordActivity.this,demoPath+"=="+inputStream.available(),Toast.LENGTH_SHORT).show();
                    FileUtil.writeFile(new File(demoPath), inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                doScan();
            }
        });
        btns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(AskRecordActivity.this,WordHtmlActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void doScan(){
        //��ȡģ���ļ�
        File demoFile=new File(demoPath);
        //�������ɵ��ļ�
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$YEAR$", "2017");
        map.put("$ADDRESS", "�Ϻ���������xx·xx��");

        writeDoc(demoFile,newFile,map);
        //�鿴
        doOpenWord();
    }
    /**
     * �����ֻ��а�װ�Ŀɴ�word�����
     */
    private void doOpenWord(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            AskRecordActivity.this.startActivity(intent);
        } catch(ActivityNotFoundException e) {
            //��⵽ϵͳ��δ��װOliveOffice��apk����
            Toast.makeText(AskRecordActivity.this, "δ�ҵ����", Toast.LENGTH_LONG).show();
            //���ȵ�www.olivephone.com/e.apk���ز���װ
        }
    }
    /**
     * demoFile ģ���ļ�
     * newFile �����ļ�
     * map Ҫ��������
     * */
    public void writeDoc(File demoFile ,File newFile ,Map<String, String> map)
    {
        try
        {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // ��ȡword�ı�����
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // �滻�ı�����
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // ����ֽ���
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
