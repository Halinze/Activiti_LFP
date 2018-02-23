package com.css.activiti.service;

import com.css.activiti.mapper.GroupMapper;
import com.css.activiti.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 46597 on 2018/2/23.
 */
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    //todo 真的是蠢啊 我的天 好郁闷
    public List<Group> fingGroup(){
        return groupMapper.findGroup();
    }





}
