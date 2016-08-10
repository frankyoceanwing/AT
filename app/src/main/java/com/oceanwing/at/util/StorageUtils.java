package com.oceanwing.at.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by franky on 16/8/10.
 */
public class StorageUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getExternalDir() {
        return getExternalDir(null);
    }

    public static String getExternalDir(String dir) {
        return Environment.getExternalStoragePublicDirectory(dir).toString();
    }

    public static String getExternalAppDir(Context context) {
        return getExternalDir() + File.separator + "Android" + File.separator + "data" + File.separator + context.getPackageName();
    }

    public static String getExternalAppFilesDir(Context context, String dir) {
        return context.getExternalFilesDir(dir).toString();
    }

    public static String getExternalAppFilesDir(Context context) {
        return getExternalAppFilesDir(context, null);
    }

    public static String getExternalAppCacheDir(Context context) {
        return context.getExternalCacheDir().toString();
    }
}
