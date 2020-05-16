package com.liujun.microservice.example.eureka.data.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DataServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(DataServiceApplication.class, args);
  }
}
