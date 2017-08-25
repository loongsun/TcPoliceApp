package com.tc.activity;

import com.tc.bean.ImageListBean;

import java.util.ArrayList;

/**
 * ���ߣ��º� on 2017/4/23.
 * �汾��v1.0
 */

public class Account {

    private ArrayList<ImageListBean> mImageList;

    private static volatile Account Inatance;

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    private String depname = "";

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    private String admin = "";
    private String loginTag = "0";


    public static Account GetInstance() {
        if (Inatance == null) {
            synchronized (Account.class) {
                if (Inatance == null) {       //Double Checked
                    Inatance = new Account();
                }
            }
        }
        return Inatance;
    }

    public static Account getInatance() {
        return Inatance;
    }

    public static void setInatance(Account inatance) {
        Inatance = inatance;
    }


    public ArrayList<ImageListBean> getmImageList() {
        return mImageList;
    }

    public void setmImageList(ArrayList<ImageListBean> mImageList) {
        this.mImageList = mImageList;
    }

    public String getLoginTag() {
        return loginTag;
    }

    public void setLoginTag(String loginTag) {
        this.loginTag = loginTag;
    }
}
