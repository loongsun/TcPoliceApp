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

    //痕迹物证
    private EditText A_ID_edit, MC_edit, JBTZ_edit, SL_edit, TQBW_edit, TQFF_edit, TQR_edit, BZ_edit, JZR_edit, TQSJ_edit;

    //分析意见
    private EditText ZARS_edit, ZADD_edit, ZAGJ_edit, ZAGC_edit;
    String XCFXYJCL, AJXZ, XZDX, XZCS, ZASJ, ZARK, ZACK, ZASD, QRFS, ZATD, ZADJMD;
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
    RadioButton mZARKRadio1,mZARKRadio2,mZARKRadio3,mZARKRadio4;

    RadioGroup zuoanshouduan_radio_group;
    RadioButton mZASDRadio1,mZASDRadio2,mZASDRadio3,mZASDRadio4,mZASDRadio5,mZASDRadio6,mZASDRadio7,mZASDRadio8,mZASDRadio9;

    RadioGroup ruqinfangshi_radio_group;
    RadioButton mRQFSRadio1,mRQFSRadio2,mRQFSRadio3,mRQFSRadio4,mRQFSRadio5,mRQFSRadio6,mRQFSRadio7,mRQFSRadio8,mRQFSRadio9;

    RadioGroup zuoantedian_radio_group;
    RadioButton mZATDRadio1,mZATDRadio2,mZATDRadio3;

    RadioGroup zuoanmudi_radio_group;
    RadioButton mZAMDRadio1,mZAMDRadio2,mZAMDRadio3,mZAMDRadio4,mZAMDRadio5,mZAMDRadio6;


    int num = 0;

    private BackOrderAdapter mAdapter;
    private ArrayList<ImageListBean> mImageList;
    private Account mAccount;
    private int posId;
    TcApp ia;


    private final static int UPLOAD = 1;
    String errorMessage = "";
    private CustomProgressDialog progressDialog = null;

    // 进度框
    private void startProgressDialog(int type) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            switch (type) {
                case UPLOAD:
                    progressDialog.setMessage("正在上传信息,请稍后");
                    break;
            }
        }
        progressDialog.show();
    }

    // 取消进度框
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
//痕迹物证
        MC_edit = (EditText) this.findViewById(R.id.MC_edit);
        JBTZ_edit = (EditText) this.findViewById(R.id.JBTZ_edit);
        SL_edit = (EditText) this.findViewById(R.id.SL_edit);
        TQBW_edit = (EditText) this.findViewById(R.id.TQBW_edit);
        TQFF_edit = (EditText) this.findViewById(R.id.TQFF_edit);
        TQR_edit = (EditText) this.findViewById(R.id.TQR_edit);
        BZ_edit = (EditText) this.findViewById(R.id.TQR_edit);
        JZR_edit = (EditText) this.findViewById(R.id.JZR_edit);
        TQSJ_edit = (EditText) this.findViewById(R.id.TQSJ_edit);

//分析意见
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
                    XCFXYJCL = "实地勘察";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "现场试验";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "调查访问";
                } else if (checkedId == mXCFXRadio1.getId()) {
                    XCFXYJCL = "检验鉴定";
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
                    AJXZ = "其他诈骗案";
                } else if (checkedId == mAJXZRadio2.getId()) {
                    AJXZ = "其他诈骗勒索案件";
                } else if (checkedId == mAJXZRadio3.getId()) {
                    AJXZ = "插片入室盗窃案件";
                } else if (checkedId == mAJXZRadio4.getId()) {
                    AJXZ = "盗窃自行车案件";
                } else if (checkedId == mAJXZRadio5.getId()) {
                    AJXZ = "拦路抢劫";
                } else if (checkedId == mAJXZRadio6.getId()) {
                    AJXZ = "故意伤害案件";
                } else if (checkedId == mAJXZRadio7.getId()) {
                    AJXZ = "入室抢劫案";
                } else if (checkedId == mAJXZRadio8.getId()) {
                    AJXZ = "矛盾激化";
                } else if (checkedId == mAJXZRadio9.getId()) {
                    AJXZ = "其他盗窃案";
                } else if (checkedId == mAJXZRadio10.getId()) {
                    AJXZ = "盗窃车内物品";
                } else if (checkedId == mAJXZRadio11.getId()) {
                    AJXZ = "盗窃宾馆案件";
                } else if (checkedId == mAJXZRadio12.getId()) {
                    AJXZ = "技术性开锁盗窃";
                } else if (checkedId == mAJXZRadio13.getId()) {
                    AJXZ = "攀爬入室盗窃";
                } else if (checkedId == mAJXZRadio14.getId()) {
                    AJXZ = "撬防盗门入室盗窃";
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
                    XZDX = "少女";
                } else if (checkedId == mXZDXRadio2.getId()) {
                    XZDX = "外地";
                } else if (checkedId == mXZDXRadio3.getId()) {
                    XZDX = "男青年";
                } else if (checkedId == mXZDXRadio4.getId()) {
                    XZDX = "本地";
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
                    XZCS = "超市";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "楼道";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "美容美发厅";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "居民小区";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "网吧";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "高层楼房";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "其他繁华地段";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "商业区";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "普通楼房";
                } else if (checkedId == mXZCSRadio1.getId()) {
                    XZCS = "路旁";
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
                    ZASJ = "雪天";
                } else if (checkedId == mZASJRadio2.getId()) {
                    ZASJ = "雾天";
                } else if (checkedId == mZASJRadio3.getId()) {
                    ZASJ = "雨天";
                } else if (checkedId == mZASJRadio4.getId()) {
                    ZASJ = "晴天";
                } else if (checkedId == mZASJRadio5.getId()) {
                    ZASJ = "昼";
                } else if (checkedId == mZASJRadio6.getId()) {
                    ZASJ = "傍晚";
                } else if (checkedId == mZASJRadio7.getId()) {
                    ZASJ = "晚";
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
                    ZARK = "其他";
                } else if (checkedId == mZARKRadio2.getId()) {
                    ZARK = "阳台";
                } else if (checkedId == mZARKRadio3.getId()) {
                    ZARK = "门";
                } else if (checkedId == mZARKRadio4.getId()) {
                    ZARK = "窗";
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
                    ZASD = "持钝器";
                } else if (checkedId == mZASDRadio2.getId()) {
                    ZASD = "踢打";
                } else if (checkedId == mZASDRadio3.getId()) {
                    ZASD = "拎包";
                } else if (checkedId == mZASDRadio4.getId()) {
                    ZASD = "掏包";
                } else if (checkedId == mZASDRadio5.getId()) {
                    ZASD = "钥匙开锁";
                } else if (checkedId == mZASDRadio6.getId()) {
                    ZASD = "用具钩锁";
                } else if (checkedId == mZASDRadio7.getId()) {
                    ZASD = "插片开锁";
                } else if (checkedId == mZASDRadio8.getId()) {
                    ZASD = "顺手牵羊";
                } else if (checkedId == mZASDRadio9.getId()) {
                    ZASD = "其他作案手段";
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
                    QRFS = "和平进入现场";
                } else if (checkedId == mRQFSRadio2.getId()) {
                    QRFS = "和平进入现场";
                } else if (checkedId == mRQFSRadio3.getId()) {
                    QRFS = "砸窗玻璃";
                } else if (checkedId == mRQFSRadio4.getId()) {
                    QRFS = "扒开门闩";
                } else if (checkedId == mRQFSRadio5.getId()) {
                    QRFS = "破护窗网";
                } else if (checkedId == mRQFSRadio6.getId()) {
                    QRFS = "破坏窗框";
                } else if (checkedId == mRQFSRadio7.getId()) {
                    QRFS = "破坏窗扇";
                } else if (checkedId == mRQFSRadio8.getId()) {
                    QRFS = "攀爬阳台";
                } else if (checkedId == mRQFSRadio9.getId()) {
                    QRFS = "撬门";
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
                    ZATD = "重复作案";
                } else if (checkedId == mZATDRadio2.getId()) {
                    ZATD = "团伙作案";
                } else if (checkedId == mZATDRadio3.getId()) {
                    ZATD = "单独作案";
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
                    ZADJMD = "淫欲";
                } else if (checkedId == mZAMDRadio2.getId()) {
                    ZADJMD = "激情";
                } else if (checkedId == mZAMDRadio3.getId()) {
                    ZADJMD = "其他";
                } else if (checkedId == mZAMDRadio4.getId()) {
                    ZADJMD = "强迫";
                } else if (checkedId == mZAMDRadio5.getId()) {
                    ZADJMD = "报复";
                } else if (checkedId == mZAMDRadio6.getId()) {
                    ZADJMD = "泄私愤";
                }
            }
        });




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
                operateNextTv.setText("下一步");
                break;

            case R.id.tv_btn:
                setBackgroundColorById(R.id.tv_btn);
                operateNextTv.setText("下一步");
                break;

            case R.id.anime_btn:
                setBackgroundColorById(R.id.anime_btn);
                operateNextTv.setText("下一步");
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
            R.id.one_end_time, R.id.one_save_time, R.id.TQSJ_edit})
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
                        operateNextTv.setText("下一步");
                        break;

                    case R.id.tv_btn:
                        setBackgroundColorById(R.id.anime_btn);
                        operateNextTv.setText("下一步");
                        break;

                    case R.id.anime_btn:


//                        if(TextUtils.isEmpty(A_ID_edit.getText())){
//                            Toast.makeText(ia, "案件编号不能为空", Toast.LENGTH_SHORT).show();
//                        }
                        if (A_ID_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "案件编号不能为空", Toast.LENGTH_SHORT).show();
                        } else if (MC_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "名称不能为空", Toast.LENGTH_SHORT).show();
                        } else if (JBTZ_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "基本特征不能为空", Toast.LENGTH_SHORT).show();
                        } else if (SL_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "数量不能为空", Toast.LENGTH_SHORT).show();
                        } else if (TQBW_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "提取部位不能为空", Toast.LENGTH_SHORT).show();
                        } else if (TQFF_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "提取方法不能为空", Toast.LENGTH_SHORT).show();
                        } else if (TQR_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "提取人不能为空", Toast.LENGTH_SHORT).show();
                        } else if (TQSJ_edit.getText().toString().trim().equals("")) {
                            Toast.makeText(ia, "提取时间不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadHJWZ();
                            startProgressDialog(UPLOAD);
                            new Thread(uploadRun).start();

                            setBackgroundColorById(R.id.variety_btn);
                            operateNextTv.setText("提交");
                        }


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

            case R.id.TQSJ_edit:
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

    private void uploadHJWZ() {


        File fileStart = new File(Values.ALLFILES + "wtxt/XCKYBL/");
        boolean flag = getFileName2(fileStart.listFiles(), name);

        if (flag) {
            //存在本地文件
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
                myFtp.connect("61.176.222.166", 21); // 连接
                myFtp.login("admin", "1234"); // 登录

                if (Values.dbjqList.size() > 0)
                    plb = Values.dbjqList.get(0);

                File fileStart = new File(Values.ALLFILES + "wtxt/XCKYBL/");
                getFileName(fileStart.listFiles(), name);

                //	myFtp.changeDirectory("wphoto");

                //	String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg";
                //	Log.e("path", "path"+path);

                for (int i = 0; i < bltxt.size(); i++) {
                    //判断上传到哪个文件夹
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
                    myFtp.upload(file, listener); // 上传
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
        public void completed() {// 上传成功
            // TODO Auto-generated method stub
            UtilTc.showLog("currentFile:" + currentFile);
            UtilTc.showLog("currentFile 后3位" + currentFile.substring(currentFile.length() - 3, currentFile.length()));
            mediaFormat = currentFile.substring(currentFile.length() - 3, currentFile.length());
            if (mediaFormat.equals("doc")) {
                mediaType = "文档";
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
        public void failed() {// 上传失败
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = Values.ERROR_UPLOAD;
            mHandler1.sendMessage(msg);
        }

        @Override
        public void started() {// 上传开始
            // TODO Auto-generated method stub
            Message msg;
            msg = Message.obtain();
            msg.what = 2;
            mHandler1.sendMessage(msg);
        }

        @Override
        public void transferred(int length) {// 上传过程监听
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
                    UtilTc.showLog("文件上传成功");

                    stopProgressDialog();
                    //改变警情状态
                    break;
                case Values.ERROR_UPLOAD:
                    UtilTc.showLog("文件上传失败");
                    stopProgressDialog();
                    break;
            }
        }

        ;
    };

    private String mediaType = "";
    //上传媒体信息
    Runnable media = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/addmeiti/";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //查询出出警时间和到达现场时间
//            mApp.getmDota().jq_queryTime(plb.getJqNum());
            params.add(new BasicNameValuePair("A_ID", name));
            params.add(new BasicNameValuePair("A_type", mediaType));
            params.add(new BasicNameValuePair("A_Format", mediaFormat));
            params.add(new BasicNameValuePair("A_MM", currentFile));
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                        params, "UTF-8");
                httpRequest.setEntity(formEntity);
                // 取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient()
                        .execute(httpRequest);
                Log.e("code", "code"
                        + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse
                            .getEntity());
                    Log.e("e", "上传媒体的值是：" + strResult);
                    // json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    if (code.trim().equals("0")) {
                        //上传成功
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
//        //获取模板文件
////        File demoFile=new File(demoPath);
//        //创建生成的文件
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
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
    public void writeDoc(File newFile, Map<String, String> map) {
        try {
            InputStream in = getAssets().open("xckybl.doc");
//            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // 替换文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
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


    //痕迹物证信息保存
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
                    UtilTc.showLog("服务器异常");
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

    //    痕迹物证数据上传
    Runnable uploadRun = new Runnable() {
        @Override
        public void run() {
            String url_passenger = "http://61.176.222.166:8765/interface/xskc/ADD_ZF_XSKC03.asp";
            HttpPost httpRequest = new HttpPost(url_passenger);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("A_ID", "1233"));
            params.add(new BasicNameValuePair("MC", MC_edit.getText().toString()));
            params.add(new BasicNameValuePair("JTZ", JBTZ_edit.getText().toString()));
            params.add(new BasicNameValuePair("SL", SL_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQBW", TQBW_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQFF", TQFF_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQR", TQR_edit.getText().toString()));
            params.add(new BasicNameValuePair("BZ", BZ_edit.getText().toString()));
            params.add(new BasicNameValuePair("JZR", JZR_edit.getText().toString()));
            params.add(new BasicNameValuePair("TQSJ", TQSJ_edit.getText().toString()));
            params.add(new BasicNameValuePair("TP", "图片"));
            params.add(new BasicNameValuePair("PNUM", "上传人警号"));


            Log.e("e", "params 是" + params);
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
                httpRequest.setEntity(formEntity);
                //取得HTTP response
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                Log.e("code", "code" + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Log.e("e", "传回来的值是：" + strResult);
                    if (strResult == null || strResult.equals("")) {
                        mHandler.sendEmptyMessage(Values.ERROR_NULLVALUEFROMSERVER);
                        return;
                    }
                    //json 解析
                    JSONTokener jsonParser = new JSONTokener(strResult);
                    JSONObject person = (JSONObject) jsonParser.nextValue();
                    String code = person.getString("error code");
                    //{ "error code":0, "data":{ "message":"", "result":"盗抢车辆", "car":{ "hphm":"辽A12345", "hpzl":"蓝牌", "csys":"黑色", "fdjh":"888888", "cjhm":"987654321" } } }
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
