package com.siyee.oscvpush.meizu;

import android.annotation.SuppressLint;
import android.content.Context;

import com.meizu.cloud.pushsdk.PushManager;
import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.base.IPushCallback;
import com.siyee.oscvpush.base.IPushManager;
import com.siyee.oscvpush.base.PushAdapter;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.util.NullUtils;
import com.siyee.oscvpush.util.RomUtils;

/**
 * 魅族推送服务注册
 */
public class MZPushRegister implements IPushManager {

//    public static final String METADATA_KEY_APPID = "com.flyme.push.app_id";

//    public static final String METADATA_KEY_APPKEY = "com.flyme.push.app_key";

    private static String mzAppid;

    private static String mzAppKey;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private volatile static MZPushRegister mInstance;

    private MZPushRegister(Context applicationContext) {
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
    public static MZPushRegister getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (MZPushRegister.class) {
                if (mInstance == null) {
                    mInstance = new MZPushRegister(applicationContext);
                }
            }
        }
        return mInstance;
    }

    private String getMZAppId() {
        if (NullUtils.checkNull(mzAppid)) {
            throw new NullPointerException("Meizu appId must not null");
        }
        return mzAppid;
    }

    private String getMZAppKey() {
        if (NullUtils.checkNull(mzAppKey)) {
            throw new NullPointerException("Meizu appKey must not null");
        }
        return mzAppKey;
    }

    /**
     * 魅族推送服务注册
     * @param appId 魅族appid
     * @param appKey 魅族appkey
     * @param callback 回调
     */
    public void register(String appId, String appKey, IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("MEIZU is Support Push");
            MZPushRegister.setPushCallback(callback);
            mzAppid = appId;
            mzAppKey = appKey;
            PushManager.register(mContext, getMZAppId(), getMZAppKey());
        }
    }

    @Override
    public void unregister() {
        PushManager.unRegister(mContext, getMZAppId(), getMZAppKey());
    }

    @Override
    public boolean isSupportPush() {
        return RomUtils.isMeizu();
    }

    @Override
    public void setAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        PushManager.subScribeAlias(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), alias);
    }

    @Override
    public void unsetAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        PushManager.unSubScribeAlias(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), alias);
    }

    @Override
    public void getAliases() {
        PushManager.checkSubScribeAlias(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext));
    }

    @Override
    public void setTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        PushManager.subScribeTags(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), tag);
    }

    @Override
    public void unsetTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        PushManager.unSubScribeTags(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), tag);
    }

    @Override
    public void getTags() {
        PushManager.checkSubScribeTags(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext));
    }

    @Override
    public void turnOnPush() {
        PushManager.switchPush(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), true);
    }

    @Override
    public void turnOffPush() {
        PushManager.switchPush(mContext, getMZAppId(), getMZAppKey(), PushManager.getPushId(mContext), false);
    }

    @Override
    public void getRegId() {
        MZPushRegister.getPushCallback().onGetRegId(PushConstants.SUCCESS_CODE,
                Token.buildToken(Target.FLYME, PushManager.getPushId(mContext)));
    }
}
