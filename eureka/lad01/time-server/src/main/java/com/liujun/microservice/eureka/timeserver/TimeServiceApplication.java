package com.liujun.microservice.eureka.timeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TimeServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TimeServiceApplication.class, args);
  }
}
