package com.digitaldeparturesystem.pojo;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Clerk implements UserDetails, Serializable {

  private String clerkID;
  private String clerkName;
  private String clerkPwd;
  private String department;
  private String clerkAccount;
  private String clerkPhoto;
  private String clerkStatus;
  private String clerkEmail;

  private List<Role> userRoles;


  public String getClerkEmail() {
    return clerkEmail;
  }

  public void setClerkEmail(String clerkEmail) {
    this.clerkEmail = clerkEmail;
  }

  public String getClerkStatus() {
    return clerkStatus;
  }

  public void setClerkStatus(String clerkStatus) {
    this.clerkStatus = clerkStatus;
  }

  public String getClerkPhoto() {
    return clerkPhoto;
  }

  public void setClerkPhoto(String clerkPhoto) {
    this.clerkPhoto = clerkPhoto;
  }

  public String getClerkAccount() {
    return clerkAccount;
  }

  public void setClerkAccount(String clerkAccount) {
    this.clerkAccount = clerkAccount;
  }

  public String getClerkID() {
    return clerkID;
  }

  public void setClerkID(String clerkID) {
    this.clerkID = clerkID;
  }

  public String getClerkName() {
    return clerkName;
  }

  public void setClerkName(String clerkName) {
    this.clerkName = clerkName;
  }


  public String getClerkPwd() {
    return clerkPwd;
  }

  public void setClerkPwd(String clerkPwd) {
    this.clerkPwd = clerkPwd;
  }


  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }


  public List<Role> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<Role> userRoles) {
    this.userRoles = userRoles;
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
    return this.getClerkPwd();
  }

  @Override
  public String getUsername() {
    return this.getClerkAccount();
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
