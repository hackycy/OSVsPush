package com.siyee.oscvpush.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * 通知通道（Channel）工具类
 */
public class ChannelUtils {

    /**
     * 创建通知Channel @see <a href="https://developer.android.com/training/notify-user/channels>channels</a>
     * @param context
     * @param channelId
     * @param channelName
     * @param importance
     */
    public static void createNotificationChannel(Context context, String channelId,
                                                 String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
