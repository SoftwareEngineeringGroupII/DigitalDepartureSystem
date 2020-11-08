package com.digitaldeparturesystem.pojo;

import java.util.Date;

/**
 * 实体类
 */
public class Student {

	private String id;
	private String stuNumber;
	private String stuName;
	private String stuPwd;
	private String stuDept;
	private String stuClass;
	private String stuPhoneNumber;
	private String stuPicture;
	private String stuSex;
	private String stuStatus;
	private Date stuInDate;
	private Date stuOutDate;
	private String stuAddress;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


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


	public String getStuPwd() {
		return stuPwd;
	}

	public void setStuPwd(String stuPwd) {
		this.stuPwd = stuPwd;
	}


	public String getStuDept() {
		return stuDept;
	}

	public void setStuDept(String stuDept) {
		this.stuDept = stuDept;
	}


	public String getStuClass() {
		return stuClass;
	}

	public void setStuClass(String stuClass) {
		this.stuClass = stuClass;
	}


	public String getStuPhoneNumber() {
		return stuPhoneNumber;
	}

	public void setStuPhoneNumber(String stuPhoneNumber) {
		this.stuPhoneNumber = stuPhoneNumber;
	}


	public String getStuPicture() {
		return stuPicture;
	}

	public void setStuPicture(String stuPicture) {
		this.stuPicture = stuPicture;
	}


	public String getStuSex() {
		return stuSex;
	}

	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}


	public String getStuStatus() {
		return stuStatus;
	}

	public void setStuStatus(String stuStatus) {
		this.stuStatus = stuStatus;
	}


    public Date getStuInDate() {
        return stuInDate;
    }

    public void setStuInDate(Date stuInDate) {
        this.stuInDate = stuInDate;
    }

    public Date getStuOutDate() {
        return stuOutDate;
    }

    public void setStuOutDate(Date stuOutDate) {
        this.stuOutDate = stuOutDate;
    }

	public String getStuAddress() {
		return stuAddress;
	}

	public void setStuAddress(String stuAddress) {
		this.stuAddress = stuAddress;
	}


}
