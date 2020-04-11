package com.liujun.microservice.oauth2.client.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication(scanBasePackages = {"com.liujun.microservice.oauth2.client.webclient"})
public class AuthcodeWebclientApplication implements ServletContextInitializer {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(AuthcodeWebclientApplication.class, args);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    servletContext.getSessionCookieConfig().setName("webclient-session");
  }
}
