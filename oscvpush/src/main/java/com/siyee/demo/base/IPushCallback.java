package com.siyee.demo.base;

import com.siyee.demo.model.Message;
import com.siyee.demo.model.Token;

import java.util.List;

/**
 * 推送服务回调接口
 */
public interface IPushCallback {

    /**
     * 注册的结果回调,如果注册成功,registerID就是客户端的唯一身份标识
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param regId 客户端的唯一身份标识
     */
    void onRegister(int resCode, Token regId);

    /**
     * 获取RegId结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param regId 客户端的唯一身份标识
     */
    void onGetRegId(int resCode, Token regId);

    /**
     * 反注册的结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     */
    void onUnRegister(int resCode);

    /**
     * 设置alias操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param aliases 操作成功的aliases列表
     */
    void onSetAliases(int resCode, List<String> aliases);

    /**
     * 取消alias操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param aliases 操作成功的aliases列表
     */
    void onUnsetAliases(int resCode, List<String> aliases);

    /**
     * 获取alias列表操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param aliases 操作成功的aliases列表
     */
    void onGetAliases(int resCode, List<String> aliases);

    /**
     * 设置tag操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param tags 操作成功的tag列表
     */
    void onSetTags(int resCode, List<String> tags);

    /**
     * 取消tag操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param tags 操作成功的tag列表
     */
    void onUnsetTags(int resCode, List<String> tags);

    /**
     * 获取tag操作结果回调
     * @param resCode responseCode来判断操作是否成功，0 代表成功,其他代码失败
     * @param tags 操作成功的tag列表
     */
    void onGetTags(int resCode, List<String> tags);

    /**
     * 通知下来之后
     * @param msg
     */
    void onMessage(Message msg);

    /**
     * 通知栏被点击之后
     * @param msg
     */
    void onMessageClicked(Message msg);

    /**
     * 通知透传消息
     * @param msg
     */
    void onMessageThrough(Message msg);

}
