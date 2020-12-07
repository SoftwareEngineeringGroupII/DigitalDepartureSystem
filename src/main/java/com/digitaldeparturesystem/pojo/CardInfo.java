package com.digitaldeparturesystem.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CardInfo  {
    @Excel(name = "学号" ,width = 20)
    private String stuNumber;

    @Excel(name = "学院",width = 20)
    private String stuDept;

    @Excel(name = "学生类型")
    private String stuType;

    @Excel(name = "一卡通余额")
    private String cardBalance;

    @Excel(name = "一卡通状态")
    private String cardStatus;

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuDept() {
        return stuDept;
    }

    public void setStuDept(String stuDept) {
        this.stuDept = stuDept;
    }

    public String getStuType() {
        return stuType;
    }

    public void setStuType(String stuType) {
        this.stuType = stuType;
    }

    public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}
