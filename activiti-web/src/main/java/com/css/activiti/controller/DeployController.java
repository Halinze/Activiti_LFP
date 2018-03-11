package com.css.activiti.controller;

import com.css.activiti.model.PageInfo;
import com.css.activiti.util.DateJsonValueProcessor;
import com.css.activiti.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 怎么有种到哪都可以共用的感觉
 * Created by 46597 on 2018/3/10.
 */
@Controller
@RequestMapping("deploy")
public class DeployController {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 分页查询流程
     *
     * @param rows
     * @param page
     * @param s_name
     * @param response
     * @return
     */
    @RequestMapping("/deployPage")
    public String deployPage(String rows, String page, String s_name, HttpServletResponse response) throws Exception {


        if (s_name == null) {
            s_name = "";
        }
        //这个人的代码写的有些啰嗦
        PageInfo pageInfo = new PageInfo();
        Integer sizePage = Integer.parseInt(rows);
        pageInfo.setPageSize(sizePage);

        String pageIndex = page;

        if (pageIndex == null || pageIndex == "") {
            pageIndex = "1";
        }

        pageInfo.setPageIndex((Integer.parseInt(pageIndex) - 1) * sizePage);
        //取得总数量
        long deployCount = repositoryService.createDeploymentQuery().deploymentNameLike("%" + s_name + "%").count();

        List<Deployment> deployList = repositoryService.createDeploymentQuery()
                .orderByDeploymenTime().desc()
                .deploymentNameLike("%" + s_name + "%")
                .listPage(pageInfo.getPageIndex(), pageInfo.getPageSize());

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"resources"});
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));

        JSONObject result = new JSONObject();
        JSONArray jsonArray = null  ;
        if(deployList != null) {
            jsonArray = JSONArray.fromObject(deployList, jsonConfig);
        }
        result.put("rows", jsonArray);
        result.put("total", deployCount);
        ResponseUtil.write(response, result);
        return null;


    }


    /**
     * 添上传流程部署zip文件
     *
     * @param response
     * @param deployFile
     * @return
     * @throws Exception
     */
    @RequestMapping("/addDeploy")
    public String addDeploy(HttpServletResponse response, MultipartFile deployFile) throws Exception {
        repositoryService.createDeployment()//创建部署
                .name(deployFile.getOriginalFilename())//需要部署流程的名称
                .addZipInputStream(new ZipInputStream(deployFile.getInputStream()))
                .deploy();//开始部署
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 批量删除流程
     *
     * @param response
     * @param ids
     * @return
     */
    @RequestMapping("/delDeploy")
    public String delDeploy(HttpServletResponse response, String ids) throws Exception {

        String[] idsStr = ids.split(",");
        for (String str : idsStr) {
            repositoryService.deleteDeployment(str, true);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }


}
