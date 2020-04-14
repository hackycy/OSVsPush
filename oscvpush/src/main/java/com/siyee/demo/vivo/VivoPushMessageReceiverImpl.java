package com.siyee.demo.vivo;

import android.content.Context;

import com.siyee.demo.PushConstants;
import com.siyee.demo.model.Message;
import com.siyee.demo.model.Target;
import com.siyee.demo.model.Token;
import com.siyee.demo.util.LogUtils;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class VivoPushMessageReceiverImpl extends OpenClientPushMessageReceiver {

    /***
     * 当设备接收到通知消息后，查看手机的通知栏，可以看到通知栏内的该新通知展示。
     * 当通知被点击时回调此方法
     * @param context 应用上下文
     * @param upsNotificationMessage 通知详情，详细信息见API接入文档
     */
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        LogUtils.e(upsNotificationMessage.toString());
        final Message message = Message.buildMessageForTarget(Target.VIVO);
        message.setNotifyType(upsNotificationMessage.getNotifyType());
        message.setMessageID(String.valueOf(upsNotificationMessage.getMsgId()));
        message.setMessage(upsNotificationMessage.getContent());
        message.setExtra(upsNotificationMessage.getParams());
        message.setTitle(upsNotificationMessage.getTitle());
        VivoPushRegister.getPushCallback().onMessageClicked(message);
    }

    /***
     * 当首次turnOnPush成功或regId发生改变时，回调此方法
     * 如需获取regId，请使用PushClient.getInstance(context).getRegId()
     * @param context 应用上下文
     * @param regId 注册id
     */
    @Override
    public void onReceiveRegId(Context context, String regId) {
        LogUtils.e(regId);
        VivoPushRegister.getPushCallback().onRegister(PushConstants.SUCCESS_CODE, Token.buildToken(Target.VIVO, regId));
    }

}
