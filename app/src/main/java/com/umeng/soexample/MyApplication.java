package com.umeng.soexample;

import android.app.Application;

import com.umeng.share.ShareConfig;

/**
 * Created by ibm on 2017/8/24.
 */

public class MyApplication extends Application {
    public static final String APP_ID_WX = "wxdc1e388c3822c80b";// 微信开放平台
    public static final String APP_WECHAT_SECRET = "3baf1193c85774b3fd9d18447d76cab0";
    public static final String APP_ID_QQ = "100424468";//QQ平台ID
    public static final String APP_KEY_QQ = "c7394704798a158208a74ab60104f0ba";//QQ平台APP_KEY
    public static final String APP_KEY_WEIBO = "3921700954";//微博平台APP_KEY
    public static final String APP_SECRET_WEIBO = "04b48b094faeb16683c32669824ebdad";//微博平台APP_KEY
    public static final String REDIRECT_URL_WEIBO = "http://sns.whalecloud.com";//微博平台应用回调页
    private static MyApplication mApplication;

    public static synchronized Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        ShareConfig.get(this);
        ShareConfig.setDebug(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        ShareConfig.setWeixin(APP_ID_WX, APP_WECHAT_SECRET);
        ShareConfig.setSinaWeibo(APP_KEY_WEIBO, APP_SECRET_WEIBO, REDIRECT_URL_WEIBO);
        ShareConfig.setQQZone(APP_ID_QQ, APP_KEY_QQ);
    }
}
