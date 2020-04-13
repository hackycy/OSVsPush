package com.siyee.oscvpush.util;

import android.util.Log;

import com.siyee.oscvpush.PushConstants;

public class LogUtils {

    private static final String TAG = "SystemPush";

    public static void e(String msg) {
        if (PushConstants.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (PushConstants.DEBUG) {
            Log.i(TAG, msg);
        }
    }

}
