package com.pxy.studyhelper.entity;

import cn.bmob.v3.BmobUser;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-01-25
 * Time: 23:19
 * BmobUser除了从BmobObject继承的属性外，还有几个特定的属性：
 username: 用户的用户名（必需）。
 password: 用户的密码（必需）。
 email: 用户的电子邮件地址（可选）。
 emailVerified:邮箱认证状态（可选）。
 mobilePhoneNumber：手机号码（可选）。
 mobilePhoneNumberVerified：手机号码的认证状态（可选）
 * FIXME
 */
public class User  extends BmobUser {

 private Boolean sex;
 private String nickName;
 private Integer level;
 private Integer score;

 public Boolean getSex() {
  return sex;
 }
 public void setSex(Boolean sex) {
  this.sex = sex;
 }

 public String getNickName() {
  return nickName;
 }

 public void setNickName(String nickName) {
  this.nickName = nickName;
 }

 public Integer getLevel() {
  return level;
 }

 public void setLevel(Integer level) {
  this.level = level;
 }

 public Integer getScore() {
  return score;
 }

 public void setScore(Integer score) {
  this.score = score;
 }

 @Override
 public String toString() {
  return "User{" +
          "sex=" + sex +
          ", nickName='" + nickName + '\'' +
          ", level=" + level +
          ", score=" + score +
          '}';
 }


}
