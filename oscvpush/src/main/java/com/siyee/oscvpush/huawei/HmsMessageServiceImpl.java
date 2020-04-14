package com.siyee.oscvpush.huawei;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.model.Message;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.util.NullUtils;

import java.util.Arrays;

public class HmsMessageServiceImpl extends HmsMessageService {

    /**
     * When an app calls the getToken method to apply for a token from the server,
     * if the server does not return the token during current method calling, the server can return the token through this method later.
     * This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
     * @param token token
     */
    @Override
    public void onNewToken(String token) {
        if (NullUtils.checkNull(token)) {
            HWPushRegister.getPushCallback().onRegister(PushConstants.UNKNOWN_CODE, null);
            return;
        }
        LogUtils.e(token);
        HWPushRegister.getPushCallback().onRegister(PushConstants.SUCCESS_CODE, Token.buildToken(Target.HUAWEI, token));
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.e("onMessageReceived is called");
        if (remoteMessage == null) {
            LogUtils.e("Received message entity is null!");
            return;
        }
        // getCollapseKey() Obtains the classification identifier (collapse key) of a message.
        // getData() Obtains valid content data of a message.
        // getMessageId() Obtains the ID of a message.
        // getMessageType() Obtains the type of a message.
        // getNotification() Obtains the notification data instance from a message.
        // getOriginalUrgency() Obtains the original priority of a message.
        // getSentTime() Obtains the time when a message is sent from the server.
        // getTo() Obtains the recipient of a message.
        LogUtils.e("getCollapseKey: " + remoteMessage.getCollapseKey()
                + "\n getData: " + remoteMessage.getData()
                + "\n getFrom: " + remoteMessage.getFrom()
                + "\n getTo: " + remoteMessage.getTo()
                + "\n getMessageId: " + remoteMessage.getMessageId()
                + "\n getOriginalUrgency: " + remoteMessage.getOriginalUrgency()
                + "\n getUrgency: " + remoteMessage.getUrgency()
                + "\n getSendTime: " + remoteMessage.getSentTime()
                + "\n getMessageType: " + remoteMessage.getMessageType()
                + "\n getTtl: " + remoteMessage.getTtl());

        // getBody() Obtains the displayed content of a message
        // getTitle() Obtains the title of a message
        // getTitleLocalizationKey() Obtains the key of the displayed title of a notification message
        // getTitleLocalizationArgs() Obtains variable parameters of the displayed title of a message
        // getBodyLocalizationKey() Obtains the key of the displayed content of a message
        // getBodyLocalizationArgs() Obtains variable parameters of the displayed content of a message
        // getIcon() Obtains icons from a message
        // getSound() Obtains the sound from a message
        // getTag() Obtains the tag from a message for message overwriting
        // getColor() Obtains the colors of icons in a message
        // getClickAction() Obtains actions triggered by message tapping
        // getChannelId() Obtains IDs of channels that support the display of messages
        // getImageUrl() Obtains the image URL from a message
        // getLink() Obtains the URL to be accessed from a message
        // getNotifyId() Obtains the unique ID of a message
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        // build msg
        final Message message = Message.buildMessageForTarget(Target.HUAWEI);
        message.setNotifyType(0);
        message.setMessageID(remoteMessage.getMessageId());
        message.setExtra(remoteMessage.getDataOfMap());
        if (notification != null) {
            LogUtils.e("\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());

            message.setTitle(notification.getTitle());
            message.setMessage(notification.getBody());
        }
        HWPushRegister.getPushCallback().onMessageThrough(message);
    }
}
