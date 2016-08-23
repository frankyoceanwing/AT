package com.oceanwing.at.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class MetaDataUtil {

    private MetaDataUtil() {
    }

    public static String read(Context context, String name) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
        return String.valueOf(appInfo.metaData.get(name));
    }
}
