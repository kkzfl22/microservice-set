package com.liujun.microservice.auth2.jwt.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * auth的服务配制
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableAuthorizationServer
public class Auth2AuthJWTServer extends AuthorizationServerConfigurerAdapter {

  /** 用户名密码验证时需要启动 */
  @Autowired private AuthenticationManager authenticationManager;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("feifei321");

    return converter;
  }

  /**
   * sprint提供一个工具类能够产生jwt的bean对象
   *
   * @return
   */
  @Bean
  public JwtTokenStore jwtTokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // 使用用户名和密码模式时需要启用authentionManager
    endpoints
        .authenticationManager(authenticationManager)
        .tokenStore(jwtTokenStore())
        .accessTokenConverter(accessTokenConverter());
  }

  /**
   * 配制auth的服务
   *
   * @param clients
   * @throws Exception
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("liujunapp")
        .secret("123456")
        .scopes("read")
        .authorizedGrantTypes("password", "authorization_code", "refresh_token");
  }
}
