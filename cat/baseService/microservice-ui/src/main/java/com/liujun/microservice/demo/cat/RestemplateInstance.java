package com.liujun.microservice.demo.cat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 获取模板操作的实体
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class RestemplateInstance {

  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate template = new RestTemplate();

    return template;
  }
}
