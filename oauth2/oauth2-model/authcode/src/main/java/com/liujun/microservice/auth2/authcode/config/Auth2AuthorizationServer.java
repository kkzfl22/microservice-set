package com.liujun.microservice.auth2.authcode.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

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
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    // 此用来支持刷新token
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("permitAll()")
        .allowFormAuthenticationForClients();

    security.allowFormAuthenticationForClients();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("liujunapp")
        .secret("123456")
        .redirectUris("http://localhost:9002/callback")
        // 授权码模式
        .authorizedGrantTypes("authorization_code","refresh_token")
        .scopes("read", "write");
  }
}
