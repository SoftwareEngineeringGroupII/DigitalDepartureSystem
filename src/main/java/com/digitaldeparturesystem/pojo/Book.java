package com.digitaldeparturesystem.pojo;


public class Book {

  private String stuID;

  private String bookID;

  private String bookNum;

  private String bookName;

  private String returnStatus;

  private double price;

  private double pay;


  public String getStuID() {
    return stuID;
  }

  public void setStuID(String stuID) {
    this.stuID = stuID;
  }


  public String getBookID() {
    return bookID;
  }

  public void setBookID(String bookID) {
    this.bookID = bookID;
  }


  public String getBookNum() {
    return bookNum;
  }

  public void setBookNum(String bookNum) {
    this.bookNum = bookNum;
  }


  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }


  public String getReturnStatus() {
    return returnStatus;
  }

  public void setReturnStatus(String returnStatus) {
    this.returnStatus = returnStatus;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public double getPay() {
    return pay;
  }

  public void setPay(double pay) {
    this.pay = pay;
  }

}
