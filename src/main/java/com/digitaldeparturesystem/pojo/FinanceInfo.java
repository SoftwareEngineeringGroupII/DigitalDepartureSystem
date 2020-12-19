package com.digitaldeparturesystem.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;

public class FinanceInfo {

    @Excel(name = "学号",width = 20)
    private String stuNumber;//学号

    @Excel(name = "姓名")
    private String stuName;//姓名

    @Excel(name = "学院",width = 20)
    private String stuDept; //学院

    @Excel(name = "学生类型",width = 15)
    private String stuType; //学生类型

    @Excel(name = "一卡通余额",width = 15)
    private double cardBalance;

    @Excel(name = "后勤处缴费",width = 15)
    private double dormFine;

    @Excel(name = "图书馆缴费",width = 15)
    private double libFine;

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

    @Excel(name = "余额")
    private long expense; //余额

   // @ExcelIgnore
    @Excel(name = "退款状态")
    private String financeStatus;  //退款状态


    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
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
