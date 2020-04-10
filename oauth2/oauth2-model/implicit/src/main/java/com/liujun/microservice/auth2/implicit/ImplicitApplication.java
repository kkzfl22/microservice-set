package com.liujun.microservice.auth2.implicit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.liujun.microservice.auth2.implicit"})
public class ImplicitApplication {

  public static void main(String[] args) throws InterruptedException {

    SpringApplication.run(ImplicitApplication.class, args);
  }
}
