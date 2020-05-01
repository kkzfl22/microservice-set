package com.liujun.microservice.demo.cat.userinfo;

import com.liujun.microservice.demo.cat.userinfo.config.CatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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
