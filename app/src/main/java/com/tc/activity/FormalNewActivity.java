package com.tc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdses.bean.PoliceStateListBean;
import com.sdses.tool.UtilTc;
import com.sdses.tool.Values;
import com.tc.app.TcApp;
import com.tc.application.R;
import com.tc.bean.ImageListBean;
import com.tc.view.CustomProgressDialog;
import com.tc.view.DateWheelDialogN;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import static com.baidu.navisdk.BNaviModuleManager.getActivity;
import static com.tc.application.R.id.one_save_time;

/**
 * ���ߣ��º� on 2017/8/25.
 * �汾��v1.0
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
    @BindView(one_save_time)
    EditText oneSaveTime;
    @BindView(R.id.TQSJ_edit)
    EditText threeGetTime;
    @BindView(R.id.anjianname)
    EditText anjianname;
    @BindView(R.id.anjiannum)
    EditText anjiannum;
    private Button movieBtn, tvBtn, animeBtn, varietyBtn;
    private ScrollView mFragmentOne;
    private LinearLayout mFragmentTwo, mFragmentThree, mFragmentFour;
    private List<Button> btnList = new ArrayList<Button>();

    private String newPath = "";
    private String name = "";

    //������Ϣ
    private String checkboxstr1, checkboxstr2, checkboxstr3, checkboxstr4;
    private String AJLB = null, SFMA = null, SFXA = null, BHCS = null, XCTJ = null, TQZK = null, GZTJ = null;
    private RadioGroup anjianleibie_radio_group;
    private RadioButton mAJLBRadio1, mAJLBRadio2, mAJLBRadio3, mAJLBRadio4, mAJLBRadio5, mAJLBRadio6,
            mAJLBRadio7, mAJLBRadio8, mAJLBRadio9, mAJLBRadio10, mAJLBRadio11, mAJLBRadio12, mAJLBRadio13;
    private RadioGroup shifoumingan_radio;
    private RadioButton mSFMARadio1, mSFMARadio2;

    private RadioGroup shifouxingan_Radio;
    private RadioButton mSFXARadio1, mSFXARadio2;

    private RadioGroup baohucuoshi_radio;
    private CheckBox mBHCSChenkbox1, mBHCSChenkbox2, mBHCSChenkbox3, mBHCSChenkbox4;

    private RadioGroup xianchangtiaojian_radio;
    private RadioButton mXCTJRadio1, mXCTJRadio2, mXCTJRadio3;

    private RadioGroup tianqiqingkuang_radio;
    private RadioButton mTQQKRadio1, mTQQKRadio2, mTQQKRadio3, mTQQKRadio4, mTQQKRadio5;

    private RadioGroup guangzhaotiaojian_radio;
    private RadioButton mGZTJRadio1, mGZTJRadio2, mGZTJRadio3;


    private EditText AFQY_edit, AFDD_edit, anfaquhua_edit, KTDD_edit, KYSY_edit, AFGC_edit, baohuren_name_edit,
            baohuren_company_edit, XCZH_edit, et_kyKydw, et_kyZpbgdw, xianchangyiliuwu_edit, kanyanqingkuang_edit,
            baoanren_edit, sunshiwupin_edit, shangwangqingkuang_edit, jianzhenren_edit;

    //�ۼ���֤
    private EditText A_ID_edit, MC_edit, JBTZ_edit, SL_edit, TQBW_edit, TQFF_edit, TQR_edit, BZ_edit,
            JZR_edit, TQSJ_edit, CBYJ_edit, GZJY_edit, XCFXDW_edit, XCFXR_edit;

    //�������
    private EditText ZARS_edit, ZADD_edit, ZAGJ_edit, ZAGC_edit;
    String XCFXYJCL = null, AJXZ = null, XZDX = null, XZCS = null, ZASJ = null, ZARK = null, ZACK = null, ZASD = null, QRFS = null, ZATD = null, ZADJMD = null;
    RadioButton mXCFXRadio1, mXCFXRadio2, mXCFXRadio3, mXCFXRadio4;
    RadioGroup radio_xianchangfenxi_group;

    RadioButton mAJXZRadio1, mAJXZRadio2, mAJXZRadio3, mAJXZRadio4, mAJXZRadio5, mAJXZRadio6, mAJXZRadio7, mAJXZRadio8, mAJXZRadio9, mAJXZRadio10,
            mAJXZRadio11, mAJXZRadio12, mAJXZRadio13, mAJXZRadio14;
    RadioGroup anjianxingzhi_radio_group;

    RadioGroup xuanzeduixinag_radio_group;
    RadioButton mXZDXRadio1, mXZDXRadio2, mXZDXRadio3, mXZDXRadio4;

    RadioGroup xuanzechusuo_radio_group;
    RadioButton mXZCSRadio1, mXZCSRadio2, mXZCSRadio3, mXZCSRadio4, mXZCSRadio5, mXZCSRadio6, mXZCSRadio7,
            mXZCSRadio8, mXZCSRadio9, mXZCSRadio10;

    RadioGroup zuoanshiji_radio_group;
    RadioButton mZASJRadio1, mZASJRadio2, mZASJRadio3, mZASJRadio4, mZASJRadio5, mZASJRadio6, mZASJRadio7;

    RadioGroup zuoanrukou_radio_group;
    RadioButton mZARKRadio1, mZARKRadio2, mZARKRadio3, mZARKRadio4;

    RadioGroup zuoanshouduan_radio_group;
    RadioButton mZASDRadio1, mZASDRadio2, mZASDRadio3, mZASDRadio4, mZASDRadio5, mZASDRadio6, mZASDRadio7, mZASDRadio8, mZASDRadio9;

    RadioGroup ruqinfangshi_radio_group;
    RadioButton mRQFSRadio1, mRQFSRadio2, mRQFSRadio3, mRQFSRadio4, mRQFSRadio5, mRQFSRadio6, mRQFSRadio7, mRQFSRadio8, mRQFSRadio9;

    RadioGroup zuoantedian_radio_group;
    RadioButton mZATDRadio1, mZATDRadio2, mZATDRadio3;

    RadioGroup zuoanmudi_radio_group;
    RadioButton mZAMDRadio1, mZAMDRadio2, mZAMDRadio3, mZAMDRadio4, mZAMDRadio5, mZAMDRadio6;


    int num = 0;

    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;
    private Account mAccount;
    private int posId;
    TcApp ia;


    private final static int UPLOAD = 1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;

    // ���ȿ�
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case UPLOAD:
                    progressDialog.setMessage("�����ϴ���Ϣ,���Ժ�");
                    break;
            }
        }
        progressDialog.show();
    }

    // ȡ�����ȿ�
    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formallnew);
        ButterKnife.bind(this);
        mImageList = new ArrayList<>();
        mAccount = Account.GetInstance();
        ia = (TcApp) TcApp.mContent;
        findById();
        setBackgroundColorById(R.id.movie_btn);
        initView();
        initfindView();
    }

    private void initfindView() {


        A_ID_edit = (EditText) this.findViewById(R.id.anjianname);
//�ۼ���֤
        MC_edit = (EditText) this.findViewById(R.id.MC_edit);
        JBTZ_edit = (EditText) this.findViewById(R.id.JBTZ_edit);
        SL_edit = (EditText) this.findViewById(R.id.SL_edit);
        TQBW_edit = (EditText) this.findViewById(R.id.TQBW_edit);
        TQFF_edit = (EditText) this.findViewById(R.id.TQFF_edit);
        TQR_edit = (EditText) this.findViewById(R.id.TQR_edit);
        BZ_edit = (EditText) this.findViewById(R.id.TQR_edit);
        JZR_edit = (EditText) this.findViewById(R.id.JZR_edit);
        TQSJ_edit = (EditText) this.findViewById(R.id.TQSJ_edit);
        CBYJ_edit = (EditText) this.findViewById(R.id.CBYJ_edit);
        GZJY_edit = (EditText) this.findViewById(R.id.GZJY_edit);
        XCFXDW_edit = (EditText) this.findViewById(R.id.XCFXDW_edit);
        XCFXR_edit = (EditText) this.findViewById(R.id.XCFXR_edit);

//�������
        ZARS_edit = findViewById(R.id.ZARS_edit);
        ZADD_edit = findViewById(R.id.ZADD_edit);
        ZAGJ_edit = findViewById(R.id.ZAGJ_edit);
        ZAGC_edit = findViewById(R.id.ZAGC_edit);

        radio_xianchangfenxi_group = findViewById(R.id.radio_xianchangfenxi_group);
        mXCFXRadio1 = findViewById(R.id.radio_xianchangfenxi_1);
        mXCFXRadio2 = findViewById(R.id.radio_xianchangfenxi_2);
        mXCFXRadio3 = findViewById(R.id.radio_xianchangfenxi_3);
        mXCFXRadio4 = findViewById(R.id.radio_xianchangfenxi_4);
        radio_xianchangfenxi_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "ʵ�ؿ���";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "�ֳ�����";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "�������";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "�������";
                }
            }
        });

        anjianxingzhi_radio_group = findViewById(R.id.anjianxingzhi_radio_group);
        mAJXZRadio1 = findViewById(R.id.anjianxingzhi_radiobutton_1);
        mAJXZRadio2 = findViewById(R.id.anjianxingzhi_radiobutton_2);
        mAJXZRadio3 = findViewById(R.id.anjianxingzhi_radiobutton_3);
        mAJXZRadio4 = findViewById(R.id.anjianxingzhi_radiobutton_4);
        mAJXZRadio5 = findViewById(R.id.anjianxingzhi_radiobutton_5);
        mAJXZRadio6 = findViewById(R.id.anjianxingzhi_radiobutton_6);
        mAJXZRadio7 = findViewById(R.id.anjianxingzhi_radiobutton_7);
        mAJXZRadio8 = findViewById(R.id.anjianxingzhi_radiobutton_8);
        mAJXZRadio9 = findViewById(R.id.anjianxingzhi_radiobutton_9);
        mAJXZRadio10 = findViewById(R.id.anjianxingzhi_radiobutton_10);
        mAJXZRadio11 = findViewById(R.id.anjianxingzhi_radiobutton_11);
        mAJXZRadio12 = findViewById(R.id.anjianxingzhi_radiobutton_12);
        mAJXZRadio13 = findViewById(R.id.anjianxingzhi_radiobutton_13);
        mAJXZRadio14 = findViewById(R.id.anjianxingzhi_radiobutton_14);
        anjianxingzhi_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mAJXZRadio1.getId()) {
                    AJXZ = "����թƭ��";
                } else if (checkedId == mAJXZRadio2.getId()) {
                    AJXZ = "����թƭ��������";
                } else if (checkedId == mAJXZRadio3.getId()) {
                    AJXZ = "��Ƭ���ҵ��԰���";
                } else if (checkedId == mAJXZRadio4.getId()) {
                    AJXZ = "�������г�����";
                } else if (checkedId == mAJXZRadio5.getId()) {
                    AJXZ = "��·����";
                } else if (checkedId == mAJXZRadio6.getId()) {
                    AJXZ = "�����˺�����";
                } else if (checkedId == mAJXZRadio7.getId()) {
                    AJXZ = "�������ٰ�";
                } else if (checkedId == mAJXZRadio8.getId()) {
                    AJXZ = "ì�ܼ���";
                } else if (checkedId == mAJXZRadio9.getId()) {
                    AJXZ = "�������԰�";
                } else if (checkedId == mAJXZRadio10.getId()) {
                    AJXZ = "���Գ�����Ʒ";
                } else if (checkedId == mAJXZRadio11.getId()) {
                    AJXZ = "���Ա��ݰ���";
                } else if (checkedId == mAJXZRadio12.getId()) {
                    AJXZ = "�����Կ�������";
                } else if (checkedId == mAJXZRadio13.getId()) {
                    AJXZ = "�������ҵ���";
                } else if (checkedId == mAJXZRadio14.getId()) {
                    AJXZ = "�˷��������ҵ���";
                }
            }
        });

        xuanzeduixinag_radio_group = findViewById(R.id.xuanzeduixinag_radio_group);
        mXZDXRadio1 = findViewById(R.id.xuanzeduixiang_radio_button1);
        mXZDXRadio2 = findViewById(R.id.xuanzeduixiang_radio_button2);
        mXZDXRadio3 = findViewById(R.id.xuanzeduixiang_radio_button3);
        mXZDXRadio4 = findViewById(R.id.xuanzeduixiang_radio_button4);
        xuanzeduixinag_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mXZDXRadio1.getId()) {
                    XZDX = "��Ů";
                } else if (checkedId == mXZDXRadio2.getId()) {
                    XZDX = "���";
                } else if (checkedId == mXZDXRadio3.getId()) {
                    XZDX = "������";
                } else if (checkedId == mXZDXRadio4.getId()) {
                    XZDX = "����";
                }
            }
        });

        xuanzechusuo_radio_group = findViewById(R.id.xuanzechusuo_radio_group);
        mXZCSRadio1 = findViewById(R.id.xuanzechusuo_radio_button1);
        mXZCSRadio2 = findViewById(R.id.xuanzechusuo_radio_button2);
        mXZCSRadio3 = findViewById(R.id.xuanzechusuo_radio_button3);
        mXZCSRadio4 = findViewById(R.id.xuanzechusuo_radio_button4);
        mXZCSRadio5 = findViewById(R.id.xuanzechusuo_radio_button5);
        mXZCSRadio6 = findViewById(R.id.xuanzechusuo_radio_button6);
        mXZCSRadio7 = findViewById(R.id.xuanzechusuo_radio_button7);
        mXZCSRadio8 = findViewById(R.id.xuanzechusuo_radio_button8);
        mXZCSRadio9 = findViewById(R.id.xuanzechusuo_radio_button9);
        mXZCSRadio10 = findViewById(R.id.xuanzechusuo_radio_button10);

        xuanzechusuo_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "����";
                } else if (checkedId == mXZCSRadio2.getId()) {
                    XZCS = "¥��";
                } else if (checkedId == mXZCSRadio3.getId()) {
                    XZCS = "����������";
                } else if (checkedId == mXZCSRadio4.getId()) {
                    XZCS = "����С��";
                } else if (checkedId == mXZCSRadio5.getId()) {
                    XZCS = "����";
                } else if (checkedId == mXZCSRadio6.getId()) {
                    XZCS = "�߲�¥��";
                } else if (checkedId == mXZCSRadio7.getId()) {
                    XZCS = "���������ض�";
                } else if (checkedId == mXZCSRadio8.getId()) {
                    XZCS = "��ҵ��";
                } else if (checkedId == mXZCSRadio9.getId()) {
                    XZCS = "��ͨ¥��";
                } else if (checkedId == mXZCSRadio10.getId()) {
                    XZCS = "·��";
                }
            }
        });

        zuoanshiji_radio_group = findViewById(R.id.zuoanshiji_radio_group);
        mZASJRadio1 = findViewById(R.id.zuoanshiji_radio_button1);
        mZASJRadio2 = findViewById(R.id.zuoanshiji_radio_button2);
        mZASJRadio3 = findViewById(R.id.zuoanshiji_radio_button3);
        mZASJRadio4 = findViewById(R.id.zuoanshiji_radio_button4);
        mZASJRadio5 = findViewById(R.id.zuoanshiji_radio_button5);
        mZASJRadio6 = findViewById(R.id.zuoanshiji_radio_button6);
        mZASJRadio7 = findViewById(R.id.zuoanshiji_radio_button7);

        zuoanshiji_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mZASJRadio1.getId()) {
                    ZASJ = "ѩ��";
                } else if (checkedId == mZASJRadio2.getId()) {
                    ZASJ = "����";
                } else if (checkedId == mZASJRadio3.getId()) {
                    ZASJ = "����";
                } else if (checkedId == mZASJRadio4.getId()) {
                    ZASJ = "����";
                } else if (checkedId == mZASJRadio5.getId()) {
                    ZASJ = "��";
                } else if (checkedId == mZASJRadio6.getId()) {
                    ZASJ = "����";
                } else if (checkedId == mZASJRadio7.getId()) {
                    ZASJ = "��";
                }
            }
        });

        zuoanrukou_radio_group = findViewById(R.id.zuoanrukou_radio_group);
        mZARKRadio1 = findViewById(R.id.zuoanrukou_radio_button1);
        mZARKRadio2 = findViewById(R.id.zuoanrukou_radio_button2);
        mZARKRadio3 = findViewById(R.id.zuoanrukou_radio_button3);
        mZARKRadio4 = findViewById(R.id.zuoanrukou_radio_button4);

        zuoanrukou_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mZARKRadio1.getId()) {
                    ZARK = "����";
                } else if (checkedId == mZARKRadio2.getId()) {
                    ZARK = "��̨";
                } else if (checkedId == mZARKRadio3.getId()) {
                    ZARK = "��";
                } else if (checkedId == mZARKRadio4.getId()) {
                    ZARK = "��";
                }
            }
        });

        zuoanshouduan_radio_group = findViewById(R.id.zuoanshouduan_radio_group);
        mZASDRadio1 = findViewById(R.id.zuoanshouduan_radio_button1);
        mZASDRadio2 = findViewById(R.id.zuoanshouduan_radio_button2);
        mZASDRadio3 = findViewById(R.id.zuoanshouduan_radio_button3);
        mZASDRadio4 = findViewById(R.id.zuoanshouduan_radio_button4);
        mZASDRadio5 = findViewById(R.id.zuoanshouduan_radio_button5);
        mZASDRadio6 = findViewById(R.id.zuoanshouduan_radio_button6);
        mZASDRadio7 = findViewById(R.id.zuoanshouduan_radio_button7);
        mZASDRadio8 = findViewById(R.id.zuoanshouduan_radio_button8);
        mZASDRadio9 = findViewById(R.id.zuoanshouduan_radio_button9);

        zuoanshouduan_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mZASDRadio1.getId()) {
                    ZASD = "�ֶ���";
                } else if (checkedId == mZASDRadio2.getId()) {
                    ZASD = "�ߴ�";
                } else if (checkedId == mZASDRadio3.getId()) {
                    ZASD = "���";
                } else if (checkedId == mZASDRadio4.getId()) {
                    ZASD = "�Ͱ�";
                } else if (checkedId == mZASDRadio5.getId()) {
                    ZASD = "Կ�׿���";
                } else if (checkedId == mZASDRadio6.getId()) {
                    ZASD = "�þ߹���";
                } else if (checkedId == mZASDRadio7.getId()) {
                    ZASD = "��Ƭ����";
                } else if (checkedId == mZASDRadio8.getId()) {
                    ZASD = "˳��ǣ��";
                } else if (checkedId == mZASDRadio9.getId()) {
                    ZASD = "���������ֶ�";
                }
            }
        });

        ruqinfangshi_radio_group = findViewById(R.id.ruqinfangshi_radio_group);
        mRQFSRadio1 = findViewById(R.id.ruqinfangshi_radio_button1);
        mRQFSRadio2 = findViewById(R.id.ruqinfangshi_radio_button2);
        mRQFSRadio3 = findViewById(R.id.ruqinfangshi_radio_button3);
        mRQFSRadio4 = findViewById(R.id.ruqinfangshi_radio_button4);
        mRQFSRadio5 = findViewById(R.id.ruqinfangshi_radio_button5);
        mRQFSRadio6 = findViewById(R.id.ruqinfangshi_radio_button6);
        mRQFSRadio7 = findViewById(R.id.ruqinfangshi_radio_button7);
        mRQFSRadio8 = findViewById(R.id.ruqinfangshi_radio_button8);
        mRQFSRadio9 = findViewById(R.id.ruqinfangshi_radio_button9);

        ruqinfangshi_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mRQFSRadio1.getId()) {
                    QRFS = "��ƽ�����ֳ�";
                } else if (checkedId == mRQFSRadio2.getId()) {
                    QRFS = "��ƽ�����ֳ�";
                } else if (checkedId == mRQFSRadio3.getId()) {
                    QRFS = "�Ҵ�����";
                } else if (checkedId == mRQFSRadio4.getId()) {
                    QRFS = "�ǿ�����";
                } else if (checkedId == mRQFSRadio5.getId()) {
                    QRFS = "�ƻ�����";
                } else if (checkedId == mRQFSRadio6.getId()) {
                    QRFS = "�ƻ�����";
                } else if (checkedId == mRQFSRadio7.getId()) {
                    QRFS = "�ƻ�����";
                } else if (checkedId == mRQFSRadio8.getId()) {
                    QRFS = "������̨";
                } else if (checkedId == mRQFSRadio9.getId()) {
                    QRFS = "����";
                }
            }
        });

        zuoantedian_radio_group = findViewById(R.id.zuoantedian_radio_group);
        mZATDRadio1 = findViewById(R.id.zuoantedian_radio_button1);
        mZATDRadio2 = findViewById(R.id.zuoantedian_radio_button2);
        mZATDRadio3 = findViewById(R.id.zuoantedian_radio_button3);

        zuoantedian_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mZATDRadio1.getId()) {
                    ZATD = "�ظ�����";
                } else if (checkedId == mZATDRadio2.getId()) {
                    ZATD = "�Ż�����";
                } else if (checkedId == mZATDRadio3.getId()) {
                    ZATD = "��������";
                }
            }
        });

        zuoanmudi_radio_group = findViewById(R.id.zuoanmudi_radio_group);
        mZAMDRadio1 = findViewById(R.id.zuoanmudi_radio_button1);
        mZAMDRadio2 = findViewById(R.id.zuoanmudi_radio_button2);
        mZAMDRadio3 = findViewById(R.id.zuoanmudi_radio_button3);
        mZAMDRadio4 = findViewById(R.id.zuoanmudi_radio_button4);
        mZAMDRadio5 = findViewById(R.id.zuoanmudi_radio_button5);
        mZAMDRadio6 = findViewById(R.id.zuoanmudi_radio_button6);

        zuoanmudi_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mZAMDRadio1.getId()) {
                    ZADJMD = "����";
                } else if (checkedId == mZAMDRadio2.getId()) {
                    ZADJMD = "����";
                } else if (checkedId == mZAMDRadio3.getId()) {
                    ZADJMD = "����";
                } else if (checkedId == mZAMDRadio4.getId()) {
                    ZADJMD = "ǿ��";
                } else if (checkedId == mZAMDRadio5.getId()) {
                    ZADJMD = "����";
                } else if (checkedId == mZAMDRadio6.getId()) {
                    ZADJMD = "й˽��";
                }
            }
        });

//������Ϣ
        mBHCSChenkbox1 = findViewById(R.id.baohucuoshi_checkbox1);
        mBHCSChenkbox2 = findViewById(R.id.baohucuoshi_checkbox2);
        mBHCSChenkbox3 = findViewById(R.id.baohucuoshi_checkbox3);
        mBHCSChenkbox4 = findViewById(R.id.baohucuoshi_checkbox4);

        anjianleibie_radio_group = findViewById(R.id.anjianleibie_radio_group);
        mAJLBRadio1 = findViewById(R.id.anjianleibie_radio_button1);
        mAJLBRadio2 = findViewById(R.id.anjianleibie_radio_button2);
        mAJLBRadio3 = findViewById(R.id.anjianleibie_radio_button3);
        mAJLBRadio4 = findViewById(R.id.anjianleibie_radio_button4);
        mAJLBRadio5 = findViewById(R.id.anjianleibie_radio_button5);
        mAJLBRadio6 = findViewById(R.id.anjianleibie_radio_button6);
        mAJLBRadio7 = findViewById(R.id.anjianleibie_radio_button7);
        mAJLBRadio8 = findViewById(R.id.anjianleibie_radio_button8);
        mAJLBRadio9 = findViewById(R.id.anjianleibie_radio_button9);
        mAJLBRadio10 = findViewById(R.id.anjianleibie_radio_button10);
        mAJLBRadio11 = findViewById(R.id.anjianleibie_radio_button11);
        mAJLBRadio12 = findViewById(R.id.anjianleibie_radio_button12);
        mAJLBRadio13 = findViewById(R.id.anjianleibie_radio_button13);

        anjianleibie_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {


                if (checkedId == mAJLBRadio1.getId()) {
                    AJLB = "թƭ";
                } else if (checkedId == mAJLBRadio2.getId()) {
                    AJLB = "��թ����";
                } else if (checkedId == mAJLBRadio3.getId()) {
                    AJLB = "����·�ư�";
                } else if (checkedId == mAJLBRadio4.getId()) {
                    AJLB = "�Ż𰸼�";
                } else if (checkedId == mAJLBRadio5.getId()) {
                    AJLB = "���Ե綯�ԳƳ�";
                } else if (checkedId == mAJLBRadio6.getId()) {
                    AJLB = "���Գ��ڲ���";
                } else if (checkedId == mAJLBRadio7.getId()) {
                    AJLB = "�������";
                } else if (checkedId == mAJLBRadio8.getId()) {
                    AJLB = "ǿ��";
                } else if (checkedId == mAJLBRadio9.getId()) {
                    AJLB = "���԰�";
                } else if (checkedId == mAJLBRadio10.getId()) {
                    AJLB = "�뻧����";
                } else if (checkedId == mAJLBRadio11.getId()) {
                    AJLB = "�����˺���";
                } else if (checkedId == mAJLBRadio12.getId()) {
                    AJLB = "�������԰���";
                } else if (checkedId == mAJLBRadio13.getId()) {
                    AJLB = "���ҵ��԰�";
                }
            }
        });
//
        shifoumingan_radio = findViewById(R.id.shifoumingan_radio);
        mSFMARadio1 = findViewById(R.id.shifoumingan_radio_button1);
        mSFMARadio2 = findViewById(R.id.shifoumingan_radio_button2);
        shifoumingan_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mSFMARadio1.getId()) {
                    SFMA = "��";
                } else if (checkedId == mSFMARadio2.getId()) {
                    SFMA = "��";
                }
            }
        });

        shifouxingan_Radio = findViewById(R.id.shifouxingan_Radio);
        mSFXARadio1 = findViewById(R.id.shifouxingan_radio_button1);
        mSFXARadio2 = findViewById(R.id.shifouxingan_radio_button2);
        shifouxingan_Radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mSFXARadio1.getId()) {
                    SFXA = "��";
                } else if (checkedId == mSFXARadio2.getId()) {
                    SFXA = "��";
                }
            }
        });
//
        baohucuoshi_radio = findViewById(R.id.baohucuoshi_radio);
        mBHCSChenkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkboxstr1 = "�����������������������";
                } else {
                    checkboxstr1 = "";
                }
            }
        });

        mBHCSChenkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkboxstr2 = "ר�˿����ֳ�����ֹ���˽���";
                } else {
                    checkboxstr2 = "";
                }
            }
        });

        mBHCSChenkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkboxstr3 = "���������б���";
                } else {
                    checkboxstr3 = "";
                }
            }
        });

        mBHCSChenkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkboxstr4 = "������ʩ";
                } else {
                    checkboxstr4 = "";
                }
            }
        });

        xianchangtiaojian_radio = findViewById(R.id.xianchangtiaojian_radio);
        mXCTJRadio1 = findViewById(R.id.xianchangtiaojian_radio_button1);
        mXCTJRadio2 = findViewById(R.id.xianchangtiaojian_radio_button2);
        mXCTJRadio3 = findViewById(R.id.xianchangtiaojian_radio_button3);

        xianchangtiaojian_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mXCTJRadio1.getId()) {
                    XCTJ = "ԭʼ�ֳ�";
                } else if (checkedId == mXCTJRadio2.getId()) {
                    XCTJ = "�䶯�ֳ�";
                } else if (checkedId == mXCTJRadio3.getId()) {
                    XCTJ = "��ȷ���ֳ�";
                }
            }
        });

        tianqiqingkuang_radio = findViewById(R.id.tianqiqingkuang_radio);
        mTQQKRadio1 = findViewById(R.id.tianqiqingkuang_radio_button1);
        mTQQKRadio2 = findViewById(R.id.tianqiqingkuang_radio_button2);
        mTQQKRadio3 = findViewById(R.id.tianqiqingkuang_radio_button3);
        mTQQKRadio4 = findViewById(R.id.tianqiqingkuang_radio_button4);
        mTQQKRadio5 = findViewById(R.id.tianqiqingkuang_radio_button5);

        tianqiqingkuang_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (checkedId == mTQQKRadio1.getId()) {
                    TQZK = "��";
                } else if (checkedId == mTQQKRadio2.getId()) {
                    TQZK = "��";
                } else if (checkedId == mTQQKRadio3.getId()) {
                    TQZK = "��";
                } else if (checkedId == mTQQKRadio4.getId()) {
                    TQZK = "��";
                } else if (checkedId == mTQQKRadio5.getId()) {
                    TQZK = "ѩ";
                }
            }
        });

        guangzhaotiaojian_radio = findViewById(R.id.guangzhaotiaojian_radio);
        mGZTJRadio1 = findViewById(R.id.guangzhaotiaojian_radio_button1);
        mGZTJRadio2 = findViewById(R.id.guangzhaotiaojian_radio_button2);
        mGZTJRadio3 = findViewById(R.id.guangzhaotiaojian_radio_button3);

        guangzhaotiaojian_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == mGZTJRadio1.getId()) {
                    GZTJ = "��Ȼ��";
                } else if (checkedId == mGZTJRadio2.getId()) {
                    GZTJ = "�ƹ�";
                } else if (checkedId == mGZTJRadio3.getId()) {
                    GZTJ = "���ֹ�";
                }
            }
        });

        anfaquhua_edit = findViewById(R.id.anfaquhua_edit);
        AFDD_edit = findViewById(R.id.AFDD_edit);
        KTDD_edit = findViewById(R.id.KTDD_edit);
        KYSY_edit = findViewById(R.id.KYSY_edit);
        AFGC_edit = findViewById(R.id.AFGC_edit);
        baohuren_name_edit = findViewById(R.id.baohuren_name_edit);
        baohuren_company_edit = findViewById(R.id.baohuren_company_edit);
        XCZH_edit = findViewById(R.id.XCZH_edit);
        et_kyKydw = findViewById(R.id.et_kyKydw);
        et_kyZpbgdw = findViewById(R.id.et_kyZpbgdw);
        xianchangyiliuwu_edit = findViewById(R.id.xianchangyiliuwu_edit);
        kanyanqingkuang_edit = findViewById(R.id.kanyanqingkuang_edit);
        baoanren_edit = findViewById(R.id.baoanren_edit);
        sunshiwupin_edit = findViewById(R.id.sunshiwupin_edit);
        shangwangqingkuang_edit = findViewById(R.id.shangwangqingkuang_edit);
        jianzhenren_edit = findViewById(R.id.jianzhenren_edit);


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
                operateNextTv.setText("��һ��");
                break;

            case R.id.tv_btn:
                setBackgroundColorById(R.id.tv_btn);
                operateNextTv.setText("��һ��");
                break;

            case R.id.anime_btn:
                setBackgroundColorById(R.id.anime_btn);
                operateNextTv.setText("��һ��");
                break;

            case R.id.variety_btn:
                setBackgroundColorById(R.id.variety_btn);
                operateNextTv.setText("�ύ");
                break;


            default:
                break;
        }
    }

    @OnClick({R.id.take_photo_tv, R.id.operate_next_tv, R.id.one_start_time, R.id.one_ending_time,
            R.id.one_end_time, one_save_time, R.id.TQSJ_edit})
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

                        BHCS = checkboxstr1 + checkboxstr2 + checkboxstr3 ;

                        if (A_ID_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������Ų���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (AJLB == null) {
                            Toast.makeText(ia, "���������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (anfaquhua_edit.getText().toString().equals("")) {
                            Toast.makeText(ia, "������������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (SFMA == null) {
                            Toast.makeText(ia, "�Ƿ���������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (SFXA == null) {
                            Toast.makeText(ia, "�Ƿ��̰�����Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (oneStartTime.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "����ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (oneEndingTime.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "����ʱ���ֹ����Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (AFDD_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "�����ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
                        }
//                        else if (oneEndTime.getText().toString().trim().equals("")) {
//                            Toast.makeText(ia, "�ư�ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
//                        }

                        else if (KTDD_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "����ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();

                        } else if (baohuren_name_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��������������Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (baohuren_company_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������λ����Ϊ��", Toast.LENGTH_SHORT).show();

                       }else if (BHCS == null) {
                            Toast.makeText(ia, "������ʩ����Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (TQZK == null) {
                            Toast.makeText(ia, "����״������Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (oneSaveTime.getText().toString().equals("")) {
                            Toast.makeText(ia, "����ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (XCTJ == null) {
                            Toast.makeText(ia, "�ֳ���������Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (GZTJ == null) {
                            Toast.makeText(ia, "������������Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (XCZH_edit.getText().toString().equals("")) {
                            Toast.makeText(ia, "�ֳ�ָ����Ա����Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (et_kyKydw.getText().toString().equals("")) {
                            Toast.makeText(ia, "��������Ա����Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (et_kyZpbgdw.getText().toString().equals("")) {
                            Toast.makeText(ia, "����������Ա����Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (baoanren_edit.getText().toString().equals("")) {
                            Toast.makeText(ia, "������/������Ա����Ϊ��", Toast.LENGTH_SHORT).show();

                        }else if (jianzhenren_edit.getText().toString().equals("")) {
                            Toast.makeText(ia, "��֤�˲���Ϊ��", Toast.LENGTH_SHORT).show();
                        }


                        else {
                            uploadHJWZ();
                            startProgressDialog(UPLOAD);
                            new Thread(uploadRunJiBenXinXi).start();


                            setBackgroundColorById(R.id.tv_btn);
                            operateNextTv.setText("��һ��");
                        }

//                        setBackgroundColorById(R.id.tv_btn);
//                        operateNextTv.setText("��һ��");

                        break;

                    case R.id.tv_btn:
                        setBackgroundColorById(R.id.anime_btn);
                        operateNextTv.setText("��һ��");
                        break;

                    case R.id.anime_btn:


//                        if(TextUtils.isEmpty(A_ID_edit.getText())){
//                            Toast.makeText(ia, "������Ų���Ϊ��", Toast.LENGTH_SHORT).show();
//                        }
                        if (A_ID_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������Ų���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (MC_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "���Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (JBTZ_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (SL_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (TQBW_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��ȡ��λ����Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (TQFF_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��ȡ��������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (TQR_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��ȡ�˲���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (TQSJ_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "��ȡʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadHJWZ();
                            startProgressDialog(UPLOAD);
                            new Thread(uploadRun).start();

                            setBackgroundColorById(R.id.variety_btn);
                            operateNextTv.setText("�ύ");
                        }


                        break;

                    case R.id.variety_btn:
                        //�������
                        if (A_ID_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������Ų���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZARS_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "������������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZADD_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "�����ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
                        }
//                        else if (ZAGJ_edit.getText().toString().trim().equals("")) {
//                            Toast.makeText(ia, "�������߲���Ϊ��", Toast.LENGTH_SHORT).show();
//                        }
                        else if (ZAGC_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "�������̲���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (XCFXYJCL == null) {
                            Toast.makeText(ia, "�ֳ��������ݲ��ϲ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (AJXZ == null) {
                            Toast.makeText(ia, "�������ʲ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (XZDX == null) {
                            Toast.makeText(ia, "ѡ�������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (XZCS == null) {
                            Toast.makeText(ia, "ѡ��������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZASJ == null) {
                            Toast.makeText(ia, "����ʱ������Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZARK == null) {
                            Toast.makeText(ia, "������ڲ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZASD == null) {
                            Toast.makeText(ia, "�����ֶβ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (QRFS == null) {
                            Toast.makeText(ia, "���ַ�ʽ����Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZATD == null) {
                            Toast.makeText(ia, "�����ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
                        } else if (ZADJMD == null) {
                            Toast.makeText(ia, "��������Ŀ�Ĳ���Ϊ��", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadHJWZ();
                            startProgressDialog(UPLOAD);
                            new Thread(uploadRunFenxiiJan).start();

                            operateNextTv.setText("�ύ");
                            finish();
                        }
//                        Toast.makeText(this, "����", Toast.LENGTH_SHORT).show();
                        break;


                }
//                Toast.makeText(this, "��һ��", Toast.LENGTH_SHORT).show();
                break;

            case R.id.one_start_time:
                DateWheelDialogN kyDateChooseDialog3 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneStartTime.setText(time);
                    }
                });
                kyDateChooseDialog3.setDateDialogTitle("����ʱ��");
                kyDateChooseDialog3.showDateChooseDialog();
                break;
            case R.id.one_ending_time:
                DateWheelDialogN kyDateChooseDialog4 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneEndingTime.setText(time);
                    }
                });
                kyDateChooseDialog4.setDateDialogTitle("�᰸ʱ��");
                kyDateChooseDialog4.showDateChooseDialog();
                break;
            case R.id.one_end_time:
                DateWheelDialogN kyDateChooseDialog5 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneEndTime.setText(time);
                    }
                });
                kyDateChooseDialog5.setDateDialogTitle("��ֹʱ��");
                kyDateChooseDialog5.showDateChooseDialog();
                break;

            case one_save_time:
                DateWheelDialogN kyDateChooseDialog6 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        oneSaveTime.setText(time);
                    }
                });
                kyDateChooseDialog6.setDateDialogTitle("����ʱ��");
                kyDateChooseDialog6.showDateChooseDialog();
                break;

            case R.id.TQSJ_edit:
                DateWheelDialogN kyDateChooseDialog7 = new DateWheelDialogN(this, new DateWheelDialogN.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        threeGetTime.setText(time);
                    }
                });
                kyDateChooseDialog7.setDateDialogTitle("��ȡʱ��");
                kyDateChooseDialog7.showDateChooseDialog();
                break;

            default:
                break;
        }
    }

    private void uploadHJWZ() {


        File fileStart = new File(Values.ALLFILES + "wtxt/XCKYBL/");
        boolean flag = getFileName2(fileStart.listFiles(), name);

        if (flag) {
            //���ڱ����ļ�
        } else {
            try {
                String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                File file = new File(sdcardPath + "/TC/wtxt/XCKYBL/");
                if (!file.exists()) {
                    file.mkdir();
                }

                String fileName = Values.PATH_BOOKMARK + "XCKYBL/" + name + "_" + UtilTc.getCurrentTime() + ".doc";
                newPath = fileName;
                InputStream inputStream = getAssets().open("xckybl.doc");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            doScan();
        }


        startProgressDialog(UPLOAD);
        new Thread(uploadRun).start();

        SendFile sf = new SendFile();
        sf.start();
    }


    private List<String> bltxt = new ArrayList<String>();

    private void getFileName(File[] files, String jqNum) {
        bltxt.clear();
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName" + fileName);
                        bltxt.add(fileName);
                    }
                }
            }
        }
    }


    private boolean getFileName2(File[] files, String jqNum) {
        boolean isFlag = false;
        if (files != null)// nullPointer
        {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName2(file.listFiles(), jqNum);
                } else {
                    String fileName = file.getName();
                    if (fileName.contains(jqNum) && fileName.endsWith(".doc")) {
                        Log.e("e", "fileName" + fileName);
                        isFlag = true;

                    }
                }
            }
        }
        return isFlag;
    }

    private FTPClient myFtp;
    private PoliceStateListBean plb;
    String currentFilePaht = "";
    private String currentFile = "";
    private int fileCount = 0;
    private int mTotalSize = 0;

    public class SendFile extends Thread {
        private String currentPath = "";

        @Override
        public void run() {
            try {
                myFtp = new FTPClient();
                myFtp.connect("61.176.222.166", 21); // ����
                myFtp.login("admin", "1234"); // ��¼

                if (Values.dbjqList.size() > 0)
                    plb = Values.dbjqList.get(0);

                File fileStart = new File(Values.ALLFILES + "wtxt/XCKYBL/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                //	Log.e("path", "path"+path);

                for (int i = 0; i < bltxt.size(); i++) {
                    //�ж��ϴ����ĸ��ļ���
                    if (bltxt.get(i).endsWith(".doc")) {
                        myFtp.changeDirectory("../");
                        myFtp.changeDirectory("xcbl-xckybl");
                        currentPath = Values.PATH_xckybl;
                        currentFilePaht = "/xcbl-xckybl";
                    }

                    File file = new File(currentPath + bltxt.get(i));
                    fileCount = (int) file.length();

                    mTotalSize = fileCount;
                    currentFile = currentFilePaht + "/" + bltxt.get(i);
                    MyFTPDataTransferListener listener = new MyFTPDataTransferListener(bltxt.get(i));
                    myFtp.upload(file, listener); // �ϴ�
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_UPLOAD);
            }
        }
    }

    private String mediaFormat = "";

    private class MyFTPDataTransferListener implements FTPDataTransferListener {
        String fileName = "";

        MyFTPDataTransferListener(String fileNameRet) {
            fileName = fileNameRet;
        }

        @Override
        public void aborted() {
            // TODO Auto-generated method stub
        }

        @Override
        public void completed() {// �ϴ��ɹ�
            // TODO Auto-generated method stub
            UtilTc.showLog("currentFile:" + currentFile);
            UtilTc.showLog("currentFile ��3λ" + currentFile.substring(currentFile.length() - 3, currentFile.length()));
            mediaFormat = currentFile.substring(currentFile.length() - 3, currentFile.length());
            if (mediaFormat.equals("doc")) {
                mediaType = "�ĵ�";
            }
            new Thread(media).start();
            File file = new File(Values.PATH_xckybl + fileName);
            if (file.exists()) {
                boolean isDel = file.delete();
            }
            Message msg;
            msg = Message.obtain();
            msg.what = Values.SUCCESS_UPLOAD;
            mHandler1.sendMessage(msg);
        }

        @Override
        public void failed() {// �ϴ�ʧ��
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler1.sendMessage(msg);
        }

        @Override
        public void started() {// �ϴ���ʼ
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = 2;
            mHandler1.sendMessage(msg);
        }

        @Override
        public void transferred(int length) {// �ϴ����̼���
            int progress = length;
            Message msg;
            msg = Message.obtain();
            msg.what = 1;
            msg.obj = progress;
            mHandler1.sendMessage(msg);
        }
    }

    Handler mHandler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ��ɹ�");

                    stopProgressDialog();
                    //�ı侯��״̬
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("�ļ��ϴ�ʧ��");
                    stopProgressDialog();
                    break;
            }
        }

        ;
    };

    private String mediaType = "";
    //�ϴ�ý����Ϣ
    Runnable media = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //��ѯ������ʱ��͵����ֳ�ʱ��
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", name));
            params.add(new BasicNameValuePair("A_type", mediaType));
            params.add(new BasicNameValuePair("A_Format", mediaFormat));
            params.add(new BasicNameValuePair("A_MM", currentFile));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient()
                        .execute(httpRequest);
                Log.e("code", "code"
                        + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse
                            .getEntity());
                    Log.e("e", "�ϴ�ý���ֵ�ǣ�" + strResult);
                    // json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //�ϴ��ɹ�
                        //	mHandler.sendEmptyMessage(Values.SUCCESS_RECORDUPLOAD);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


//    private void doScan() {
//        //��ȡģ���ļ�
////        File demoFile=new File(demoPath);
//        //�������ɵ��ļ�
//        File newFile = new File(newPath);
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("$XCKYDW$", et_kyKydw.getText().toString());
//        map.put("$BGDW$", et_kyZpbgdw.getText().toString());
//        map.put("$TIME$", et_kyZpsj.getText().toString());
//        map.put("$KYSY$", et_kySy.getText().toString());
//
//        map.put("$XCKYKSSJ$", et_kyBeginTime.getText().toString());
//        map.put("$XCKYJSSJ$", et_kyEndTime.getText().toString());
//        map.put("$XCDD$", et_kyXcdd.getText().toString());
//        map.put("$SCBHQK$", et_kyXcbhqk.getText().toString());
//        map.put("$TQ$", kyTq);
//        map.put("$WD$", et_kyWd.getText().toString());
//        map.put("$SD$", et_kySd.getText().toString());
//        map.put("$FX$", et_kyFx.getText().toString());
//        map.put("$XCTJ$", kyXctj);
//        map.put("$GX$", kyGx);
//        map.put("$ZHR$", et_kyZhr.getText().toString());
//
//        map.put("$DW$", et_kyZhrDw.getText().toString());
//        map.put("$ZW$", et_kyZhrZw.getText().toString());
//        map.put("$XCKYQK$", et_kyKyqk.getText().toString());
//        map.put("$ZTNUM$", et_kyHzt.getText().toString());
//
//        map.put("$ZXNUM$", et_kyZx.getText().toString());
//        map.put("$LX$", et_kyLx.getText().toString());
//        map.put("$LYTIME$", et_kyLy.getText().toString());
//
//        map.put("$BLR$", et_kyBlr.getText().toString());
//        map.put("$ZTR$", et_kyZtr.getText().toString());
//        map.put("$ZXR$", et_kyZxr.getText().toString());
//        map.put("$LXR$", et_kyLxr.getText().toString());
//        map.put("$LYR$", et_kyLyr.getText().toString());
//
//        map.put("$BRQM1$", et_brqm1.getText().toString());
//        map.put("$BRDW1$", et_brdw1.getText().toString());
//        map.put("$BRZW1$", et_brzw1.getText().toString());
//
//        map.put("$BRQM2$", et_brqm2.getText().toString());
//        map.put("$BRDW2$", et_brdw2.getText().toString());
//        map.put("$BRZW2$", et_brzw2.getText().toString());
//
//        map.put("$BRQM3$", et_brqm3.getText().toString());
//        map.put("$BRDW3$", et_brdw3.getText().toString());
//        map.put("$BRZW3$", et_brzw3.getText().toString());
//
//        map.put("$BRQM4$", et_brqm4.getText().toString());
//        map.put("$BRDW4$", et_brdw4.getText().toString());
//        map.put("$BRZW4$", et_brzw4.getText().toString());
//
//        map.put("$BRQM5$", et_brqm5.getText().toString());
//        map.put("$BRDW5$", et_brdw5.getText().toString());
//        map.put("$BRZW5$", et_brzw5.getText().toString());
//
//        map.put("$BRQM6$", et_brqm6.getText().toString());
//        map.put("$BRDW6$", et_brdw6.getText().toString());
//        map.put("$BRZW6$", et_brzw6.getText().toString());
//
//
//        map.put("$JZRQM1$", et_jzrqm1.getText().toString());
//        map.put("$JZRSEX1$", et_jzrxb1.getText().toString());
//        map.put("$JZRSR1$", et_jzrsr1.getText().toString());
//        map.put("$JZRZZ1$", et_jzrzz1.getText().toString());
//
//        map.put("$JZRQM2$", et_jzrqm2.getText().toString());
//        map.put("$JZRSEX2$", et_jzrxb2.getText().toString());
//        map.put("$JZRSR2$", et_jzrsr2.getText().toString());
//        map.put("$JZRZZ2$", et_jzrzz2.getText().toString());
//
//        writeDoc(newFile, map);
//    }


    /**
     * demoFile ģ���ļ�
     * newFile �����ļ�
     * map Ҫ��������
     */
    public void writeDoc(File newFile, Map<String, String> map) {
        try {
            InputStream in = getAssets().open("xckybl.doc");
//            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // ��ȡword�ı�����
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // �滻�ı�����
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // ����ֽ���
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                afterPhoto(data);
                break;
            case 2:// ��ѡ������ʱ����
                afterPhoto2(data);
                break;


        }
    }

    private void afterPhoto2(Intent data) {
        num++;
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
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
                Log.e("ͼƬ·��afterPhoto2", "" + photo);
//                iv_2.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afterPhoto(Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
            compressImage(photo, Environment.getExternalStorageDirectory() + "/" + "xczp1" + ".jpg");
            if (photo == null) {
//                iv_1.setImageResource(R.drawable.icon_photo);
            } else {
                Log.e("ͼƬ·��afterPhoto1", "" + photo);

//                iv_1.setImageBitmap(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200) {    //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
                baos.reset();//����baos�����baos
                options -= 10;//ÿ�ζ�����10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��
            }
            //ѹ���ú�д���ļ���
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
            Log.e("ͼƬ·��", mDatas.get(position).getImageUrl());


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


    //�ۼ���֤��Ϣ����
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Values.SUCCESS_RECORDUPLOAD://

                    break;
                case Values.ERROR_CONNECT:
                    UtilTc.myToastForContent(getApplicationContext());
                    break;
                case Values.ERROR_OTHER:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();
                    break;
                case Values.ERROR_NULLVALUEFROMSERVER:
                    UtilTc.showLog("�������쳣");
                    stopProgressDialog();
                    break;
                case Values.SUCCESS_FORRESULR:
                    UtilTc.myToast(getApplicationContext(), "" + errorMessage);
                    stopProgressDialog();
                    ia.sendHandleMsg(100, SenceCheck.waitingHandler);
                    break;
            }
        }

        ;
    };

    //    �ۼ���֤�����ϴ�
    Runnable uploadRun = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/xskc/ADD_ZF_XSKC03.asp";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID", anjiannum.getText().toString()));
            params.add(new BasicNameValuePair("MC", MC_edit.getText().toString()));
            params.add(new BasicNameValuePair("JTZ", JBTZ_edit.getText().toString()));
            params.add(new BasicNameValuePair("SL", SL_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQBW", TQBW_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQFF", TQFF_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQR", TQR_edit.getText().toString()));
            params.add(new BasicNameValuePair("BZ", BZ_edit.getText().toString()));
            params.add(new BasicNameValuePair("JZR", JZR_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQSJ", TQSJ_edit.getText().toString()));
            params.add(new BasicNameValuePair("TP", "ͼƬ"));
            params.add(new BasicNameValuePair("PNUM", "�ϴ��˾���"));


            Log.e("e", "params ��" + params);
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�" + strResult);
                    if (strResult == null || strResult.equals("")) {
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if (code.trim().equals("0")) {
                        //    jsResult=person.getJSONObject("data");
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    } else if (code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };


    //    ������������ϴ�
    Runnable uploadRunFenxiiJan = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/xskc/ADD_ZF_XSKC04.asp";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID", anjiannum.getText().toString()));
            params.add(new BasicNameValuePair("XCFXYJCL", XCFXYJCL));
            params.add(new BasicNameValuePair("AJXZ", AJXZ));
            params.add(new BasicNameValuePair("XZDX", XZDX));
            params.add(new BasicNameValuePair("XZCS", XZCS));
            params.add(new BasicNameValuePair("ZASJ", ZASJ));
            params.add(new BasicNameValuePair("ZARK", ZARK));
            params.add(new BasicNameValuePair("ZASD", ZASD));
            params.add(new BasicNameValuePair("QRFS", QRFS));
            params.add(new BasicNameValuePair("ZATD", ZATD));
            params.add(new BasicNameValuePair("ZADJMD", ZADJMD));
            params.add(new BasicNameValuePair("ZARS", ZARS_edit.getText().toString()));
            params.add(new BasicNameValuePair("ZADD", ZADD_edit.getText().toString()));
            params.add(new BasicNameValuePair("ZAGJ", ZAGJ_edit.getText().toString()));
            params.add(new BasicNameValuePair("ZAGC", ZAGC_edit.getText().toString()));
            params.add(new BasicNameValuePair("ZARTD", ZATD));
            params.add(new BasicNameValuePair("CBYJYGJ", CBYJ_edit.getText().toString()));
            params.add(new BasicNameValuePair("GZJY", GZJY_edit.getText().toString()));
            params.add(new BasicNameValuePair("XCFXDW", XCFXDW_edit.getText().toString()));
            params.add(new BasicNameValuePair("XCFXR", XCFXR_edit.getText().toString()));
            params.add(new BasicNameValuePair("FXSJ", ""));
            params.add(new BasicNameValuePair("PNUM", "����"));


            Log.e("e", "params ��" + params);
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�" + strResult);
                    if (strResult == null || strResult.equals("")) {
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if (code.trim().equals("0")) {
                        //    jsResult=person.getJSONObject("data");
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    } else if (code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };

    //    ������������ϴ�
    Runnable uploadRunJiBenXinXi = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/xskc/ADD_ZF_XSKC01.asp";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID", anjiannum.getText().toString()));
            params.add(new BasicNameValuePair("A_SLID", A_ID_edit.getText().toString()));
            params.add(new BasicNameValuePair("AJLB", AJLB));
            params.add(new BasicNameValuePair("FAQH", anfaquhua_edit.getText().toString()));
            params.add(new BasicNameValuePair("SFMA", SFMA));
            params.add(new BasicNameValuePair("SFXA", SFXA));
            params.add(new BasicNameValuePair("FASJ1", oneStartTime.getText().toString()));
            params.add(new BasicNameValuePair("FASJ2", oneEndingTime.getText().toString()));
            params.add(new BasicNameValuePair("FADD", AFDD_edit.getText().toString()));
            params.add(new BasicNameValuePair("PASJ", oneEndTime.getText().toString()));
            params.add(new BasicNameValuePair("ZPBGDW", ""));//
            params.add(new BasicNameValuePair("JKSJ", ""));//
            params.add(new BasicNameValuePair("JJR", ""));//�Ӿ���
            params.add(new BasicNameValuePair("ZPFS", ""));//ָ�ɷ�ʽ
            params.add(new BasicNameValuePair("CJSJ", ""));//����ʱ��
            params.add(new BasicNameValuePair("KSKYSJ", ""));//����ʱ�俪ʼ
            params.add(new BasicNameValuePair("JSKYSJ", ""));//����ʱ�����
            params.add(new BasicNameValuePair("KYDD", KTDD_edit.getText().toString()));
            params.add(new BasicNameValuePair("KYSY", KYSY_edit.getText().toString()));
            params.add(new BasicNameValuePair("AJFXGC", AFGC_edit.getText().toString()));
            params.add(new BasicNameValuePair("BHRXM", baohuren_name_edit.getText().toString()));
            params.add(new BasicNameValuePair("BHRDW", baohuren_company_edit.getText().toString()));
            params.add(new BasicNameValuePair("BHCS", checkboxstr1 + checkboxstr2 + checkboxstr3));

            params.add(new BasicNameValuePair("BHSJ", oneSaveTime.getText().toString()));
            params.add(new BasicNameValuePair("XCWPFDCD", ""));//�ֳ���Ʒ�����̶�
            params.add(new BasicNameValuePair("XCTJ", XCTJ));
            params.add(new BasicNameValuePair("TQZK", TQZK));
            params.add(new BasicNameValuePair("WD", ""));//�¶�
            params.add(new BasicNameValuePair("SD", ""));//ʪ��
            params.add(new BasicNameValuePair("FX", ""));//����
            params.add(new BasicNameValuePair("GZTJ", GZTJ));
            params.add(new BasicNameValuePair("XCZHRY", XCZH_edit.getText().toString()));
            params.add(new BasicNameValuePair("KYJCRY", et_kyKydw.getText().toString()));
            params.add(new BasicNameValuePair("QTDDXCRY", et_kyZpbgdw.getText().toString()));//����
            params.add(new BasicNameValuePair("XCYLW", xianchangyiliuwu_edit.getText().toString()));


            params.add(new BasicNameValuePair("KYJCQK", kanyanqingkuang_edit.getText().toString()));
            params.add(new BasicNameValuePair("BHRBAR", baoanren_edit.getText().toString()));
            params.add(new BasicNameValuePair("SSWP", sunshiwupin_edit.getText().toString()));
            params.add(new BasicNameValuePair("LX", ""));//¼��
            params.add(new BasicNameValuePair("LY", ""));//¼��
            params.add(new BasicNameValuePair("SWQK", shangwangqingkuang_edit.getText().toString()));
            params.add(new BasicNameValuePair("SSWPZJZ", ""));//��ʧ��Ʒ�ܼ�ֵ
            params.add(new BasicNameValuePair("JZR", jianzhenren_edit.getText().toString()));
            params.add(new BasicNameValuePair("PNUM", "�ϴ�����"));


            Log.e("e", "params ��" + params);
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //ȡ��HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "��������ֵ�ǣ�" + strResult);
                    if (strResult == null || strResult.equals("")) {
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json ����
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"��������", "car":{ "hphm":"��A12345", "hpzl":"����", "csys":"��ɫ", "fdjh":"888888", "cjhm":"987654321" } } }
                    if (code.trim().equals("0")) {
                        //    jsResult=person.getJSONObject("data");
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.SUCCESS_FORRESULR);
                    } else if (code.trim().equals("10003")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    } else if (code.trim().equals("10001")) {
                        JSONObject jb = person.getJSONObject("data");
                        errorMessage = jb.getString("message");
                        mHandler.sendEmptyMessage(Values.ERROR_OTHER);
                    }
                } else {
                    //   mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  mHandler.sendEmptyMessage(Values.ERROR_CONNECT);
            }
        }
    };
}
