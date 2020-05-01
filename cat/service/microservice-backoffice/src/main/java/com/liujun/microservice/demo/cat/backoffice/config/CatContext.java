package com.liujun.microservice.demo.cat.backoffice.config;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来进调用链保存相关信息的容器
 *
 * @author liujun
 * @version 0.0.1
 */
public class CatContext implements Cat.Context {

  private Map<String, String> contextData = new HashMap<>();

  @Override
  public void addProperty(String key, String value) {
    contextData.put(key, value);
  }

  @Override
  public String getProperty(String key) {
    return contextData.get(key);
  }
}
