package com.liujun.microservice.zuul.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
@RestController
public class DemoData {

  @GetMapping(value = "/demo/{name}")
  public String getDataStr(@PathVariable(name = "name") String name) {
    return "this is test data demo, welcome to :" + name;
  }

  @PostMapping("/demo/{name}")
  public Map<String, String> getDataBean(@PathVariable(name = "name") String name) {
    Map<String, String> datamap = new HashMap<>(2);
    datamap.put("key", "data key");
    datamap.put("value2", "value2");
    datamap.put("name", name);

    return datamap;
  }
}
