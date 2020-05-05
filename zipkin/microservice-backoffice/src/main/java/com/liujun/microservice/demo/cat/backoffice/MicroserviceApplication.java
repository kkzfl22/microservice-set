package com.liujun.microservice.demo.cat.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 应用服务的启动入口
 *
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
public class MicroserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MicroserviceApplication.class, args);
  }

  /**
   * 用来获取restful的接口调用
   *
   * @return
   */
  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory clientRestFactory = new SimpleClientHttpRequestFactory();
   // clientRestFactory.setChunkSize(50000);
    clientRestFactory.setReadTimeout(50000);
    RestTemplate restTemplate = new RestTemplate(clientRestFactory);

    return restTemplate;
  }
}
