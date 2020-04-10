package com.liujun.microservice.auth2.implicit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * auth2的授权服务器
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableAuthorizationServer
public class Auth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("liujunapp")
        .secret("123456")
        .redirectUris("http://localhost:9002/callback")
        // 简化模式
        .authorizedGrantTypes("implicit")
        // 有效期
        .accessTokenValiditySeconds(120)
        .scopes("read", "write");
  }
}
