package com.digitaldeparturesystem.pojo;


//import cn.afterturn.easypoi.excel.annotation.Excel;

public class Finance {

 // @Excel(name = "财务主键")
  private String financeId;

  //@Excel(name = "学生ID")
  private String stuId;

  //@Excel(name = "应缴费")
  private long expense;

  //@Excel(name = "缴费情况")
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
