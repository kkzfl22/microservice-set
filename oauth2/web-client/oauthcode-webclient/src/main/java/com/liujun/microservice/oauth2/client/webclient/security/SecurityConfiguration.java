package com.liujun.microservice.oauth2.client.webclient.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 进行安全的相关配制
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired private UserDetailsService userDetailsService;

  /**
   * 用来调用用户查询接口接口，以作详细的用户操作令牌接口
   *
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/", "/index.html")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .logout()
        .permitAll()
        .and()
        .csrf()
        .disable();
  }
}
