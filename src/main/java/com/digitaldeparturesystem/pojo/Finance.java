package com.digitaldeparturesystem.pojo;


public class Finance {

  private String financeID;

  private String stuID;

  private double expense;

  private String financeStatus;

  private double dorm;

  private double card;

  private double lib;


  public String getFinanceID() {
    return financeID;
  }

  public void setFinanceID(String financeID) {
    this.financeID = financeID;
  }


  public String getStuID() {
    return stuID;
  }

  public void setStuID(String stuID) {
    this.stuID = stuID;
  }


  public double getExpense() {
    return expense;
  }

  public void setExpense(double expense) {
    this.expense = expense;
  }


  public String getFinanceStatus() {
    return financeStatus;
  }

  public void setFinanceStatus(String financeStatus) {
    this.financeStatus = financeStatus;
  }


  public double getDorm() {
    return dorm;
  }

  public void setDorm(double dorm) {
    this.dorm = dorm;
  }


  public double getCard() {
    return card;
  }

  public void setCard(double card) {
    this.card = card;
  }


  public double getLib() {
    return lib;
  }

  public void setLib(double lib) {
    this.lib = lib;
  }

}
