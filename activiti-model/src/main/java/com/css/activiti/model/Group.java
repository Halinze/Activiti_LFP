package com.css.activiti.model;

import java.io.Serializable;

/**
 * Created by 46597 on 2018/2/23
 *
 * 很多东西要从扩展性来思考
 */
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id ;

    private String name ;

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

}
