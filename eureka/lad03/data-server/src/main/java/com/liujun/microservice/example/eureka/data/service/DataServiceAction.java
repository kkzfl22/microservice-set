package com.liujun.microservice.example.eureka.data.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 后台数据操作服务
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/dataServer")
public class DataServiceAction {

  @Value("${server.port}")
  private int port;

  @GetMapping
  public String getData() {
    StringBuilder outData = new StringBuilder();

    outData.append("outData time").append(new Date().toString());
    outData.append(", to server port : (").append(port).append(")");

    return outData.toString();
  }
}
