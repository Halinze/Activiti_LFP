package com.css.activiti.model;

import java.util.Date;

/**
 * Created by 46597 on 2018/2/24.
 * <p>
 * 请假单实体
 */
public class Leave {


    private Integer id; //编号
    private User user; // 请假人
    private Date leaveDate; // 请假日期
    private Integer leaveDays;// 请假原因
    private String leaveReason; // 请假原因
    private String state;//状态审核 未提交  审核中 审核通过 审核未通过
    private String processInstanceId; //流程实例id；！！！


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


}
