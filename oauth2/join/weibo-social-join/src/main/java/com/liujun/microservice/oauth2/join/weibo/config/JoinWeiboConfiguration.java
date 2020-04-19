package com.liujun.microservice.oauth2.join.weibo.config;

import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.weibo.api.Weibo;
import org.springframework.social.weibo.api.impl.WeiboTemplate;
import org.springframework.social.weibo.connect.WeiboConnectionFactory;

/**
 * 接入gihub所需的配制的bean贪睡
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableSocial
@EnableConfigurationProperties(WeiboProperties.class)
public class JoinWeiboConfiguration extends SocialAutoConfigurerAdapter {

  private final WeiboProperties properties;

  public JoinWeiboConfiguration(WeiboProperties properties) {
    this.properties = properties;
  }

  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public Weibo weibo(ConnectionRepository repository) {
    Connection<Weibo> getGitHubConn = repository.findPrimaryConnection(Weibo.class);

    return getGitHubConn != null ? getGitHubConn.getApi() : null;
  }

  public ConnectController connectController(
      ConnectionFactoryLocator factoryLocator, ConnectionRepository repository) {
    ConnectController controller = new ConnectController(factoryLocator, repository);
    controller.setApplicationUrl("http://localhost:8080");
    return controller;
  }

  @Override
  protected ConnectionFactory<?> createConnectionFactory() {
    // 将github的应用id与secret令牌带入
    return new WeiboConnectionFactory(properties.getAppId(), properties.getAppSecret());
  }
}
