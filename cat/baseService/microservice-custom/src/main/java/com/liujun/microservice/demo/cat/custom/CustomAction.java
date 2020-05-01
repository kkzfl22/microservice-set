package com.liujun.microservice.demo.cat.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/custom")
@Slf4j
public class CustomAction {

  @RequestMapping(method = RequestMethod.GET)
  public String getCustomData() throws InterruptedException {

    log.info("custom info start");

    Thread.sleep(2000);

    log.info("custom info finish");

    return "get custominfo";
  }
}
