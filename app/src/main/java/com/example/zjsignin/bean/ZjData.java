package com.example.zjsignin.bean;

import java.io.Serializable;

 
public class ZjData implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2023-03-09 10:02:54
     * updateBy : null
     * groupBy : null
     * dateFormat : null
     * dateType : null
     * updateTime : 2023-03-09 10:07:18
     * beginCreateTime : null
     * endCreateTime : null
     * beginCreateDate : null
     * endCreateDate : null
     * remark : null
     * params : {}
     * id : 187
     * signUpId : 239
     * name : 测试签到
     * delTf : 1
     * personChargeId : 526
     * personChargeName : null
     * personChargeMobile : null
     * status : 1
     * voiceStatus : 1
     * location : null
     * startTime : 2023-03-09 00:00
     * endTime : 2023-03-10 00:00
     * address : 地点
     * addressStatus : 2
     * signUpType : 2
     * userMeetingTypes : null
     * leveStatus : null
     * meetingId : 39
     * loginTimeStatus : 2
     * insertUserInfoStatus : 2
     * bingStatus : 1
     * showUserStatus : 2
     * signUpNumStatus : 1
     * signUpNum : 1
     * useAllStatus : null
     * totalUserCount : null
     * currentUserCount : null
     * signUpStatus : 2
     * ticketIds : null
     * shockStatus : 2
     * autoStatus : 2
     * okMsg : 成功签到
     * repeatMsg : 重复签到
     * failedMsg : 识别失败
     * timeLong : 1
     * type : 1
     * businessId : null
     * percent : null
     * signUpCount : null
     * levelCount : null
     * siteName : null
     * personChargeUsername : null
     * speechStatus : 1
     * userType : null
     * userId : null
     * meetingFormList : null
     * userMeetingFormFiledDTOList : null
     * validConditionStringDTO : null
     * codeNo : 39A239B187
     */

    private String searchValue;
    private String deviceImg;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String groupBy;
    private String dateFormat;
    private String dateType;
    private String updateTime;
    private String beginCreateTime;
    private String endCreateTime;
    private String beginCreateDate;
    private String endCreateDate;
    private String remark;

    private int id;
    private int signUpId;
    private String name;
    private int delTf;
    private int personChargeId;
    private String personChargeName;
    private String personChargeMobile;
    private int status;
    private int voiceStatus;
    private String location;
    private String startTime;
    private String endTime;
    private String address;
    private String addressStatus;
    private int signUpType;
    private String userMeetingTypes;
    private String leveStatus;
    private String meetingId;
    private int loginTimeStatus;
    private int faceDetect = 1;
    private int insertUserInfoStatus;
    private int bingStatus;
    private int showUserStatus;
    private int signUpNumStatus;
    private int signUpNum;

    private String useAllStatus;
    private String totalUserCount;
    private String currentUserCount;
    private int signUpStatus;
    private String ticketIds;
    private String shockStatus;
    private int autoStatus;
    private String okMsg;
    private String repeatMsg;
    private String failedMsg;
    private int timeLong;
    private int type;
    private String businessId;
    private String percent;
    private String signUpCount;
    private String levelCount;
    private String siteName;
    private String personChargeUsername;
    private String speechStatus;
    private String userType;
    private String userId;
    private String meetingFormList;
    private String userMeetingFormFiledDTOList;
    private String validConditionStringDTO;
    private String codeNo = "";

    public String getDeviceImg() {
        return deviceImg;
    }

    public void setDeviceImg(String deviceImg) {
        this.deviceImg = deviceImg;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(String beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public String getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(String endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(int signUpId) {
        this.signUpId = signUpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelTf() {
        return delTf;
    }

    public void setDelTf(int delTf) {
        this.delTf = delTf;
    }

    public int getPersonChargeId() {
        return personChargeId;
    }

    public void setPersonChargeId(int personChargeId) {
        this.personChargeId = personChargeId;
    }

    public String getPersonChargeName() {
        return personChargeName;
    }

    public void setPersonChargeName(String personChargeName) {
        this.personChargeName = personChargeName;
    }

    public String getPersonChargeMobile() {
        return personChargeMobile;
    }

    public void setPersonChargeMobile(String personChargeMobile) {
        this.personChargeMobile = personChargeMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVoiceStatus() {
        return voiceStatus;
    }

    public void setVoiceStatus(int voiceStatus) {
        this.voiceStatus = voiceStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public int getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(int signUpType) {
        this.signUpType = signUpType;
    }

    public String getUserMeetingTypes() {
        return userMeetingTypes;
    }

    public void setUserMeetingTypes(String userMeetingTypes) {
        this.userMeetingTypes = userMeetingTypes;
    }

    public String getLeveStatus() {
        return leveStatus;
    }

    public void setLeveStatus(String leveStatus) {
        this.leveStatus = leveStatus;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getFaceDetect() {
        return faceDetect;
    }

    public void setFaceDetect(int faceDetect) {
        this.faceDetect = faceDetect;
    }
    public int getLoginTimeStatus() {
        return loginTimeStatus;
    }

    public void setLoginTimeStatus(int loginTimeStatus) {
        this.loginTimeStatus = loginTimeStatus;
    }

    public int getInsertUserInfoStatus() {
        return insertUserInfoStatus;
    }

    public void setInsertUserInfoStatus(int insertUserInfoStatus) {
        this.insertUserInfoStatus = insertUserInfoStatus;
    }

    public int getBingStatus() {
        return bingStatus;
    }

    public void setBingStatus(int bingStatus) {
        this.bingStatus = bingStatus;
    }

    public int getShowUserStatus() {
        return showUserStatus;
    }

    public void setShowUserStatus(int showUserStatus) {
        this.showUserStatus = showUserStatus;
    }

    public int getSignUpNumStatus() {
        return signUpNumStatus;
    }

    public void setSignUpNumStatus(int signUpNumStatus) {
        this.signUpNumStatus = signUpNumStatus;
    }

    public int getSignUpNum() {
        return signUpNum;
    }

    public void setSignUpNum(int signUpNum) {
        this.signUpNum = signUpNum;
    }

    public String getUseAllStatus() {
        return useAllStatus;
    }

    public void setUseAllStatus(String useAllStatus) {
        this.useAllStatus = useAllStatus;
    }

    public String getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(String totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public String getCurrentUserCount() {
        return currentUserCount;
    }

    public void setCurrentUserCount(String currentUserCount) {
        this.currentUserCount = currentUserCount;
    }

    public int getSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(int signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public String getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(String ticketIds) {
        this.ticketIds = ticketIds;
    }

    public String getShockStatus() {
        return shockStatus;
    }

    public void setShockStatus(String shockStatus) {
        this.shockStatus = shockStatus;
    }

    public int getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(int autoStatus) {
        this.autoStatus = autoStatus;
    }

    public String getOkMsg() {
        return okMsg;
    }

    public void setOkMsg(String okMsg) {
        this.okMsg = okMsg;
    }

    public String getRepeatMsg() {
        return repeatMsg;
    }

    public void setRepeatMsg(String repeatMsg) {
        this.repeatMsg = repeatMsg;
    }

    public String getFailedMsg() {
        return failedMsg;
    }

    public void setFailedMsg(String failedMsg) {
        this.failedMsg = failedMsg;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSignUpCount() {
        return signUpCount;
    }

    public void setSignUpCount(String signUpCount) {
        this.signUpCount = signUpCount;
    }

    public String getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(String levelCount) {
        this.levelCount = levelCount;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPersonChargeUsername() {
        return personChargeUsername;
    }

    public void setPersonChargeUsername(String personChargeUsername) {
        this.personChargeUsername = personChargeUsername;
    }

    public String getSpeechStatus() {
        return speechStatus;
    }

    public void setSpeechStatus(String speechStatus) {
        this.speechStatus = speechStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeetingFormList() {
        return meetingFormList;
    }

    public void setMeetingFormList(String meetingFormList) {
        this.meetingFormList = meetingFormList;
    }

    public String getUserMeetingFormFiledDTOList() {
        return userMeetingFormFiledDTOList;
    }

    public void setUserMeetingFormFiledDTOList(String userMeetingFormFiledDTOList) {
        this.userMeetingFormFiledDTOList = userMeetingFormFiledDTOList;
    }

    public String getValidConditionStringDTO() {
        return validConditionStringDTO;
    }

    public void setValidConditionStringDTO(String validConditionStringDTO) {
        this.validConditionStringDTO = validConditionStringDTO;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    @Override
    public String toString() {
        return "ZjData{" +
                "searchValue='" + searchValue + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", groupBy='" + groupBy + '\'' +
                ", dateFormat='" + dateFormat + '\'' +
                ", dateType='" + dateType + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", beginCreateTime='" + beginCreateTime + '\'' +
                ", endCreateTime='" + endCreateTime + '\'' +
                ", beginCreateDate='" + beginCreateDate + '\'' +
                ", endCreateDate='" + endCreateDate + '\'' +
                ", remark='" + remark + '\'' +
                ", id=" + id +
                ", signUpId=" + signUpId +
                ", name='" + name + '\'' +
                ", delTf=" + delTf +
                ", personChargeId=" + personChargeId +
                ", personChargeName='" + personChargeName + '\'' +
                ", personChargeMobile='" + personChargeMobile + '\'' +
                ", status=" + status +
                ", voiceStatus=" + voiceStatus +
                ", location='" + location + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", address='" + address + '\'' +
                ", addressStatus='" + addressStatus + '\'' +
                ", signUpType=" + signUpType +
                ", userMeetingTypes='" + userMeetingTypes + '\'' +
                ", leveStatus='" + leveStatus + '\'' +
                ", meetingId=" + meetingId +
                ", loginTimeStatus=" + loginTimeStatus +
                ", insertUserInfoStatus=" + insertUserInfoStatus +
                ", bingStatus=" + bingStatus +
                ", showUserStatus=" + showUserStatus +
                ", signUpNumStatus=" + signUpNumStatus +
                ", signUpNum=" + signUpNum +
                ", useAllStatus='" + useAllStatus + '\'' +
                ", totalUserCount='" + totalUserCount + '\'' +
                ", currentUserCount='" + currentUserCount + '\'' +
                ", signUpStatus=" + signUpStatus +
                ", ticketIds='" + ticketIds + '\'' +
                ", shockStatus=" + shockStatus +
                ", autoStatus=" + autoStatus +
                ", okMsg='" + okMsg + '\'' +
                ", repeatMsg='" + repeatMsg + '\'' +
                ", failedMsg='" + failedMsg + '\'' +
                ", timeLong=" + timeLong +
                ", type=" + type +
                ", businessId='" + businessId + '\'' +
                ", percent='" + percent + '\'' +
                ", signUpCount='" + signUpCount + '\'' +
                ", levelCount='" + levelCount + '\'' +
                ", siteName='" + siteName + '\'' +
                ", personChargeUsername='" + personChargeUsername + '\'' +
                ", speechStatus='" + speechStatus + '\'' +
                ", userType='" + userType + '\'' +
                ", userId='" + userId + '\'' +
                ", meetingFormList='" + meetingFormList + '\'' +
                ", userMeetingFormFiledDTOList='" + userMeetingFormFiledDTOList + '\'' +
                ", validConditionStringDTO='" + validConditionStringDTO + '\'' +
                ", codeNo='" + codeNo + '\'' +
                '}';
    }
}
