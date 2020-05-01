package com.liujun.microservice.demo.cat.filter;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class CatContext implements Cat.Context {

  private Map<String, String> installMap = new HashMap<>();

  @Override
  public void addProperty(String key, String value) {
    installMap.put(key, value);
  }

  @Override
  public String getProperty(String key) {
    return installMap.get(key);
  }
}
