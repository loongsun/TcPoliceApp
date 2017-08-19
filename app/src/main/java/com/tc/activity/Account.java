package com.tc.activity;

/**
 * ×÷Õß£º³Âº× on 2017/4/23.
 * °æ±¾£ºv1.0
 */

public class Account {

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


}
