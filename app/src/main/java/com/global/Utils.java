package com.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {
    public static String getIp(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("ip", Constant.NFCIp);
    }

    public static int getPort(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getInt("port", Constant.NFCPort);
    }

    public static String getAccount(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("acc", Constant.NFCAcc);
    }

    public static String getPassword(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("pwd", Constant.NFCPwd);
    }

    public static boolean getIsWss(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean("wss", true);
    }

    public static void setIsWss(Context context, boolean flag) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("wss", flag);
        editor.commit();
    }
}
