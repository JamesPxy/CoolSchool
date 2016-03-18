package com.pxy.studyhelper.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by:Pxy
 * Date: 2016-03-17
 * Time: 14:50
 * 用户反馈实体类
 */
public class Feedback extends BmobObject{

    private String  userId;
    private String  content;
    private String  contact;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
