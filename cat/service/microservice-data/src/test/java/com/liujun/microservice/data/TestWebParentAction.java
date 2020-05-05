package com.liujun.microservice.data;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liujun
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
    classes = DataApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestWebParentAction {

  /** 获取服务器端口 */
  @Value("${local.server.port}")
  protected int port;

  @Autowired protected TestRestTemplate restTemplate;
}
