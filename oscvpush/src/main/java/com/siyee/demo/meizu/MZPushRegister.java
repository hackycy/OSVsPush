package com.siyee.demo.meizu;

import android.annotation.SuppressLint;
import android.content.Context;

import com.meizu.cloud.pushsdk.PushManager;
import com.siyee.demo.PushConstants;
import com.siyee.demo.base.IPushCallback;
import com.siyee.demo.base.IPushManager;
import com.siyee.demo.base.PushAdapter;
import com.siyee.demo.model.Target;
import com.siyee.demo.model.Token;
import com.siyee.demo.util.LogUtils;
import com.siyee.demo.util.MetaDataUtils;
import com.siyee.demo.util.NullUtils;
import com.siyee.demo.util.RomUtils;

/**
 * 魅族推送服务注册
 * 需要在清单文件Application中注册魅族应用AppID，appkey
 * <pre>
 * {@code
 *  <meta-data
 *     android:name="com.flyme.push.app_id"
 *     android:value="appid=xxxxxx" />
 *  <meta-data
 *     android:name="com.flyme.push.app_key"
 *     android:value="appkey=xxxxxx" />
 * }
 * </pre>
 */
public class MZPushRegister implements IPushManager {

    public static final String METADATA_KEY_APPID = "com.flyme.push.app_id";

    public static final String METADATA_KEY_APPKEY = "com.flyme.push.app_key";

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
        return MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPID);
    }

    private String getMZAppKey() {
        return MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPKEY);
    }

    @Override
    public void register(IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("MEIZU is Support Push");
            MZPushRegister.setPushCallback(callback);
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
