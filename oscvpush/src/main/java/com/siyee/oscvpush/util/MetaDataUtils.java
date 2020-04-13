package com.siyee.oscvpush.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public final class MetaDataUtils {

    /**
     * Return the value of meta-data in application.
     *
     * @param key The key of meta-data.
     * @return the value of meta-data in application
     */
    public static String getMetaDataInApp(Context context, final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

}
