package com.oceanwing.at.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    private static final SimpleDateFormat sFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);

    private DateTimeUtil() {
    }

    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getCurrentTimeInString(sFormat);
    }

    public static String getCurrentTimeInString(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return getCurrentTimeInString(format);
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
