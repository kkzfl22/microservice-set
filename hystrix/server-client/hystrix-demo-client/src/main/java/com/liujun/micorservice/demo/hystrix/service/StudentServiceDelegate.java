package com.liujun.micorservice.demo.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * 学生的服务类
 *
 * @author liujun
 * @version 0.0.1
 */
@Service
@Slf4j
public class StudentServiceDelegate {

  /** 请求的模拟操作 */
  @Autowired private RestTemplate restTemplate;

  private static final String SERVER_URL =
      "http://127.0.0.1:8084/studentController/getStudentBySchool/{schoolName}";

  // @HystrixCommand
  //  @HystrixCommand(
  //      commandProperties = {
  //        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value =
  // "4000")
  //      })
  // @HystrixCommand(fallbackMethod = "failBackStudent")
  @HystrixCommand(
      fallbackMethod = "failBackStudent",
      threadPoolKey = "studentServiceThreadPool",
      threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "30"),
        @HystrixProperty(name = "maxQueueSize", value = "10")
      })
  public String callStudentService(String schoolName) {
    log.info("call student service start ,school name {}", schoolName);
    String response =
        restTemplate
            .exchange(
                SERVER_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {},
                schoolName)
            .getBody();

    log.info("url : {} rsp {}", SERVER_URL, response);

    return "NORMAL server !!! school name " + schoolName + ",student details :" + response;
  }

  @Bean
  public RestTemplate template() {
    return new RestTemplate();
  }

  /**
   * 降级的函数
   *
   * @param schoolName 学生的名称
   * @return 返回错误信息
   */
  public String failBackStudent(String schoolName) {
    log.info("call student service start ,school name {}", schoolName);

    return "fail back  server !!! school name "
        + schoolName
        + ",student details : full bacl curr time:"
        + new Date();
  }
}
