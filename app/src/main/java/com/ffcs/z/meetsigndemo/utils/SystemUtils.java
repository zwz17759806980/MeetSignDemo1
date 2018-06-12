package com.ffcs.z.meetsigndemo.utils;

import java.lang.reflect.Method;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class SystemUtils {


    //获取设备序列号
    public static String getSerialNumber(){
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get =c.getMethod("get", String.class);

            serial = (String)get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }
        return serial;

    }
}
