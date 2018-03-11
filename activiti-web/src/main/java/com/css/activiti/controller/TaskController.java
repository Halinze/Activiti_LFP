package com.css.activiti.controller;

import com.css.activiti.model.MyTask;
import com.css.activiti.model.PageInfo;
import com.css.activiti.util.DateJsonValueProcessor;
import com.css.activiti.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 46597 on 2018/3/11.
 */
@RequestMapping("/task")
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;


    /**
     * 代办流程分页查询
     * @param response
     * @param page
     * @param rows
     * @param s_name
     * @param groupId
     * @return
     */
    @RequestMapping("/taskPage")
    public String taskPage(HttpServletResponse response , String page , String rows ,String s_name , String groupId) throws Exception {

        System.out.println("==========================================");


        if(s_name==null){
            s_name="";
        }
        PageInfo pageInfo=new PageInfo();
        Integer pageSize=Integer.parseInt(rows);
        pageInfo.setPageSize(pageSize);
        if(page==null||page.equals("")){
            page="1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page)-1)*pageInfo.getPageSize());
        // 获取总记录数
        System.out.println("分组ID："+groupId+"\n"+"名称:"+s_name);
        long total=taskService.createTaskQuery()
                .taskCandidateGroup(groupId)
                .taskNameLike("%"+s_name+"%")
                .count(); // 获取总记录数
        //有想法的话，可以去数据库观察  ACT_RU_TASK 的变化
        List<Task> taskList=taskService.createTaskQuery()
                // 根据用户id查询
                .taskCandidateGroup(groupId)
                // 根据任务名称查询
                .taskNameLike("%"+s_name+"%")
                // 返回带分页的结果集合
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());
        //这里需要使用一个工具类来转换一下主要是转成JSON格式
        List<MyTask> MyTaskList=new ArrayList<MyTask>();
        for(Task t:taskList){
            MyTask myTask=new MyTask();
            myTask.setId(t.getId());
            myTask.setName(t.getName());
            myTask.setCreateTime(t.getCreateTime());
            MyTaskList.add(myTask);
        }
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result=new JSONObject();
        JSONArray jsonArray=JSONArray.fromObject(MyTaskList,jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 重定向审核处理页面
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/redirectPage")
    public String redirdectPage(String taskId , HttpServletResponse response) throws Exception {


        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        String url = taskFormData.getFormKey();
        System.out.println("****************" + url);
        JSONObject result = new JSONObject();
        result.put("url",url);
        ResponseUtil.write(response , result);
        return null;


    }






}
