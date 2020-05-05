package com.liujun.microservice.demo.hystrix.domain;

import lombok.Data;
import lombok.ToString;

/**
 * 跑马大赛信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class RaceCourse {

  private String id;

  private String name;

  public RaceCourse(String id, String name) {
    super();
    this.id = id;
    this.name = name;
  }
}
