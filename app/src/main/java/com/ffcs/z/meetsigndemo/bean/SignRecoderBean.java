package com.ffcs.z.meetsigndemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class SignRecoderBean implements Serializable {
    private String apiMethod;
    private String returnCode;
    private List<FaceWebInfoApi> result;

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public List<FaceWebInfoApi> getResult() {
        return result;
    }

    public void setResult(List<FaceWebInfoApi> result) {
        this.result = result;
    }
}
