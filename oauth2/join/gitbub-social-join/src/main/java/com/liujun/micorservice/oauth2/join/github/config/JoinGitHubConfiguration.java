package com.liujun.micorservice.oauth2.join.github.config;

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
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;

/**
 * 接入gihub所需的配制的bean贪睡
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableSocial
@EnableConfigurationProperties(GitHubProperties.class)
public class JoinGitHubConfiguration extends SocialAutoConfigurerAdapter {

  private final GitHubProperties properties;

  public JoinGitHubConfiguration(GitHubProperties properties) {
    this.properties = properties;
  }

  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
  public GitHub gitHub(ConnectionRepository repository) {
    Connection<GitHub> getGitHubConn = repository.findPrimaryConnection(GitHub.class);

    System.out.println("执行gitHub");
    for (int i = 0; i < 100; i++) {
      System.out.print("--");
    }
    System.out.println();

    return getGitHubConn != null ? getGitHubConn.getApi() : null;
  }

  public ConnectController connectController(
      ConnectionFactoryLocator factoryLocator, ConnectionRepository repository) {
    System.out.println("执行connectController");
    for (int i = 0; i < 100; i++) {
      System.out.print("*");
    }
    System.out.println();

    ConnectController controller = new ConnectController(factoryLocator, repository);
    controller.setApplicationUrl("http://localhost:8080");
    return controller;
  }

  @Override
  protected ConnectionFactory<?> createConnectionFactory() {
    System.out.println("执行createConnectionFactory");
    for (int i = 0; i < 100; i++) {
      System.out.print("+");
    }
    System.out.println();
    // 将github的应用id与secret令牌带入
    return new GitHubConnectionFactory(properties.getAppId(), properties.getAppSecret());
  }
}
