package com.liujun.microservice.demo.cat.backoffice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 对外提供统一的服务,作为聚合服务
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/convert")
@Slf4j
public class BackOfficeAction {

  /** 聚合服务调用 */
  @Autowired private RestTemplate rest;

  /**
   * 聚合服务
   *
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String convertService() throws InterruptedException {
    log.info("backoffice start");

    Thread.sleep(1000);
    String customData =
        rest.getForObject("http://localhost:8097/microservice-custom/custom", String.class);

    log.info("backoffice request customdata : {}", customData);

    String userInfoData =
        rest.getForObject("http://localhost:8096/microservice-userinfo/userinfo", String.class);

    log.info("backoffice request userinfo : {}", userInfoData);

    log.info("backoffice finish");

    return String.format("custom [%s] , userinfodata [%s]", customData, userInfoData);
  }

  /**
   * 用来模拟readtimeout的请求
   *
   * @return
   */
  @RequestMapping(value = "/connecTimeOut", method = RequestMethod.GET)
  public String connectTimeOut() {
    try {
      Thread.sleep(1000);
      String timeOutData =
          rest.getForObject(
              "http://localhost:" + MockService.MOCK_PORT + "/readtimeout", String.class);
      return "blow up";
    } catch (RestClientException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return "error data";
  }
}
