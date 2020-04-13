package com.siyee.oscvpush.mi;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.base.IPushCallback;
import com.siyee.oscvpush.base.IPushManager;
import com.siyee.oscvpush.base.PushAdapter;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.util.MetaDataUtils;
import com.siyee.oscvpush.util.NullUtils;
import com.siyee.oscvpush.util.RomUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * 小米推送服务注册
 * 需要在清单文件Application中注册小米应用AppID，appkey
 * <pre>
 * {@code
 *  <meta-data
 *     android:name="com.xiaomi.push.app_id"
 *     android:value="appid=xxxxxx" />
 *  <meta-data
 *     android:name="com.xiaomi.push.app_key"
 *     android:value="appkey=xxxxxx" />
 * }
 * </pre>
 */
public class MiPushRegister implements IPushManager {

    public static final String METADATA_KEY_APPID = "com.xiaomi.push.app_id";

    public static final String METADATA_KEY_APPKEY = "com.xiaomi.push.app_key";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private volatile static MiPushRegister mInstance;

    private MiPushRegister(Context applicationContext) {
        mContext = applicationContext;
    }

    private static IPushCallback mPushCallback;

    private static void setPushCallback(IPushCallback pushCallback) {
        if (pushCallback == null) {
            mPushCallback = new PushAdapter();
            return;
        }
        mPushCallback = pushCallback;
    }

    public static IPushCallback getPushCallback() {
        return mPushCallback;
    }

    /**
     * 只需要第一次调用时传入Context
     * @param applicationContext applicationContext
     * @return
     */
    public static MiPushRegister getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (MiPushRegister.class) {
                if (mInstance == null) {
                    mInstance = new MiPushRegister(applicationContext);
                }
            }
        }
        return mInstance;
    }

    /**
     * 小米推送服务注册
     * @param callback 回调
     */
    @Override
    public void register(IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("XIAOMI is Support Push");
            MiPushRegister.setPushCallback(callback);
            MiPushClient.registerPush(mContext,
                    MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPID),
                    MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPKEY));
        }
    }

    /**
     * 因为推送服务XMPushService在AndroidManifest.xml中设置为运行在另外一个进程，
     * 这导致本Application会被实例化两次，所以让应用的主进程初始化。
     * @return
     */
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mContext.getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unregister() {
        MiPushClient.unregisterPush(mContext);
    }

    @Override
    public boolean isSupportPush() {
        return shouldInit() && RomUtils.isXiaomi();
    }

    @Override
    public void setAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        MiPushClient.setAlias(mContext, alias, null);
    }

    @Override
    public void unsetAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        MiPushClient.unsetAlias(mContext, alias, null);
    }

    @Override
    public void getAliases() {
        MiPushRegister.getPushCallback().onGetAliases(PushConstants.SUCCESS_CODE, MiPushClient.getAllAlias(mContext));
    }

    @Override
    public void setTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        MiPushClient.subscribe(mContext, tag, null);
    }

    @Override
    public void unsetTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        MiPushClient.subscribe(mContext, tag, null);
    }

    @Override
    public void getTags() {
        MiPushRegister.getPushCallback().onGetTags(PushConstants.SUCCESS_CODE, MiPushClient.getAllTopic(mContext));
    }

    @Override
    public void turnOnPush() {
        MiPushClient.enablePush(mContext);
    }

    @Override
    public void turnOffPush() {
        MiPushClient.disablePush(mContext);
    }

    @Override
    public void getRegId() {
        String regId = MiPushClient.getRegId(mContext);
        if (NullUtils.checkNull(regId)) {
            MiPushRegister.getPushCallback().onGetRegId(PushConstants.UNKNOWN_CODE, null);
        } else {
            MiPushRegister.getPushCallback().onGetRegId(PushConstants.SUCCESS_CODE, Token.buildToken(Target.XIAOMI, regId));
        }
    }
}
