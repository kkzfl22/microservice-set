package com.liujun.microservice.hystrix.demo.server.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * student info
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
@Builder
public class Student {

  private Integer studentId;

  private String name;

  private String address;
}
