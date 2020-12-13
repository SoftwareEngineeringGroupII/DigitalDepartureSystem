package com.digitaldeparturesystem.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;

import java.io.Serializable;


public class StuBasicInfo implements Serializable {

    @Excel(name = "学号",width = 20)
    private String stuNumber;//学号

    @Excel(name = "姓名")
    private String stuName; //姓名

    @Excel(name = "民族")
    private String stuNation;//民族

    @Excel(name = "政治面貌",width = 15)
    private String stuFeature;//政治面貌

    @Excel(name = "性别")
    private String stuSex; //性别

    @Excel(name = "学生类型")
    private String stuType;//学生类型

    @Excel(name = "学院",width = 20)
    private String stuDept; //学院

    @Excel(name = "专业",width = 15)
    private String stuSpecialty;//学生专业

    @Excel(name = "班级")
    private String stuClass; //班级

    @Excel(name = "已获得学分")
    private String stuCredit;//已获得学分

    @Excel(name = "入学时间",width = 15)
    private String stuInDate;//学生入学时间

    @Excel(name = "离校时间",width = 15)
    private String stuOutDate;//学生离校时间

    @Excel(name = "离校状态")
    private String stuStatus;//学生离校状态

    @Excel(name = "住址",width = 25)
    private String stuAddress;//学生地址

    @Excel(name = "联系方式",width = 20)
    private String stuPhoneNumber;//电话联系方式

    @Excel(name = "邮箱",width = 20)
    private String stuContact;//邮箱

    @ExcelIgnore
    @Excel(name = "头像")
    private String photo;//头像

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

    public String getStuNation() {
        return stuNation;
    }

    public void setStuNation(String stuNation) {
        this.stuNation = stuNation;
    }

    public String getStuFeature() {
        return stuFeature;
    }

    public void setStuFeature(String stuFeature) {
        this.stuFeature = stuFeature;
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }

    public String getStuType() {
        return stuType;
    }

    public void setStuType(String stuType) {
        this.stuType = stuType;
    }

    public String getStuDept() {
        return stuDept;
    }

    public void setStuDept(String stuDept) {
        this.stuDept = stuDept;
    }

    public String getStuSpecialty() {
        return stuSpecialty;
    }

    public void setStuSpecialty(String stuSpecialty) {
        this.stuSpecialty = stuSpecialty;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuCredit() {
        return stuCredit;
    }

    public void setStuCredit(String stuCredit) {
        this.stuCredit = stuCredit;
    }

    public String getStuInDate() {
        return stuInDate;
    }

    public void setStuInDate(String stuInDate) {
        this.stuInDate = stuInDate;
    }

    public String getStuOutDate() {
        return stuOutDate;
    }

    public void setStuOutDate(String stuOutDate) {
        this.stuOutDate = stuOutDate;
    }

    public String getStuStatus() {
        return stuStatus;
    }

    public void setStuStatus(String stuStatus) {
        this.stuStatus = stuStatus;
    }

    public String getStuAddress() {
        return stuAddress;
    }

    public void setStuAddress(String stuAddress) {
        this.stuAddress = stuAddress;
    }

    public String getStuPhoneNumber() {
        return stuPhoneNumber;
    }

    public void setStuPhoneNumber(String stuPhoneNumber) {
        this.stuPhoneNumber = stuPhoneNumber;
    }

    public String getStuContact() {
        return stuContact;
    }

    public void setStuContact(String stuContact) {
        this.stuContact = stuContact;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
