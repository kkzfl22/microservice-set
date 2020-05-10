package com.liujun.microservice.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author liujun
 * @version 0.0.1
 */
@RestController
public class TimeClientAction {

  @Autowired private RestTemplate restTemplate;

  @GetMapping
  public String getTime() {
    return restTemplate.getForEntity("http://time-server", String.class).getBody();
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
