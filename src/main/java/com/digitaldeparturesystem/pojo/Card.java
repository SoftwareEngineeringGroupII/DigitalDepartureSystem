package com.digitaldeparturesystem.pojo;


public class Card {

  private String cardId;
  private String stuId;
  private long cardBalance;
  private String cardStatus;


  public String getCardId() {
    return cardId;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }


  public String getStuId() {
    return stuId;
  }

  public void setStuId(String stuId) {
    this.stuId = stuId;
  }


  public long getCardBalance() {
    return cardBalance;
  }

  public void setCardBalance(long cardBalance) {
    this.cardBalance = cardBalance;
  }


  public String getCardStatus() {
    return cardStatus;
  }

  public void setCardStatus(String cardStatus) {
    this.cardStatus = cardStatus;
  }

}
