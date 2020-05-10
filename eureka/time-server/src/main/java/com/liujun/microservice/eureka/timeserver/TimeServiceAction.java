package com.liujun.microservice.eureka.timeserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 对外提供服务的API
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
public class TimeServiceAction {

  @Value("${server.port}")
  private int port;

  @GetMapping
  public String getTime() {
    StringBuilder outData = new StringBuilder();

    outData.append("this current time is :");
    outData.append(new Date().toString());
    outData.append("(");
    outData.append("answered by service running cn  ").append(port).append(")");

    return outData.toString();
  }
}
