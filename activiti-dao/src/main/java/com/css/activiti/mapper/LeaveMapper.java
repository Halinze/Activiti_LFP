package com.css.activiti.mapper;

import com.css.activiti.model.Leave;

import java.util.List;
import java.util.Map;

/**
 * 业务管理
 * Created by 46597 on 2018/3/9.
 */
public interface LeaveMapper {


    public List<Leave> leavePage(Map<String,Object> map);

    public int leaveCount(Map<String,Object> map);

    public int addLeave(Leave leave);

    public  Leave findById(String id );

    public int updateLeave(Leave leave);

    /**
     *
     * @param processInstanceId
     * @return
     */
    public Leave getLeaveByTaskId(String processInstanceId);




}
