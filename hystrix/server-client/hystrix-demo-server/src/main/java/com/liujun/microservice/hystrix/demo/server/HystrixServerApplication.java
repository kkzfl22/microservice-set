package com.liujun.microservice.hystrix.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务类
 *
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
public class HystrixServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(HystrixServerApplication.class, args);
  }
}
