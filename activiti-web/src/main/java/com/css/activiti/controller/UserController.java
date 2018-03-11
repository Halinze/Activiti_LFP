package com.css.activiti.controller;

import com.css.activiti.model.MemberShip;
import com.css.activiti.service.MemberShipService;
import com.css.activiti.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by 46597 on 2018/2/23.
 */
@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private MemberShipService memberShipService;

    //页面登录功能  username password 和分组

    //入参：userName  password groupId 这里得返回json

    @RequestMapping("/userLogin")
    public String userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", request.getParameter("userName"));
        map.put("password", request.getParameter("password"));
        map.put("groupId", request.getParameter("groupId"));

        MemberShip memberShip = memberShipService.userLogin(map);
        System.out.println(memberShip.getGroup().getId() + " "+ memberShip.getUser().getId());

        JSONObject result = new JSONObject();

        if (memberShip == null) {
            result.put("success", false);
            result.put("errorInfo", "用户名或密码错误");
        } else {
            result.put("success", true);
            request.getSession().setAttribute("currentMemberShip", memberShip);
        }

        ResponseUtil.write(response, result);

        return null;


    }


}
