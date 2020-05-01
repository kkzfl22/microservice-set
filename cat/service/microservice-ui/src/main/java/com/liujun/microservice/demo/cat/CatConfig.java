package com.liujun.microservice.demo.cat;

import com.liujun.microservice.demo.cat.filter.CatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将cat加入到springboot的流程中
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class CatConfig {

  @Bean
  public FilterRegistrationBean catFilter() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    CatFilter catFilter = new CatFilter();
    registrationBean.setFilter(catFilter);
    registrationBean.addUrlPatterns("/*");
    registrationBean.setName("cat-filter");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
