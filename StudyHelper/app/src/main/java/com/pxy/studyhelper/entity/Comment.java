package com.pxy.studyhelper.entity;

import cn.bmob.v3.BmobObject;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-15
 * Time: 23:11
 * 用户评论表
 * FIXME
 * BmobObject类本身包含objectId、createdAt、updatedAt、ACL四个默认的属性，
 * objectId是数据的唯一标示，相当于数据库中表的主键，createdAt是数据的创建时间，
 * updatedAt是数据的最后修改时间，ACL是数据的操作权限。
 */
public class Comment  extends BmobObject {
    private String  topicId;
    private String  content;
    private String  userName;
    private String  headUrl;
    private Integer  love=0;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = new Integer(love);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "topicId='" + topicId + '\'' +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
