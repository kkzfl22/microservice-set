package com.liujun.microservice.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TimeClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(TimeClientApplication.class, args);
  }
}
