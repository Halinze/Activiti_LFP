package com.css.activiti.controller;

import com.css.activiti.model.Leave;
import com.css.activiti.model.PageInfo;
import com.css.activiti.model.User;
import com.css.activiti.service.LeaveService;
import com.css.activiti.util.DateJsonValueProcessor;
import com.css.activiti.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 46597 on 2018/3/10.
 */
@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;


    @RequestMapping("/leavePage")
    public String leavePage(HttpServletResponse response ,int rows ,
                            Integer page ,String userId) throws Exception {
        PageInfo<Leave> leavePage = new PageInfo<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId" ,userId);
        leavePage.setPageSize(rows);

        if(page == null || page <= 0  ){
            page = 1;
        }
        leavePage.setPageIndex((page-1) * rows);

        map.put("pageIndex",leavePage.getPageIndex());
        map.put("pageSize",leavePage.getPageSize());

        int leaveCount= leaveService.leaveCount(map);
        List<Leave> leaveList = leaveService.leavePage(map);


        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));

        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(leaveList, jsonConfig);
        result.put("rows",jsonArray);
        result.put("total",leaveCount);
        ResponseUtil.write(response,result);
        return null;
    }



    /**
     * 添加 请假单
     * @param leave
     * @param response
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(Leave leave , HttpServletResponse response, String userId) throws Exception {

        User user = new User();
        System.out.println(userId);
        user.setId(userId);
        int resutlTotal = 0 ;
        leave.setLeaveDate(new Date());
        leave.setUser(user);
        resutlTotal = leaveService.add(leave);
        JSONObject result = new JSONObject();
        if(resutlTotal > 0 ){
            result.put("success",true);
        }else {
            result.put("success",false);
        }
        ResponseUtil.write(response,result);
        return null;    
    }

    /**
     * 提交请假申请流程  很关键
     * @param response
     * @param leaveId
     * @return
     */
    @RequestMapping("/startApply")
    public String startApply(HttpServletResponse response , String leaveId) throws Exception {

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("leaveId",leaveId);
        //启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("activitiemployeeProcess", variables);

        //根据流程实例Id 查询任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
        //完成填写请假单的任务
        taskService.complete(task.getId());
        Leave leave = leaveService.findById(leaveId);
        leave.setState("审核中");
        leave.setProcessInstanceId(pi.getProcessInstanceId());


        //修改请假单状态
        leaveService.updateLeave(leave);
        JSONObject result = new JSONObject();
        result.put("success",true);
        ResponseUtil.write(response,result);
        return null;


    }


    /**
     * 查询流程信息？  填充审核页面！
     *
     * @param response
     * @param taskId
     * @return
     */
    @RequestMapping("/getLeaveByTaskId")
    public String getLeaveByTaskId(HttpServletResponse response, String taskId) throws Exception {

        System.out.println("caoshisheng");
        //这里是个联系点！
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        Leave leave = leaveService.getLeaveByTaskId(task.getProcessInstanceId());

        JSONObject result = new JSONObject();
        result.put("leave",JSONObject.fromObject(leave));
        ResponseUtil.write(response,result);


        return null;
    }






}
