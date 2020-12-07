package com.digitaldeparturesystem.pojo;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Student implements UserDetails {

  private String stuId;
  private String stuNumber;
  private String stuPwd;
  private String stuName;
  private String stuDept;
  private String stuClass;
  private String stuContact;
  private String stuSex;
  private String stuStatus;
  private String stuInDate;
  private String stuOutDate;
  private String stuAddress;
  private String stuSpecialty;
  private String stuNation;
  private String stuFeature;
  private String stuType;
  private String stuCredit;
  private String stuPhoneNumber;
  private String stuPhoto;

  private List<Role> userRoles;

  public List<Role> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<Role> userRoles) {
    this.userRoles = userRoles;
  }

  public String getStuPhoneNumber() {
    return stuPhoneNumber;
  }

  public void setStuPhoneNumber(String stuPhoneNumber) {
    this.stuPhoneNumber = stuPhoneNumber;
  }

  public String getStuPhoto() {
    return stuPhoto;
  }

  public void setStuPhoto(String stuPhoto) {
    this.stuPhoto = stuPhoto;
  }

  public String getStuId() {
    return stuId;
  }

  public void setStuId(String stuId) {
    this.stuId = stuId;
  }


  public String getStuNumber() {
    return stuNumber;
  }

  public void setStuNumber(String stuNumber) {
    this.stuNumber = stuNumber;
  }


  public String getStuPwd() {
    return stuPwd;
  }

  public void setStuPwd(String stuPwd) {
    this.stuPwd = stuPwd;
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


  public String getStuClass() {
    return stuClass;
  }

  public void setStuClass(String stuClass) {
    this.stuClass = stuClass;
  }


  public String getStuContact() {
    return stuContact;
  }

  public void setStuContact(String stuContact) {
    this.stuContact = stuContact;
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

  public String getStuAddress() {
    return stuAddress;
  }

  public void setStuAddress(String stuAddress) {
    this.stuAddress = stuAddress;
  }


  public String getStuSpecialty() {
    return stuSpecialty;
  }

  public void setStuSpecialty(String stuSpecialty) {
    this.stuSpecialty = stuSpecialty;
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


  public String getStuType() {
    return stuType;
  }

  public void setStuType(String stuType) {
    this.stuType = stuType;
  }


  public String getStuCredit() {
    return stuCredit;
  }

  public void setStuCredit(String stuCredit) {
    this.stuCredit = stuCredit;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    if (userRoles == null){
      return authorities;
    }
    for (Role role : userRoles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.stuPwd;
  }

  @Override
  public String getUsername() {
    return this.stuPwd;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
