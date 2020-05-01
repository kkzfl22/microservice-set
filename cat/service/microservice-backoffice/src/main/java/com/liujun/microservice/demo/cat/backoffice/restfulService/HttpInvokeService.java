package com.liujun.microservice.demo.cat.backoffice.restfulService;

import com.liujun.microservice.demo.cat.backoffice.config.CatResTemplateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * 提供resttemplate的调用操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class HttpInvokeService {

  /**
   * 用来获取restful的接口调用
   *
   * @return
   */
  @Bean
  public RestTemplate restTemplate() {
    SimpleClientHttpRequestFactory clientRestFactory = new SimpleClientHttpRequestFactory();
    clientRestFactory.setChunkSize(2000);
    clientRestFactory.setReadTimeout(3000);
    RestTemplate restTemplate = new RestTemplate(clientRestFactory);

    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    // 设置拦截器
    restTemplate.setInterceptors(Arrays.asList(new CatResTemplateInterceptor()));

    return restTemplate;
  }
}
