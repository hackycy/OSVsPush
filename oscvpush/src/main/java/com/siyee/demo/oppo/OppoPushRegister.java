package com.siyee.demo.oppo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.siyee.demo.PushConstants;
import com.siyee.demo.base.IPushCallback;
import com.siyee.demo.base.IPushManager;
import com.siyee.demo.base.PushAdapter;
import com.siyee.demo.model.Target;
import com.siyee.demo.model.Token;
import com.siyee.demo.util.LogUtils;
import com.siyee.demo.util.MetaDataUtils;
import com.siyee.demo.util.NullUtils;

/**
 * OPPO推送服务注册
 * 需要在清单文件Application中注册Oppo应用appkey，appsecret
 * <pre>
 * {@code
 *  <meta-data
 *     android:name="com.heytap.push.app_key"
 *     android:value="appid=xxxxxx" />
 *  <meta-data
 *     android:name="com.heytap.push.app_secret"
 *     android:value="appkey=xxxxxx" />
 * }
 * </pre>
 */
public class OppoPushRegister implements IPushManager {

    public static final String METADATA_KEY_APPKEY = "com.heytap.push.app_key";

    public static final String METADATA_KEY_APPSECRET = "com.heytap.push.app_secret";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private volatile static OppoPushRegister mInstance;

    private OppoPushRegister(Context applicationContext) {
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
    public static OppoPushRegister getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (OppoPushRegister.class) {
                if (mInstance == null) {
                    mInstance = new OppoPushRegister(applicationContext);
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
    public void register(final IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("OPPO is Support Push");
            OppoPushRegister.setPushCallback(callback);
            HeytapPushManager.register(mContext, MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPKEY),
                    MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPSECRET), null);
            HeytapPushManager.setPushCallback(new ICallBackResultService() {
                @Override
                public void onRegister(int i, String s) {
                    OppoPushRegister.getPushCallback().onRegister(i, Token.buildToken(Target.OPPO, s));
                }

                @Override
                public void onUnRegister(int i) {
                    OppoPushRegister.getPushCallback().onUnRegister(i);
                }

                @Override
                public void onSetPushTime(int i, String s) {

                }

                @Override
                public void onGetPushStatus(int i, int i1) {

                }

                @Override
                public void onGetNotificationStatus(int i, int i1) {

                }
            });
        }
    }

    @Override
    public void unregister() {
        HeytapPushManager.unRegister();
    }

    @Override
    public boolean isSupportPush() {
        // OPPO 注册推送服务必须要调用init(...)接口，才能执行后续操作
        HeytapPushManager.init(mContext, PushConstants.DEBUG);
        return HeytapPushManager.isSupportPush();
    }

    @Override
    public void setAliases(String alias) {

    }

    @Override
    public void unsetAliases(String alias) {

    }

    @Override
    public void getAliases() {

    }

    @Override
    public void setTags(String tag) {

    }

    @Override
    public void unsetTags(String tag) {

    }

    @Override
    public void getTags() {

    }

    @Override
    public void turnOnPush() {
        HeytapPushManager.resumePush();
    }

    @Override
    public void turnOffPush() {
        HeytapPushManager.pausePush();
    }

    @Override
    public void getRegId() {
        String regId = HeytapPushManager.getRegisterID();
        if (NullUtils.checkNull(regId)) {
            OppoPushRegister.getPushCallback().onGetRegId(PushConstants.UNKNOWN_CODE, null);
        } else {
            OppoPushRegister.getPushCallback().onGetRegId(PushConstants.SUCCESS_CODE, Token.buildToken(Target.OPPO, regId));
        }
    }
}
