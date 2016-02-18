package com.pxy.studyhelper.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-15
 * Time: 23:07
 * FIXME
 */
public class Topic  extends BmobObject implements Serializable{
    private String  userName;
    private String  content;
    private BmobFile image;
    private Integer love;
    private String  headUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", love=" + love +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
