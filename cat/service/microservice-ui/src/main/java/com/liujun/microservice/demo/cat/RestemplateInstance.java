package com.liujun.microservice.demo.cat;

import com.liujun.microservice.demo.cat.filter.RestTemplateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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
    // 将拦载注入
    template.setInterceptors(Arrays.asList(new RestTemplateInterceptor()));
    return template;
  }
}
