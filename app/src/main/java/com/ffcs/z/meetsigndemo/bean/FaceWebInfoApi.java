package com.ffcs.z.meetsigndemo.bean;

import java.io.Serializable;

/**
 * ===========================================
 * 作者 ;//曾立强 3042938728@qq.com
 * 时间 ;//2018/5/24
 * 描述 ;//
 * ============================================
 */

public class FaceWebInfoApi implements Serializable {

    private String faceImageId;//抓拍编号
    private String faceImageUrl;//头像Url
    private String pictureUrl;//场景图
    private Boolean isHit;//是否命中
    private String deviceName;//设备名称
    private String passTime;//抓拍时间
    private Integer hitFaceImageId;//员工编号
    private String hitFacename;//员工姓名
    private String hitFaceImageUrl;// 命中员工图片
    private String desc;//setFaceImageId
    private String toTarget;
    private Integer isExistSign;//是否签到过  1签到过 0未签到过
    private String similarity;//相似度
    private String audioType;

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public String getFaceImageId() {
        return faceImageId;
    }

    public void setFaceImageId(String faceImageId) {
        this.faceImageId = faceImageId;
    }

    public String getFaceImageUrl() {
        return faceImageUrl;
    }

    public void setFaceImageUrl(String faceImageUrl) {
        this.faceImageUrl = faceImageUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getToTarget() {
        return toTarget;
    }

    public void setToTarget(String toTarget) {
        this.toTarget = toTarget;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public Integer getHitFaceImageId() {
        return hitFaceImageId;
    }

    public void setHitFaceImageId(Integer hitFaceImageId) {
        this.hitFaceImageId = hitFaceImageId;
    }

    public String getHitFacename() {
        return hitFacename;
    }

    public void setHitFacename(String hitFacename) {
        this.hitFacename = hitFacename;
    }

    public String getHitFaceImageUrl() {
        return hitFaceImageUrl;
    }

    public void setHitFaceImageUrl(String hitFaceImageUrl) {
        this.hitFaceImageUrl = hitFaceImageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public Boolean getHit() {
        return isHit;
    }

    public void setHit(Boolean hit) {
        isHit = hit;
    }

    public Integer getIsExistSign() {
        return isExistSign;
    }

    public void setIsExistSign(Integer isExistSign) {
        this.isExistSign = isExistSign;
    }
}
