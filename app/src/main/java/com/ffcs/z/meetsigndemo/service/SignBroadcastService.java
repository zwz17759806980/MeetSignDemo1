package com.ffcs.z.meetsigndemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ffcs.z.meetsigndemo.bean.Api;
import com.ffcs.z.meetsigndemo.bean.SignBean;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class SignBroadcastService extends Service {

    private boolean connecting = true;
    private long queryTime = 5000;
    private int currentNet=1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Intent intent = new Intent("com.ffcs.z.RECEIVER");


    /**
     * 模拟下载任务，每秒钟更新一次
     */
    public void startQuery() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int dinnerSignStatus = 3;
                while (connecting) {
                    try {
                        RequestParams requestParams = new RequestParams(Api.QUERY_SIGN);
                        SignBean baseBean = x.http().postSync(requestParams, SignBean.class);
                        if (baseBean.getReturnCode().equals("1")) {
                            SignBean.Meetinginfos bean = baseBean.getMeetinginfos();
                            if (bean != null) {
                                if (bean.getMeetingSignStatus() != dinnerSignStatus) {
                                    dinnerSignStatus = bean.getMeetingSignStatus();
                                    baseBean.setNetWork(0);
                                    intent.putExtra("SignBean", baseBean);
                                    sendBroadcast(intent);
                                }


                            } else {
                                dinnerSignStatus = 3;
                                baseBean.setNetWork(0);
                                intent.putExtra("SignBean", baseBean);
                                sendBroadcast(intent);
                            }

                        }

                        Thread.sleep(queryTime);
                    } catch (Exception e) {
                        SignBean beans=new SignBean();
                        beans.setNetWork(1);

                        intent.putExtra("SignBean", beans);
                        sendBroadcast(intent);
//                        e.printStackTrace();
                    } catch (Throwable throwable) {
//                        throwable.printStackTrace();
                        Log.i("DdDD", "2:" + throwable.getMessage());
                    }
                }
            }
        }).start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startQuery();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connecting = false;
    }
}
