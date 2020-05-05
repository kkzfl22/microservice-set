package com.liujun.microservice.demo.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
public class UiApplication {

  public static void main(String[] args) {
    SpringApplication.run(UiApplication.class, args);
  }
  /**
   * 用来获取restful的接口调用
   *
   * @return
   */
  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory clientRestFactory = new SimpleClientHttpRequestFactory();
    clientRestFactory.setChunkSize(2000);
    clientRestFactory.setReadTimeout(5000);
    RestTemplate restTemplate = new RestTemplate(clientRestFactory);

    return restTemplate;
  }
}
