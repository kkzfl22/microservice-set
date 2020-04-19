package com.liujun.micorservice.oauth2.join.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 接入github的主启动应用服务
 *
 * @author liujun
 * @version 0.0.1
 */
@SpringBootApplication
public class JoinGitHubApplication {

  public static void main(String[] args) {
    SpringApplication.run(JoinGitHubApplication.class, args);
  }
}
