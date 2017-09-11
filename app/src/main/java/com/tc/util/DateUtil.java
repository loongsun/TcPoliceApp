package com.tc.util;

/**
 * Created by zhao on 17-9-10.
 */

public class DateUtil {

    public static boolean isDateRight(String start,String end){
        if(end!=null && start!=null)
        {
            if(end.compareTo(start)>0)
            {
                return true;
            }
        }
        return false;
    }
}
