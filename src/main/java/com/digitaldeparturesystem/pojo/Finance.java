package com.digitaldeparturesystem.pojo;



public class Finance {


  private String financeID;

  private String stuID;

  private double expense;

  private String financeStatus;

  private double cardBalance;

  private double dormFine;

  private double libFine;


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


  public double getCardBalance() {
    return cardBalance;
  }

  public void setCardBalance(double cardBalance) {
    this.cardBalance = cardBalance;
  }


  public double getDormFine() {
    return dormFine;
  }

  public void setDormFine(double dormFine) {
    this.dormFine = dormFine;
  }


  public double getLibFine() {
    return libFine;
  }

  public void setLibFine(double libFine) {
    this.libFine = libFine;
  }

}
