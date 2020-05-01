package com.liujun.microservice.demo.cat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 显示的ui交互
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/ui")
@Slf4j
public class UIAaction {

  /** 实体操作 */
  @Autowired private RestTemplate restTemplate;

  /**
   * 测试获取数据流程
   *
   * @return
   * @throws InterruptedException
   */
  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public String start() throws InterruptedException {
    log.info("ui start..");
    Thread.sleep(500);
    // 调用获取数据
    String converData =
        restTemplate.getForObject(
            "http://127.0.0.1:8095/microservice-backoffice/convert", String.class);

    log.info("back office request customdata : {}", converData);

    return converData;
  }

  /**
   * 测试超时的情况
   *
   * @return
   */
  @RequestMapping(value = "/connectTimeOut", method = RequestMethod.GET)
  public String timeout() {
    try {
      Thread.sleep(1000);
      String getData =
          restTemplate.getForObject(
              "http://127.0.0.1:8095/microservice-backoffice/connecTimeOut", String.class);
      return "timeout data";
    } catch (RestClientException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "error data";
  }
}
