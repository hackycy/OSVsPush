package com.siyee.demo.util;

import android.text.TextUtils;

import java.util.List;

public class NullUtils {

    public static boolean checkNull(List list) {
        return list == null || list.size() > 0;
    }

    public static boolean checkNull(String str) {
        return TextUtils.isEmpty(str);
    }

}
