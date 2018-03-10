package com.css.activiti.service;

import com.css.activiti.mapper.MemberShipMapper;
import com.css.activiti.model.MemberShip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by 46597 on 2018/3/10.
 */
@Service
public class MemberShipService {

    @Autowired
    private MemberShipMapper memberShipMapper;


    public MemberShip userLogin(Map<String, Object> map) {
        return memberShipMapper.userLogin(map);
    }


}
