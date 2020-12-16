package com.digitaldeparturesystem.pojo;


import java.io.Serializable;

public class Process implements Serializable {

  private String processID;

  private String stuID;

  private String libStatus;

  private String cardStatus;

  private String eduStatus;

  private String financeStatus;

  private String dormStatus;


  public String getProcessID() {
    return processID;
  }

  public void setProcessID(String processID) {
    this.processID = processID;
  }


  public String getStuID() {
    return stuID;
  }

  public void setStuID(String stuID) {
    this.stuID = stuID;
  }


  public String getLibStatus() {
    return libStatus;
  }

  public void setLibStatus(String libStatus) {
    this.libStatus = libStatus;
  }


  public String getCardStatus() {
    return cardStatus;
  }

  public void setCardStatus(String cardStatus) {
    this.cardStatus = cardStatus;
  }


  public String getEduStatus() {
    return eduStatus;
  }

  public void setEduStatus(String eduStatus) {
    this.eduStatus = eduStatus;
  }


  public String getFinanceStatus() {
    return financeStatus;
  }

  public void setFinanceStatus(String financeStatus) {
    this.financeStatus = financeStatus;
  }


  public String getDormStatus() {
    return dormStatus;
  }

  public void setDormStatus(String dormStatus) {
    this.dormStatus = dormStatus;
  }

}
