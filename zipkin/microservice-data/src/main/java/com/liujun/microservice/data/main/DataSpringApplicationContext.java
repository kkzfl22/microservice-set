package com.liujun.microservice.data.main;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring application context
 * @author lianhl
 * @version 0.0.1
 * @date 2019/12/17
 */
@Component
public class DataSpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
  public static ApplicationContext getApplicationContext() {
    return context;
  }
  /**
   * 根据类型获取bean
   * @param requiredType
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> requiredType){
    return getApplicationContext().getBean(requiredType);
  }
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name){
    return (T) getApplicationContext().getBean(name);
  }
}
