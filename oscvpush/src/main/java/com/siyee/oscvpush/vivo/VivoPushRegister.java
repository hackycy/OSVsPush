package com.siyee.oscvpush.vivo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.base.IPushCallback;
import com.siyee.oscvpush.base.IPushManager;
import com.siyee.oscvpush.base.PushAdapter;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.util.NullUtils;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Vivo推送服务注册
 * 需要在清单文件中配置一下key
 * <pre>
 * {@code
 *  <meta-data
 *   android:name="com.vivo.push.api_key"
 *   android:value="xxxxxxxx"/>
 *  <meta-data
 *   android:name="com.vivo.push.app_id"
 *   android:value="xxxx"/>
 * }
 * </pre>
 */
public class VivoPushRegister implements IPushManager {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @SuppressLint("StaticFieldLeak")
    private volatile static VivoPushRegister mInstance;

    private VivoPushRegister(Context applicationContext) {
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
    public static VivoPushRegister getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (VivoPushRegister.class) {
                if (mInstance == null) {
                    mInstance = new VivoPushRegister(applicationContext);
                }
            }
        }
        return mInstance;
    }

    /**
     * vivo推送服务注册
     * @param callback 回调
     */
    public void register(IPushCallback callback) {
        if (isSupportPush()) {
            LogUtils.e("VIVO is Support Push");
            VivoPushRegister.setPushCallback(callback);
            PushClient.getInstance(mContext).initialize();
            turnOnPush();
        }
    }

    @Override
    public void unregister() {
    }

    @Override
    public boolean isSupportPush() {
        return PushClient.getInstance(mContext).isSupport();
    }

    @Override
    public void setAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        PushClient.getInstance(mContext).bindAlias(alias, new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == PushConstants.SUCCESS_CODE) {
                    VivoPushRegister.getPushCallback().onSetAliases(PushConstants.SUCCESS_CODE, null);
                } else {
                    VivoPushRegister.getPushCallback().onSetAliases(i, null);
                }
            }
        });
    }

    @Override
    public void unsetAliases(String alias) {
        if (NullUtils.checkNull(alias)) {
            return;
        }
        PushClient.getInstance(mContext).unBindAlias(alias, new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == PushConstants.SUCCESS_CODE) {
                    VivoPushRegister.getPushCallback().onUnsetAliases(PushConstants.SUCCESS_CODE, null);
                } else {
                    VivoPushRegister.getPushCallback().onUnsetAliases(i, null);
                }
            }
        });
    }

    @Override
    public void getAliases() {
        String alias = PushClient.getInstance(mContext).getAlias();
        List<String> aliaes = new ArrayList<>();
        if (!NullUtils.checkNull(aliaes)) {
            aliaes.add(alias);
        }
        VivoPushRegister.getPushCallback().onGetAliases(PushConstants.SUCCESS_CODE, aliaes);
    }

    @Override
    public void setTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        PushClient.getInstance(mContext).setTopic(tag, new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == PushConstants.SUCCESS_CODE) {
                    VivoPushRegister.getPushCallback().onSetTags(PushConstants.SUCCESS_CODE, null);
                } else {
                    VivoPushRegister.getPushCallback().onSetTags(i, null);
                }
            }
        });
    }

    @Override
    public void unsetTags(String tag) {
        if (NullUtils.checkNull(tag)) {
            return;
        }
        PushClient.getInstance(mContext).delTopic(tag, new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                if (i == PushConstants.SUCCESS_CODE) {
                    VivoPushRegister.getPushCallback().onUnsetTags(PushConstants.SUCCESS_CODE, null);
                } else {
                    VivoPushRegister.getPushCallback().onUnsetTags(i, null);
                }
            }
        });
    }

    @Override
    public void getTags() {
        VivoPushRegister.getPushCallback().onGetTags(PushConstants.SUCCESS_CODE,
                PushClient.getInstance(mContext).getTopics());
    }

    @Override
    public void turnOnPush() {
        PushClient.getInstance(mContext).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                LogUtils.e("vivo turnOnPush code: " + i);
            }
        });
    }

    @Override
    public void turnOffPush() {
        PushClient.getInstance(mContext).turnOffPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
                LogUtils.e("vivo turnOffPush code: " + i);
            }
        });
    }

    @Override
    public void getRegId() {
        String regId = PushClient.getInstance(mContext).getRegId();
        if (NullUtils.checkNull(regId)) {
            VivoPushRegister.getPushCallback().onGetRegId(PushConstants.UNKNOWN_CODE, null);
        } else {
            VivoPushRegister.getPushCallback().onGetRegId(PushConstants.SUCCESS_CODE, Token.buildToken(Target.VIVO, regId));
        }
    }

}
