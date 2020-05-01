package com.liujun.microservice.demo.cat.userinfo.config;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class CatContext implements Cat.Context {

  private Map<String, String> catContext = new HashMap<>();

  @Override
  public void addProperty(String key, String value) {
    catContext.put(key, value);
  }

  @Override
  public String getProperty(String key) {
    return catContext.get(key);
  }
}
