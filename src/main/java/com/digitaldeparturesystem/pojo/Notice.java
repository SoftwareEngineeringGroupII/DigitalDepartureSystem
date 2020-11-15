package com.digitaldeparturesystem.pojo;


import java.util.Date;

public class Notice {

  private String noticeId;
  private String content;
  private String title;
  private String publisherId;
  private Date publishTime;
  private String checkStatus;
  private String remark;
  private String noticeType;
  private String isTop;


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

}
