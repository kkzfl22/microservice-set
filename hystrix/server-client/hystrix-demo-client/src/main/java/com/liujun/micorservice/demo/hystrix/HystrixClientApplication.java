package com.liujun.micorservice.demo.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableHystrixDashboard
@EnableCircuitBreaker
public class HystrixClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(HystrixClientApplication.class, args);
  }
}
