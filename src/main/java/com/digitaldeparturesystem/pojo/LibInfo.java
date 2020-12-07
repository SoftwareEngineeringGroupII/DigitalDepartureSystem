package com.digitaldeparturesystem.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class LibInfo {

    @Excel(name = "学号")
    private String stuNumber;

    @Excel(name = "姓名")
    private String stuName;

    @Excel(name = "学院")
    private String  stuDept;

    @Excel(name = "类型")
    private String stuType;

    @Excel(name = "未归还书数")
    private String AllBook;

    @Excel(name = "通过状态")
    private String libStatus;


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

    public String getAllBook() {
        return AllBook;
    }

    public void setAllBook(String allBook) {
        AllBook = allBook;
    }

    public String getLibStatus() {
        return libStatus;
    }

    public void setLibStatus(String libStatus) {
        this.libStatus = libStatus;
    }





}
