package com.liujun.microservice.auth2.password.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * auth2的授权服务器
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableAuthorizationServer
public class Auth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

  /** 用户认证 */
  @Autowired private AuthenticationManager authenticationManager;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("liujunapp")
        .secret("123456")
        // 密码模式
        .authorizedGrantTypes("password")
        .scopes("read", "write");
  }
}
