package com.liujun.microservice.eureka.client.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 提供聚合服务的中音客户端面
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/dataClientAction")
public class DataClientAction {

  @Value("${server.port}")
  private int port;

  /** 注入模板请求对象 */
  @Autowired private RestTemplate restTemplate;

  @GetMapping
  public String getDataValue() {
    String url = "http://data-server/dataServer";
    String dataClientJson = restTemplate.getForObject(url, String.class);

    return "out data " + dataClientJson + ", outService:" + port;
  }
}
