package com.liujun.microservice.demo.cat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

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

  /** 调用链跟踪 */
  @Autowired private Tracer tracer;

  @Value("${service2.address:localhost:8095}")
  private String serviceAddress;

  /**
   * 测试获取数据流程
   *
   * @return
   * @throws InterruptedException
   */
  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public String start(HttpServletRequest request) throws InterruptedException {
    log.info("ui start..");
    Thread.sleep(500);
    // 调用获取数据
    String converData =
        restTemplate.getForObject("http://" + serviceAddress + "/convert/data", String.class);

    log.info("back office request customdata : {}", converData);

    return converData;
  }

  /**
   * 测试超时的情况
   *
   * @return
   */
  @RequestMapping(value = "/connectTimeOut", method = RequestMethod.GET)
  public String timeout(HttpServletRequest request) throws InterruptedException {

    Span span = this.tracer.createSpan("microservice-ui");

    try {
      Thread.sleep(1000);
      String getData =
          restTemplate.getForObject("http://" + serviceAddress + "/convert/connecTimeOut", String.class);
      return getData;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      this.tracer.close(span);
    }
  }


  /**
   * 测试超时的情况
   *
   * @return
   */
  @RequestMapping(value = "/getData", method = RequestMethod.GET)
  public String getData(HttpServletRequest request) throws InterruptedException {

    Span span = this.tracer.createSpan("microservice-ui");

    try {
      Thread.sleep(1000);
      String getData =
              restTemplate.getForObject("http://" + serviceAddress + "/convert/getData", String.class);
      return getData;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      this.tracer.close(span);
    }
  }

}
