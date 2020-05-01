package com.liujun.microservice.demo.cat.custom.config;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class CatContext implements Cat.Context {

  private Map<String, String> catContextMap = new HashMap<>();

  @Override
  public void addProperty(String key, String value) {
    catContextMap.put(key, value);
  }

  @Override
  public String getProperty(String key) {
    return catContextMap.get(key);
  }
}
