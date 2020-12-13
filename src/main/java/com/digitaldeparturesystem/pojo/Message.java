package com.digitaldeparturesystem.pojo;


import java.util.Date;

public class Message {

  private String messageId;
  private String sendId;
  private String title;
  private String content;
  private Date messagedate;
  private String receiveId;
  private String msgStatus;

  public String getMsgStatus() {
    return msgStatus;
  }

  public void setMsgStatus(String msgStatus) {
    this.msgStatus = msgStatus;
  }

  public String getReceiveId() {
    return receiveId;
  }

  public void setReceiveId(String receiveId) {
    this.receiveId = receiveId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }


  public String getSendId() {
    return sendId;
  }

  public void setSendId(String sendId) {
    this.sendId = sendId;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public Date getMessagedate() {
    return messagedate;
  }

  public void setMessagedate(Date messagedate) {
    this.messagedate = messagedate;
  }
}
