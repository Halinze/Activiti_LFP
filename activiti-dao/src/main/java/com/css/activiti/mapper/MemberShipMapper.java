package com.css.activiti.mapper;

import com.css.activiti.model.MemberShip;

import java.util.Map;

/**
 * Created by 46597 on 2018/3/9.
 */
public interface MemberShipMapper {
    /**
     * 用户登入的方法
     * @param map
     * @return
     */
    public MemberShip userLogin(Map<String,Object> map);


    public int deleteAllGroupsByUserId(String userId);


    public int addMemberShip(MemberShip memberShip);



}
