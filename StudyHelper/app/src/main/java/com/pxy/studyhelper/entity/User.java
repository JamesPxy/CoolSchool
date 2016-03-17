package com.pxy.studyhelper.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

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
public class User  extends BmobUser implements Serializable{

 private static final long serialVersionUID = 1L;


 /**
  * //性别-true-男
  */
 private Boolean sex;
 private String nickName;
 private Integer level=0;
 private Integer score;
 private String  headUrl;
 private String  school;
 private String  sign;




 private BmobRelation contacts;
 private String installId;
 private String deviceType;
 private BmobRelation blacklist;
 /**
  * //显示数据拼音的首字母
  */
 private String sortLetters;

 /**
  * 地理坐标
  */
 private BmobGeoPoint location;//

// private Integer hight;

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

 public String getHeadUrl() {
  return headUrl;
 }

 public void setHeadUrl(String headUrl) {
  this.headUrl = headUrl;
 }

 public String getSchool() {
  return school;
 }

 public void setSchool(String school) {
  this.school = school;
 }

 public String getSign() {
  return sign;
 }

 public void setSign(String sign) {
  this.sign = sign;
 }

 public String getSortLetters() {
  return sortLetters;
 }

 public void setSortLetters(String sortLetters) {
  this.sortLetters = sortLetters;
 }

 public BmobGeoPoint getLocation() {
  return location;
 }

 public void setLocation(BmobGeoPoint location) {
  this.location = location;
 }


 public BmobRelation getContacts() {
  return contacts;
 }

 public void setContacts(BmobRelation contacts) {
  this.contacts = contacts;
 }

 public String getInstallId() {
  return installId;
 }

 public void setInstallId(String installId) {
  this.installId = installId;
 }

 public String getDeviceType() {
  return deviceType;
 }

 public void setDeviceType(String deviceType) {
  this.deviceType = deviceType;
 }

 public BmobRelation getBlacklist() {
  return blacklist;
 }

 public void setBlacklist(BmobRelation blacklist) {
  this.blacklist = blacklist;
 }

 @Override
 public String toString() {
  return "User{" +
          "sex=" + sex +
          ", nickName='" + nickName + '\'' +
          ", level=" + level +
          ", score=" + score +
          ", headUrl='" + headUrl + '\'' +
          '}';
 }
}
