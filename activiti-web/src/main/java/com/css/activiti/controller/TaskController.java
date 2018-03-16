package com.css.activiti.controller;

import com.css.activiti.model.*;
import com.css.activiti.service.LeaveService;
import com.css.activiti.util.DateJsonValueProcessor;
import com.css.activiti.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    @Autowired
    private HistoryService historyService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private RepositoryService repositoryService;


    /**
     * 代办流程分页查询
     *
     * @param response
     * @param page
     * @param rows
     * @param s_name
     * @param groupId
     * @return
     */
    @RequestMapping("/taskPage")
    public String taskPage(HttpServletResponse response, String page, String rows, String s_name, String groupId) throws Exception {

        System.out.println("==========================================");


        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        Integer pageSize = Integer.parseInt(rows);
        pageInfo.setPageSize(pageSize);
        if (page == null || page.equals("")) {
            page = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page) - 1) * pageInfo.getPageSize());
        // 获取总记录数
        System.out.println("分组ID：" + groupId + "\n" + "名称:" + s_name);
        long total = taskService.createTaskQuery()
                .taskCandidateGroup(groupId)
                .taskNameLike("%" + s_name + "%")
                .count(); // 获取总记录数
        //有想法的话，可以去数据库观察  ACT_RU_TASK 的变化
        List<Task> taskList = taskService.createTaskQuery()
                // 根据用户id查询
                .taskCandidateGroup(groupId)
                // 根据任务名称查询
                .taskNameLike("%" + s_name + "%")
                // 返回带分页的结果集合
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());
        //这里需要使用一个工具类来转换一下主要是转成JSON格式
        List<MyTask> MyTaskList = new ArrayList<MyTask>();
        for (Task t : taskList) {
            MyTask myTask = new MyTask();
            myTask.setId(t.getId());
            myTask.setName(t.getName());
            myTask.setCreateTime(t.getCreateTime());
            MyTaskList.add(myTask);
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(MyTaskList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 重定向审核处理页面
     *
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/redirectPage")
    public String redirdectPage(String taskId, HttpServletResponse response) throws Exception {


        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        String url = taskFormData.getFormKey();
        System.out.println("****************" + url);
        JSONObject result = new JSONObject();
        result.put("url", url);
        ResponseUtil.write(response, result);
        return null;


    }

    /**
     * 查询历史批注
     *
     * @param response
     * @param taskId
     * @return
     */
    @RequestMapping("/listHistoryComment")
    public String listHistoryComment(HttpServletResponse response, String taskId) throws Exception {


        if (taskId == null) {
            taskId = "";
        }

        HistoricTaskInstance his = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));

        JSONObject result = new JSONObject();
        List<Comment> commentList = null;

        if (his != null) {
            commentList = taskService.getProcessInstanceComments(his.getProcessInstanceId());
            //集合元素反转
            Collections.reverse(commentList);
        }

        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);

        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 审核的请求 重点
     *
     * @param taskId
     * @param leaveDays
     * @param comment
     * @param state
     * @param response
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/audit_bz")
    public String audit_bz(String taskId, Integer leaveDays, String comment, Integer state, HttpServletResponse response, HttpSession session) throws Exception {

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        HashMap<String, Object> variables = new HashMap<>();

        //取得角色用户登入的session对象
        MemberShip currentMemberShip = (MemberShip) session.getAttribute("currentMemberShip");

        User currentUser = currentMemberShip.getUser();

        Group currentGroup = currentMemberShip.getGroup();

        if (currentGroup.getName().equals("总裁") || currentGroup.getName().equals("副总裁")) {
            if (state == 1) {
                //获取leave 的另一种写法？？
                String leaveId = (String) taskService.getVariable(taskId, "leaveId");
                Leave leave = leaveService.findById(leaveId);
                leave.setState("审核通过");
                //更新审核信息
                leaveService.updateLeave(leave);
                variables.put("msg", "通过");
            } else {
                String leaveId = (String) taskService.getVariable(taskId, "leaveId");

                Leave leave = leaveService.findById(leaveId);
                leave.setState("审核未通过");
                variables.put("msg", "未通过");

            }


        }

        if (state == 1) {
            variables.put("msg", "通过");

        } else {
            //soga 原来是这么玩的
            String leaveId = (String) taskService.getVariable(taskId, "leaveId");
            Leave leave = leaveService.findById(leaveId);
            leave.setState("审核未通过");
            //更新审核信息
            leaveService.updateLeave(leave);
            //更新审核信息
            variables.put("msg", "未通过");
        }

        //todo  这边有个拼写错误啊啊！
        //设置流程变量  看发布的流程图 的确就是dasy!
        variables.put("dasy", leaveDays);
        //获取流程实例id
        String processInstanceId = task.getProcessInstanceId();
        //设置用户id
        Authentication.setAuthenticatedUserId(currentUser.getFirstName() + currentUser.getLastName() + "[" + currentGroup.getName() + "]");
        //添加批注信息
        taskService.addComment(taskId, processInstanceId, comment);

        taskService.complete(taskId, variables);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;

    }

    /**
     * 查询历史批注
     *
     * @param response
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listHistoryCommentWithProcessInstanceId")
    public String listHistoryCommentWithProcessinstanceId(HttpServletResponse response, String processInstanceId) throws Exception {


        if (processInstanceId == null) {
            return null;
        }

        List<Comment> commentList = taskService.getProcessInstanceComments(processInstanceId);

        Collections.reverse(commentList);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));


        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;

    }


    /**
     * 查询流程正常走完的历史流程表： act_hi_actinst
     *
     * @param response
     * @param rows
     * @param s_name
     * @param groupId
     * @return
     */
    @RequestMapping("finishedList")
    public String finishedList(HttpServletResponse response, String rows, String s_name, String groupId, String page) throws Exception {

        //第一次进入后台的是 s_name 必定==null
        if (s_name == null) {
            s_name = "";
        }
        PageInfo pageInfo = new PageInfo();
        Integer pageSize = Integer.parseInt(rows);
        pageInfo.setPageSize(pageSize);
        if (page == null || page.equals("")) {
            page = "1";
        }
        pageInfo.setPageIndex((Integer.parseInt(page) - 1) * pageSize);
        List<HistoricTaskInstance> histList = historyService.createHistoricTaskInstanceQuery()
                .taskNameLike("%" + s_name + "%")
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());

        HistoricTaskInstanceQuery hisCount = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateGroup(groupId)
                .taskNameLike("%" + s_name + "%");


        ArrayList<MyTask> taskList = new ArrayList<>();

        for (HistoricTaskInstance hti : histList) {
            MyTask myTask = new MyTask();
            myTask.setId(hti.getId());
            myTask.setName(hti.getName());
            myTask.setCreateTime(hti.getCreateTime());
            myTask.setEndTime(hti.getEndTime());
            taskList.add(myTask);
        }


        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(taskList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", hisCount);
        ResponseUtil.write(response, result);


        return null;
    }


    /**
     * 根据任务id查询流程实例的具体实例过程
     *
     * @param taskId
     * @param respose
     * @return
     */
    @RequestMapping("/listAction")
    public String listAction(String taskId, HttpServletResponse respose) throws Exception {

        HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        //获取流程实例id
        String processInstanceId = hti.getProcessInstanceId();

        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));

        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(haiList, jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(respose, result);
        return null;
    }

    /**
     * 查询当前流程
     * @param response
     * @param taskId
     * @return
     */
    @RequestMapping("/showCurrentView")
    public String showCurrentView(HttpServletResponse response , String taskId){


        //视图
        ModelAndView mav = new ModelAndView();

        Task task = taskService.createTaskQuery() //创建任务查询
                .taskId(taskId)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();



        return null;


    }





}
