package com.liujun.microservice.demo.cat.custom;

import com.liujun.microservice.demo.cat.custom.config.CatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 前轩调用链的恢复，使用filter，对之前的调用链进行恢复操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class CatFilterConfigure {

  @Bean
  public FilterRegistrationBean catFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    CatFilter filter = new CatFilter();
    registration.setFilter(filter);
    registration.addUrlPatterns("/*");
    registration.setName("cat-filter");
    registration.setOrder(1);
    return registration;
  }
}
