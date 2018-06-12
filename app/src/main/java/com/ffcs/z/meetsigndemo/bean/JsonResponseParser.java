package com.ffcs.z.meetsigndemo.bean;

import com.alibaba.fastjson.JSON;

import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/18
 * 描述 ：
 * ============================================
 */

public class JsonResponseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {
        // custom check ?
        // get headers ?
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        // TODO: json to java bean
        if (resultClass == List.class) {
            // fastJson 解析:
            return JSON.parseArray(result, (Class<?>) ParameterizedTypeUtil.getParameterizedType(resultType, List.class, 0));
        } else {
            // fastjson 解析:
            return JSON.parseObject(result, resultClass);
        }

    }
}
