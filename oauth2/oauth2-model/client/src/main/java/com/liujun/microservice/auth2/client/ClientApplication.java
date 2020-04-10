package com.liujun.microservice.auth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.liujun.microservice.auth2.client"})
public class ClientApplication {

  public static void main(String[] args) throws InterruptedException {

    SpringApplication.run(ClientApplication.class, args);
  }
}
