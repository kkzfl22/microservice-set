package com.liujun.microservice.auth2.password;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.liujun.microservice.auth2.password"})
public class PasswordApplication {

  public static void main(String[] args) throws InterruptedException {

    SpringApplication.run(PasswordApplication.class, args);
  }
}
