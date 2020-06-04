package com.siyee.oscvpush.mi;

import android.content.Context;

import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.model.Message;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 */
public class MiPushMessageReceiverImpl extends PushMessageReceiver {

    /**
     * 透传消息
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        LogUtils.e("onReceivePassThroughMessage : " + miPushMessage.toString());
        final Message message = buildMessage(miPushMessage);
//        MiPushRegister.getPushCallback().onMessageThrough(message);
    }

    /**
     * 接收服务器向客户端发送的通知消息
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        LogUtils.e("onNotificationMessageArrived : " + miPushMessage.toString());
        final Message message = buildMessage(miPushMessage);
//        MiPushRegister.getPushCallback().onMessage(message);
    }

    /**
     * 这个回调方法会在用户手动点击通知后触发
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        LogUtils.e("onNotificationMessageClicked : " + miPushMessage.toString());
        final Message message = buildMessage(miPushMessage);
//        MiPushRegister.getPushCallback().onMessageClicked(message);
    }

    /**
     * 接收客户端向服务器发送命令后的响应结果
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        LogUtils.e("onCommandResult : " + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                MiPushRegister.getPushCallback().onRegister(PushConstants.SUCCESS_CODE, Token.buildToken(Target.XIAOMI, cmdArg1));
            } else {
                MiPushRegister.getPushCallback().onRegister((int) miPushCommandMessage.getResultCode(), null);
            }
        } else if (MiPushClient.COMMAND_UNREGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                MiPushRegister.getPushCallback().onUnRegister(PushConstants.SUCCESS_CODE);
            } else {
                MiPushRegister.getPushCallback().onUnRegister((int) miPushCommandMessage.getResultCode());
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                List<String> aliases = new ArrayList<>();
                aliases.add(cmdArg1);
                MiPushRegister.getPushCallback().onSetAliases(PushConstants.SUCCESS_CODE, aliases);
            } else {
                MiPushRegister.getPushCallback().onSetAliases((int) miPushCommandMessage.getResultCode(), null);
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                List<String> aliases = new ArrayList<>();
                aliases.add(cmdArg1);
                MiPushRegister.getPushCallback().onUnsetAliases(PushConstants.SUCCESS_CODE, aliases);
            } else {
                MiPushRegister.getPushCallback().onUnsetAliases((int) miPushCommandMessage.getResultCode(), null);
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                List<String> tags = new ArrayList<>();
                tags.add(cmdArg1);
                MiPushRegister.getPushCallback().onSetTags(PushConstants.SUCCESS_CODE, tags);
            } else {
                MiPushRegister.getPushCallback().onSetTags((int) miPushCommandMessage.getResultCode(), null);
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                List<String> tags = new ArrayList<>();
                tags.add(cmdArg1);
                MiPushRegister.getPushCallback().onUnsetTags(PushConstants.SUCCESS_CODE, tags);
            } else {
                MiPushRegister.getPushCallback().onUnsetTags((int) miPushCommandMessage.getResultCode(), null);
            }
        }
    }

    /**
     * 接收客户端向服务器发送注册命令后的响应结果
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        LogUtils.e("onReceiveRegisterResult : " + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                MiPushRegister.getPushCallback().onRegister(PushConstants.SUCCESS_CODE, Token.buildToken(Target.XIAOMI, cmdArg1));
            } else {
                MiPushRegister.getPushCallback().onRegister((int) miPushCommandMessage.getResultCode(), null);
            }
        }
    }

    private Message buildMessage(MiPushMessage miPushMessage) {
        final Message message = Message.buildMessageForTarget(Target.XIAOMI);
        message.setMessageID(miPushMessage.getMessageId());
        message.setNotifyType(miPushMessage.getNotifyType());
        message.setTitle(miPushMessage.getTitle());
        message.setExtra(miPushMessage.getExtra());
        message.setMessage(miPushMessage.getDescription());
        return message;
    }

}
