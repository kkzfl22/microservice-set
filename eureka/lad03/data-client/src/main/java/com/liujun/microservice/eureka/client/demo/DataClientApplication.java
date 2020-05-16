package com.liujun.microservice.eureka.client.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 数据客户端
 *
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DataClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(DataClientApplication.class, args);
  }
}
