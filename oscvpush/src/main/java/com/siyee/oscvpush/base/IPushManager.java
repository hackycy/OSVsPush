package com.siyee.oscvpush.base;

/**
 * 推送管理统一接口
 */
public interface IPushManager {

    /**
     * 注册推送服务
     * 由于各大推送服务注册条件不一，又子类自定义，无抽象
     */
//    void register(IPushCallback callback);

    /**
     * 解注册推送服务
     */
    void unregister();

    /**
     * 判断是否手机平台是否支持当前平台推送
     * @return true 则支持否则反之
     */
    boolean isSupportPush();

    /**
     * 为指定用户设置alias
     * @param alias 别名
     */
    void setAliases(String alias);

    /**
     *  取消指定用户的aliases
     * @param alias 别名
     */
    void unsetAliases(String alias);

    /**
     * 方法返回了客户端设置的别名列表(如果客户端没有设置别名，则返回空列表)
     */
    void getAliases();

    /**
     * 为某个用户设置订阅topic
     * @param tag topic
     */
    void setTags(String tag);

    /**
     * 取消某个用户的订阅topic
     * @param tag topic
     */
    void unsetTags(String tag);

    /**
     * 方法返回了客户端订阅的主题列表(如果客户端没有订阅主题，则返回空列表)
     */
    void getTags();

//    /**
//     * 为指定用户设置userAccount
//     * @param account userAccount
//     */
//    void setUserAccount(String account);
//
//    /**
//     * 取消指定用户的userAccount
//     * @param account
//     */
//    void unsetUserAccount(String account);
//
//    /**
//     * 设置接收推送服务推送的时段
//     * @param startHour
//     * @param startMin
//     * @param endHour
//     * @param endMin
//     */
//    void setAcceptTime(int startHour, int startMin, int endHour, int endMin);

    /**
     * 恢复接收服务推送的消息
     */
    void turnOnPush();

    /**
     * 暂停接收服务推送的消息
     */
    void turnOffPush();

    /**
     * 获取客户端唯一标识
     */
    void getRegId();

}
