package com.liujun.microservice.demo.cat.backoffice;

import com.liujun.microservice.demo.cat.backoffice.config.CatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配制filter
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class CatFilterConfigure {

  @Bean
  public FilterRegistrationBean catFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    CatFilter filterInstance = new CatFilter();
    registration.setFilter(filterInstance);
    registration.addUrlPatterns("/*");
    registration.setName("cat-filter");
    registration.setOrder(1);
    return registration;
  }
}
