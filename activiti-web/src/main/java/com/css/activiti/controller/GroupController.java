package com.css.activiti.controller;

import com.css.activiti.model.Group;
import com.css.activiti.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 46597 on 2018/2/23.
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;


    /**
     * 页面ajax请求查询分组信息
     * 入参
     * 出参：id name
     *
     * 为什么那个人写的那么复杂？？
     *
     */
    @RequestMapping("/findGroup")
    @ResponseBody
     public List<Group> findGroup(){
        return groupService.fingGroup();
     }






}
