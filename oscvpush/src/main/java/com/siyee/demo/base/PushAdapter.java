package com.siyee.demo.base;

import com.siyee.demo.model.Message;
import com.siyee.demo.model.Token;

import java.util.List;

/**
 * 空实现IPushCallback
 */
public class PushAdapter implements IPushCallback {
    @Override
    public void onRegister(int resCode, Token regId) {

    }

    @Override
    public void onGetRegId(int resCode, Token regId) {

    }

    @Override
    public void onUnRegister(int resCode) {

    }

    @Override
    public void onSetAliases(int resCode, List<String> aliases) {

    }

    @Override
    public void onUnsetAliases(int resCode, List<String> aliases) {

    }

    @Override
    public void onGetAliases(int resCode, List<String> aliases) {

    }

    @Override
    public void onSetTags(int resCode, List<String> tags) {

    }

    @Override
    public void onUnsetTags(int resCode, List<String> tags) {

    }

    @Override
    public void onGetTags(int resCode, List<String> tags) {

    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onMessageClicked(Message msg) {

    }

    @Override
    public void onMessageThrough(Message msg) {

    }
}
