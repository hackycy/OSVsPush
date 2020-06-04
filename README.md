# 简介

![](https://img.shields.io/badge/release-v1.1.0-green) ![](https://img.shields.io/badge/license-MIT-green)

更方便的将各个rom厂商自己的推送服务进行集成,并统一管理,使用前还是需要熟悉各个平台的相关文档

# 如何集成

gradle引入

``` gradle
allprojects {
    repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
}
```

``` gradle
implementation 'com.github.hackycy:OSVsPush:${版本号}'
```

由于库中并不包含各大推送的sdk，请自行在github的**oscvpush**的[libs](https://github.com/hackycy/OSVsPush/tree/dev/oscvpush/libs)中的厂商推送sdk下载引入到自己的项目中。

并增加华为远程依赖包：

``` gradle
implementation 'com.huawei.hms:push:4.0.2.300'
```

> 华为远程依赖包请在`allprojects ->repositories`里面配置`HMS SDK`的maven仓地址。
>
> ``` gradle
> allprojects {
>     repositories {
>         ...
>         maven {url 'http://developer.huawei.com/repo/'}
>     }
> }
> ```
>
> 在`buildscript ->repositories`里面配置`HMS SDK`的maven仓地址。
>
> ``` gradle
> buildscript {
>     repositories {
>         ...
>         maven {url 'http://developer.huawei.com/repo/'}
>     }
> }
> ```

# 如何使用

**配置厂商推送服务所需key**

``` xml
<!-- 华为appid -->
<meta-data
	android:name="com.huawei.push.app_id"
	android:value="appid=********" />

<!-- vivo appid和appkey -->
<meta-data
	android:name="com.vivo.push.api_key"
	android:value="**********"/>
<meta-data
	android:name="com.vivo.push.app_id"
	android:value="**********"/>
```

> 将*号填写为实际应用的id等

**初始化**

``` java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }

    /**
     * 注册各大厂商推送服务
     */
    private void initPush() {
        // 注册回调
        PushAdapter adapter = new PushAdapter(){

            @Override
            public void onGetRegId(int resCode, Token regId) {
                LogUtils.e(regId);
            }

            @Override
            public void onRegister(int resCode, Token regId) {
                LogUtils.e(regId);
            }
        };
        // 厂商推送注册，sdk会自动判断是否需要注册。
        HWPushRegister.getInstance(this).register(adapter);
        MiPushRegister.getInstance(this).register("appid", "appkey", adapter);
        OppoPushRegister.getInstance(this).register("appkey", "appsecret", adapter);
        VivoPushRegister.getInstance(this).register(adapter);
        MZPushRegister.getInstance(this).register("appid", "appkey", adapter);
    }

}

```

即可完成集成，具体可查看Demo。

# 厂商推送平台链接

小米推送开发文档：
https://dev.mi.com/console/doc/detail?pId=41#_4_1

OPPO推送开发文档：
https://open.oppomobile.com/wiki/doc#id=10704

Vivo推送开发文档：
https://dev.vivo.com.cn/documentCenter/doc/233

华为推送开发文档：
https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/push-basic-capability

FLYME推送开发文档：
http://open-wiki.flyme.cn/doc-wiki/index#id?129
