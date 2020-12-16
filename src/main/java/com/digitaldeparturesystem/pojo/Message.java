package com.digitaldeparturesystem.pojo;


import java.util.Date;

public class Message {


  private String messageID;
  private String sendID;
  private String title;
  private String content;
  private Date messagedate;
  private String receiveID;
  private String msgStatus;


  public String getMessageID() {
    return messageID;
  }

  public void setMessageID(String messageID) {
    this.messageID = messageID;
  }


  public String getSendID() {
    return sendID;
  }

  public void setSendID(String sendID) {
    this.sendID = sendID;
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


  public String getReceiveID() {
    return receiveID;
  }

  public void setReceiveID(String receiveID) {
    this.receiveID = receiveID;
  }


  public String getMsgStatus() {
    return msgStatus;
  }

  public void setMsgStatus(String msgStatus) {
    this.msgStatus = msgStatus;
  }

}
