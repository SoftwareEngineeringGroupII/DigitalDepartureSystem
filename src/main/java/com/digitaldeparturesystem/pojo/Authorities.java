package com.digitaldeparturesystem.pojo;


import java.util.ArrayList;
import java.util.List;

public class Authorities {

  private String id;
  private String name;
  private String parentId;
  private long index;
  private long available;
  private String resourceType;
  private String url;

  private List<Authorities> children=new ArrayList<>();

  public List<Authorities> getChildren() {
    return children;
  }

  public void setChildren(List<Authorities> children) {
    this.children = children;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  public long getIndex() {
    return index;
  }

  public void setIndex(long index) {
    this.index = index;
  }


  public long getAvailable() {
    return available;
  }

  public void setAvailable(long available) {
    this.available = available;
  }


  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
