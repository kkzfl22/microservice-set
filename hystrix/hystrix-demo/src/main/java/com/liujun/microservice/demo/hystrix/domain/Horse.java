package com.liujun.microservice.demo.hystrix.domain;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 马的信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class Horse {

  private String id;

  private String name;

  public Horse(String id, String name) {
    super();
    this.id = id;
    this.name = name;
  }
}
