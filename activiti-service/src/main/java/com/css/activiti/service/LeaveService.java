package com.css.activiti.service;

import com.css.activiti.mapper.LeaveMapper;
import com.css.activiti.model.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 46597 on 2018/3/10.
 */
@Service
public class LeaveService {

    @Autowired
    private LeaveMapper leaveMapper;


    //todo  编程式事务的代码还没加上
    public int add(Leave leave) {
       return leaveMapper.addLeave(leave);
    }


    public List<Leave> leavePage(Map<String,Object> map){
        return leaveMapper.leavePage(map);
    }

    public int leaveCount(Map<String,Object> map){
        return leaveMapper.leaveCount(map);
    }

    public Leave findById(String id){
        return leaveMapper.findById(id);
    }

    public int updateLeave(Leave leave){
        return leaveMapper.updateLeave(leave);
    }

    public Leave getLeaveByTaskId(String processInstanceId) {

        return leaveMapper.getLeaveByTaskId(processInstanceId);
    }
}
