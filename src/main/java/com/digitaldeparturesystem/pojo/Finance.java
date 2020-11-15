package com.digitaldeparturesystem.pojo;


public class Finance {

  private String financeId;
  private String stuId;
  private long expense;
  private String financeStatus;


  public String getFinanceId() {
    return financeId;
  }

  public void setFinanceId(String financeId) {
    this.financeId = financeId;
  }


  public String getStuId() {
    return stuId;
  }

  public void setStuId(String stuId) {
    this.stuId = stuId;
  }


  public long getExpense() {
    return expense;
  }

  public void setExpense(long expense) {
    this.expense = expense;
  }


  public String getFinanceStatus() {
    return financeStatus;
  }

  public void setFinanceStatus(String financeStatus) {
    this.financeStatus = financeStatus;
  }

}
