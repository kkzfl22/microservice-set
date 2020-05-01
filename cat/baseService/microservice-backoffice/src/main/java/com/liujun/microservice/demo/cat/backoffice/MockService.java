package com.liujun.microservice.demo.cat.backoffice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 模拟服务器信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
public class MockService {

  public static final int MOCK_PORT = 8093;

  /** 模拟服务器对象信息 */
  WireMock wireMock = new WireMock(MOCK_PORT);

  WireMockServer wireMockServer = new WireMockServer(MOCK_PORT);

  @PostConstruct
  public void setup() {
    wireMockServer.start();
    wireMock.register(any(urlMatching(".*")).willReturn(aResponse().withFixedDelay(3000)));
  }

  @PreDestroy
  public void shutdown() {
    wireMock.shutdown();
    wireMockServer.shutdown();
  }
}
