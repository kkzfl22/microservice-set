package com.liujun.microservice.ereka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author liujun
 * @version 0.0.1
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaServiceApplication.class, args);
  }
}
