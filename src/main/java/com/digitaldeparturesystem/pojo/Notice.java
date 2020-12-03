package com.digitaldeparturesystem.pojo;


import java.util.Date;

public class Notice {

  private String noticeId; //公告ID
  private String content; //公告内容
  private String title; //公告标题
  private String publisherId; //发布者ID
  private Date publishTime; //发布时间
  private String checkStatus; //公告审核状态
  private String remark; //备注
  private String noticeType; //公告类型
  private String isTop; //公告是否置顶
  private String path;//图片路径


  public String getNoticeId() {
    return noticeId;
  }

  public void setNoticeId(String noticeId) {
    this.noticeId = noticeId;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(String publisherId) {
    this.publisherId = publisherId;
  }


  public Date getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Date publishTime) {
    this.publishTime = publishTime;
  }

  public String getCheckStatus() {
    return checkStatus;
  }

  public void setCheckStatus(String checkStatus) {
    this.checkStatus = checkStatus;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public String getNoticeType() {
    return noticeType;
  }

  public void setNoticeType(String noticeType) {
    this.noticeType = noticeType;
  }


  public String getIsTop() {
    return isTop;
  }

  public void setIsTop(String isTop) {
    this.isTop = isTop;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
