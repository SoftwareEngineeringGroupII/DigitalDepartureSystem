package com.digitaldeparturesystem.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DormInfo {
    @Excel(name = "学号",width = 20)
    private String stuNumber;

    @Excel(name = "姓名")
    private String stuName;

    @Excel(name = "学院",width = 20)
    private String stuDept;

    @Excel(name = "类型")
    private String stuType;

    @Excel(name = "寝室名",width = 20)
    private String dormName;

    @Excel(name = "寝室号")
    private String dormNumber;

    @Excel(name = "是否退寝")
    private String isLeave;

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

    public String getDormName() {
        return dormName;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public String getDormNumber() {
        return dormNumber;
    }

    public void setDormNumber(String dormNumber) {
        this.dormNumber = dormNumber;
    }

    public String getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }
}
