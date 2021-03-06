package com.liujun.demo.gradle.mult.module;

public class DataInfo {

  private int id;

  private String name;

  private String value;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DataInfo{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", value='").append(value).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
