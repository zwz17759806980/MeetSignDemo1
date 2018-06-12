package com.ffcs.z.meetsigndemo.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SignBean implements Serializable {

    private String apiMethod;
    private String returnCode;
    private Meetinginfos meetinginfos;
    private int netWork;

    public class Meetinginfos implements Serializable {
        private String addTime;//": "2018-05-24 15:22:26",
        private String meetingDate;
        private String meetingEndTime;//": "08:00:00",
        private String meetingInfoId;//": 100,
        private Integer meetingSignStatus;//": 0,
        private String meetingStartTime;//: "03:03:00",
        private String meetingTitle;
        private String updateTime;//": "2018-05-24 15:22:26"

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getMeetingDate() {
            return meetingDate;
        }

        public void setMeetingDate(String meetingDate) {
            this.meetingDate = meetingDate;
        }

        public String getMeetingEndTime() {
            return meetingEndTime;
        }

        public void setMeetingEndTime(String meetingEndTime) {
            this.meetingEndTime = meetingEndTime;
        }

        public String getMeetingInfoId() {
            return meetingInfoId;
        }

        public void setMeetingInfoId(String meetingInfoId) {
            this.meetingInfoId = meetingInfoId;
        }

        public Integer getMeetingSignStatus() {
            return meetingSignStatus;
        }

        public void setMeetingSignStatus(Integer meetingSignStatus) {
            this.meetingSignStatus = meetingSignStatus;
        }

        public String getMeetingStartTime() {
            return meetingStartTime;
        }

        public void setMeetingStartTime(String meetingStartTime) {
            this.meetingStartTime = meetingStartTime;
        }

        public String getMeetingTitle() {
            return meetingTitle;
        }

        public void setMeetingTitle(String meetingTitle) {
            this.meetingTitle = meetingTitle;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
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

    public Meetinginfos getMeetinginfos() {
        return meetinginfos;
    }

    public void setMeetinginfos(Meetinginfos meetinginfos) {
        this.meetinginfos = meetinginfos;
    }

    public int getNetWork() {
        return netWork;
    }

    public void setNetWork(int netWork) {
        this.netWork = netWork;
    }
}
