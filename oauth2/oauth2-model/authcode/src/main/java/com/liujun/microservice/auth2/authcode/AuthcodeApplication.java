package com.liujun.microservice.auth2.authcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.liujun.microservice.auth2.authcode"})
public class AuthcodeApplication {

  public static void main(String[] args) throws InterruptedException {

    SpringApplication.run(AuthcodeApplication.class, args);
  }
}
