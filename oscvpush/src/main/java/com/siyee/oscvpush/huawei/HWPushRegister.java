package com.siyee.oscvpush.huawei;

import android.annotation.SuppressLint;
import android.content.Context;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
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

/**
 * 华为推送服务注册
 * 需要在清单文件Application中注册华为应用AppID
 * <pre>
 * {@code
 *  <meta-data
 *     android:name="com.huawei.hms.client.appid"
 *     android:value="appid=xxxxxx" />
 * }
 * </pre>
 */
public class HWPushRegister implements IPushManager {

    public static final String HCM = "HCM";

    public static final String METADATA_KEY_APPID = "com.huawei.hms.client.appid";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private volatile static HWPushRegister mInstance;

    private HWPushRegister(Context applicationContext) {
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
    public static HWPushRegister getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (HWPushRegister.class) {
                if (mInstance == null) {
                    mInstance = new HWPushRegister(applicationContext);
                }
            }
        }
        return mInstance;
    }

    private String getAppId() {
        String orginAppId = MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPID);
        if (NullUtils.checkNull(orginAppId)) {
            throw new NullPointerException("huawei appid must not null");
        }
        return orginAppId.replace("appid=", "");
    }

    /**
     * 华为推送服务注册
     * @param callback
     */
    public void register(IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("HUAWEI is Support Push");
            HWPushRegister.setPushCallback(callback);
            // 开发者在获取PUSH Token前需要先获取HmsInstanceId类实例然后才能调用类中的其他方法，例如getId、getAAID等。
            // 获取接入华为推送服务所需的令牌。如果本地没有生成AAID，调用本方法会自动生成AAID，Push服务端生成令牌时依赖AAID。此接口为同步接口，请不要在主线程中调用，以免阻塞主线程。
            new Thread() {
                @Override
                public void run() {
                    try {
                        HmsInstanceId.getInstance(mContext).getToken(getAppId(), HCM);
                    } catch (ApiException e) {
                        LogUtils.e(e.getMessage());
                    }
                }
            }.start();
        }
    }

    @Override
    public void unregister() {

    }

    @Override
    public boolean isSupportPush() {
        return RomUtils.isHuawei();
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
    public void setTags(final String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        HmsMessaging.getInstance(mContext).subscribe(tag).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    HWPushRegister.getPushCallback().onSetTags(PushConstants.SUCCESS_CODE, null);
                } else {
                    HWPushRegister.getPushCallback().onSetTags(PushConstants.UNKNOWN_CODE, null);
                }
            }
        });
    }

    @Override
    public void unsetTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        HmsMessaging.getInstance(mContext).unsubscribe(tag).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    HWPushRegister.getPushCallback().onUnsetTags(PushConstants.SUCCESS_CODE, null);
                } else {
                    HWPushRegister.getPushCallback().onUnsetTags(PushConstants.UNKNOWN_CODE, null);
                }
            }
        });
    }

    @Override
    public void getTags() {
    }

    @Override
    public void turnOnPush() {
        HmsMessaging.getInstance(mContext).turnOnPush();
    }

    @Override
    public void turnOffPush() {
        HmsMessaging.getInstance(mContext).turnOffPush();
    }

    @Override
    public void getRegId() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String regId = HmsInstanceId.getInstance(mContext)
                            .getToken(MetaDataUtils.getMetaDataInApp(mContext, METADATA_KEY_APPID), HCM);
                    HWPushRegister.getPushCallback().onGetRegId(PushConstants.SUCCESS_CODE, Token.buildToken(Target.HUAWEI, regId));
                } catch (ApiException e) {
                    LogUtils.e(e.getMessage());
                    HWPushRegister.getPushCallback().onGetRegId(PushConstants.UNKNOWN_CODE, null);
                }
            }
        }.start();
    }

}
