package com.liujun.microservice.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 当前为一个eureka的服务器
 *
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaServiceApplication.class, args);
  }
}
