package com.ffcs.z.meetsigndemo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.xutils.x;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/23
 * 描述 ：
 * ============================================
 */

public class App extends Application {
    public static Context ctx;
    private static int mainThreadId;
    private static Handler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
        ctx = getApplicationContext();
        mainThreadId = android.os.Process.myTid();// 获取当前主线程id
        handler = new Handler();
    }


    public static Context getCtx() {
        return ctx;
    }
    public static int getMainThreadId() {
        return mainThreadId;
    }
    public static Handler getHandler() {
        return handler;
    }


}
