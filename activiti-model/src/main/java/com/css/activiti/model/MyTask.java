package com.css.activiti.model;

import java.util.Date;

/**
 * Created by 46597 on 2018/2/24.
 *
 * 这个实体类的数据对应 的是哪张表？
 */
public class MyTask {

    private String id ;
    private String name ;
    private Date createTime;
    private Date endTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
