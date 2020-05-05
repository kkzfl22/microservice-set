package com.liujun.microservice.demo.cat.backoffice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

  /** 调用链操作接口 */
  @Autowired private Tracer tracer;

  @Value("${service3.address:localhost:8097}")
  private String serviceAddress3;

  @Value("${service4.address:localhost:8096}")
  private String serviceAddress4;

  @Value("${service5.address:localhost:9023}")
  private String serviceAddress5;

  /**
   * 聚合服务
   *
   * @return
   */
  @RequestMapping(value = "/data", method = RequestMethod.GET)
  public String convertService() throws InterruptedException {
    log.info("backoffice start");

    Thread.sleep(1000);
    String customData = rest.getForObject("http://" + serviceAddress3 + "/custom", String.class);

    log.info("backoffice request customdata : {}", customData);

    String userInfoData =
        rest.getForObject("http://" + serviceAddress4 + "/userinfo", String.class);

    log.info("backoffice request userinfo : {}", userInfoData);

    Map<String, String> dataValue = new HashMap<>();
    dataValue.put("page", "1");
    dataValue.put("pageSize", "10");
    // 将对象装入HttpEntity中
    HttpEntity<Map> request = new HttpEntity<>(dataValue);

    String outRsp =
        rest.postForObject(
            "http://" + serviceAddress5 + "/user/queryPage.action", request, String.class);

    log.info("backoffice request queryPage : {}", outRsp);

    log.info("backoffice finish");

    return String.format(
        "custom [%s] , userinfodata [%s] , data rs [%s]", customData, userInfoData, outRsp);
  }

  /**
   * 用来模拟readtimeout的请求
   *
   * @return
   */
  @RequestMapping(value = "/connecTimeOut", method = RequestMethod.GET)
  public String connectTimeOut() throws InterruptedException {

    Span span = this.tracer.createSpan("microservice-backoffice");

    try {
      Thread.sleep(1000);
      String timeOutData =
          rest.getForObject(
              "http://localhost:" + MockService.MOCK_PORT + "/readtimeout", String.class);
      return "blow up";
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw e;
    } finally {
      this.tracer.close(span);
    }
  }

  /**
   * 用来模拟readtimeout的请求
   *
   * @return
   */
  @RequestMapping(value = "/getData", method = RequestMethod.GET)
  public String getData() throws InterruptedException {
    return "test Data";
  }
}
