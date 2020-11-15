package com.digitaldeparturesystem.pojo;


import java.util.Date;

public class Form {

  private String formId;
  private String stuId;
  private String processId;
  private Date outDate;


  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }


  public String getStuId() {
    return stuId;
  }

  public void setStuId(String stuId) {
    this.stuId = stuId;
  }


  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }


  public Date getOutDate() {
    return outDate;
  }

  public void setOutDate(Date outDate) {
    this.outDate = outDate;
  }
}
