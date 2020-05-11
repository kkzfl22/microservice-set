package com.liujun.microservice.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liujun
 * @version 0.0.1
 */
@RestController
public class TimeClientAction {

  @Autowired private RestTemplate restTemplate;

  /** 使用封装的接口来进行获取 */
  @Autowired private DiscoveryClient discoveryClient;

  //  @GetMapping
  //  public String getTime() {
  //    return restTemplate.getForEntity("http://time-server", String.class).getBody();
  //  }
  //
  //  @Bean
  //  @LoadBalanced
  //  public RestTemplate restTemplate() {
  //    return new RestTemplate();
  //  }

  /**
   * 通过自定义获取ip列表的方式进行路由
   *
   * @return
   */
  @GetMapping("/eurekaRoute")
  public String getEurekaRoteExecute() {
    String result = "defaultData";

    List<ServiceInstance> listInstances = discoveryClient.getInstances("time-server");

    if (null != listInstances && !listInstances.isEmpty()) {

      int size = listInstances.size();

      int randomIndex = ThreadLocalRandom.current().nextInt(size);

      ServiceInstance oneInstance = listInstances.get(randomIndex);

      URI produceURI =
          URI.create(String.format("http://%s:%s", oneInstance.getHost(), oneInstance.getPort()));

      result = restTemplate.getForObject(produceURI, String.class);
    }

    return result;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
