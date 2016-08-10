package com.oceanwing.at.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by franky on 16/8/10.
 */
public class DateTimeUtils {

    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat sFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);

    public static long getCurrentTimeInLong(){
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getCurrentTimeInString(sFormat);
    }

    public static String getCurrentTimeInString(SimpleDateFormat format) {
        return format.format(new Date());
    }


    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, sFormat);
    }

    public static String getTime(long timeInMillis, SimpleDateFormat format) {
        return format.format(new Date(timeInMillis));
    }

}
