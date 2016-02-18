package com.pxy.studyhelper.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

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
    private String  userId;
    private String  content;
    private BmobDate  createdAt;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

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

    @Override
    public String getCreatedAt() {
        return createdAt.getDate();
    }

    public void setCreatedAt(BmobDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "topicId='" + topicId + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt.getDate() +
                '}';
    }
}
