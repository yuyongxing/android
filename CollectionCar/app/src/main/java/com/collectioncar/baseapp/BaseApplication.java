package com.collectioncar.baseapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;
/**
 * APPLICATION
 */
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    public static Handler handler;

    public static Context context;

    public static Thread mainThread;

    public static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();
        mainThreadId = android.os.Process.myTid();
        //极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        baseApplication = this;
    }

    public static Context getAppContext() {

        return baseApplication;
    }
    public static Resources getAppResources() {

        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
