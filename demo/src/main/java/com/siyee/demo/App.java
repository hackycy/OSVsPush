package com.siyee.demo;

import android.app.Application;
import android.util.Log;

import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.base.PushAdapter;
import com.siyee.oscvpush.huawei.HWPushRegister;
import com.siyee.oscvpush.meizu.MZPushRegister;
import com.siyee.oscvpush.mi.MiPushRegister;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.oppo.OppoPushRegister;
import com.siyee.oscvpush.vivo.VivoPushRegister;

public class App extends Application {

    /**
     * 推送注册回调
     */
    private PushAdapter adapter = new PushAdapter(){

        @Override
        public void onRegister(int resCode, Token regId) {
            if (resCode == PushConstants.SUCCESS_CODE && regId != null) {
                Log.i("Application", regId.getRegId());
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 将*替换成自己的key
        HWPushRegister.getInstance(this).register(adapter);
        MiPushRegister.getInstance(this).register("***********", "***********", adapter);
        OppoPushRegister.getInstance(this).register("***********", "***********", adapter);
        VivoPushRegister.getInstance(this).register(adapter);
        MZPushRegister.getInstance(this).register("***********", "***********", adapter);
    }
}
