package com.css.activiti.model;

/**
 * Created by 46597 on 2018/2/24.
 *
 * 用户，角色的中间表 ， 多对多的关系
 */
public class MemberShip {

    private User user ;
    private Group group ;
    private String UserId ;
    private String groupId ;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
