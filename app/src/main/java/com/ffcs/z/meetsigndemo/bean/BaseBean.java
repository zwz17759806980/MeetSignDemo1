package com.ffcs.z.meetsigndemo.bean;

import java.io.Serializable;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class BaseBean implements Serializable {

    private String apiMethod;
    private String returnCode;
    private String subCode;
    private String subMsg;


    private FaceWebInfoApi result;
    private SignBean DinnerInfos;

    public FaceWebInfoApi getResult() {
        return result;
    }
    public void setResult(FaceWebInfoApi result) {
        this.result = result;
    }

    public SignBean getDinnerInfos() {
        return DinnerInfos;
    }

    public void setDinnerInfos(SignBean dinnerInfos) {
        DinnerInfos = dinnerInfos;
    }

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

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }





}


