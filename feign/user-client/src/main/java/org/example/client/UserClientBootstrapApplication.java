package org.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example", "org.example.client.user"})
// @EnableFeignClients(basePackages = "org.example.data")
public class UserClientBootstrapApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserClientBootstrapApplication.class, args);
  }
}
