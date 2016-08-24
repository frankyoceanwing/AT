package com.oceanwing.at.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.apache.commons.lang3.StringUtils;

public class MetaDataUtil {

    private MetaDataUtil() {
    }

    public static String read(Context context, String name) throws PackageManager.NameNotFoundException {
        if (context != null && StringUtils.isNotBlank(name)) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo appInfo = packageManager
                        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (appInfo != null) {
                    return String.valueOf(appInfo.metaData.get(name));
                }
            }
        }
        return "";
    }
}
