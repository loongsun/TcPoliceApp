package com.tc.util;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by zhao on 17-9-10.
 */

public class DateUtil {

    public static boolean isDateRight(String start, String end) {
        if (!TextUtils.isEmpty(end) && !TextUtils.isEmpty(start)) {
            if (end.compareTo(start) > 0) {
                return true;
            }
        }
        return false;
    }

    public static void handleMonthMap(String startTime, String endTime, Map<String, String> map) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || map == null) {
            return;
        }
        try {
            String y1 = startTime.substring(0, 4);
            String M1 = startTime.substring(5, 7);
            String d1 = startTime.substring(8, 10);
//            String H1 = startTime.substring(11, 13);
//            String m1 = startTime.substring(14, 16);

            String y2 = endTime.substring(0, 4);
            String M2 = endTime.substring(5, 7);
            String d2 = endTime.substring(8, 10);
//            String H2 = endTime.substring(11, 13);
//            String m2 = endTime.substring(14, 16);

            map.put("&yy1", "" + y1);
            map.put("&M1", "" + M1);
            map.put("&d1", "" + d1);
//            map.put("&H1", "" + H1);
//            map.put("&f1", "" + m1);

            map.put("&yy2", "" + y2);
            map.put("&M2", "" + M2);
            map.put("&d2", "" + d2);
//            map.put("&H2", "" + H2);
//            map.put("&f2", "" + m2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleDateMap(String startTime, String endTime, Map<String, String> map) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || map == null) {
            return;
        }
        try {
            String y1 = startTime.substring(0, 4);
            String M1 = startTime.substring(5, 7);
            String d1 = startTime.substring(8, 10);
            String H1 = startTime.substring(11, 13);
            String m1 = startTime.substring(14, 16);

            String y2 = endTime.substring(0, 4);
            String M2 = endTime.substring(5, 7);
            String d2 = endTime.substring(8, 10);
            String H2 = endTime.substring(11, 13);
            String m2 = endTime.substring(14, 16);

            map.put("&yy1", "" + y1);
            map.put("&M1", "" + M1);
            map.put("&d1", "" + d1);
            map.put("&H1", "" + H1);
            map.put("&f1", "" + m1);

            map.put("&yy2", "" + y2);
            map.put("&M2", "" + M2);
            map.put("&d2", "" + d2);
            map.put("&H2", "" + H2);
            map.put("&f2", "" + m2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
