package com.digitaldeparturesystem.pojo;

import java.util.Date;

public class Checkcommit {

  private String checkId;
  private String stuId;
  private String reason;
  private Date commitdate;
  private String type;


  public String getCheckId() {
    return checkId;
  }

  public void setCheckId(String checkId) {
    this.checkId = checkId;
  }


  public String getStuId() {
    return stuId;
  }

  public void setStuId(String stuId) {
    this.stuId = stuId;
  }


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Date getCommitdate() {
    return commitdate;
  }

  public void setCommitdate(Date commitdate) {
    this.commitdate = commitdate;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
