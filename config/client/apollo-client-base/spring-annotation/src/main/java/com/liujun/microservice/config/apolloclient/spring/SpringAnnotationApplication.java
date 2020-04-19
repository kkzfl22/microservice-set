package com.liujun.microservice.config.apolloclient.spring;

import com.google.common.base.Charsets;
import com.liujun.microservice.config.apolloclient.spring.annotation.bean.CfgDataBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 使用spring注解的方法对数据进行加载与更新操作
 *
 * <p>此仅能进行数据的读取，不能进行最新值的更新
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class SpringAnnotationApplication {

  public static void main(String[] args) throws IOException {
    // 1,加载context对象
    ApplicationContext context =
        new AnnotationConfigApplicationContext(
            "com.liujun.microservice.config.apolloclient.spring.annotation");
    CfgDataBean cfgDataBean = context.getBean(CfgDataBean.class);
    System.out.println(
        "AnnotationApplication demo,input any key except quit to print the value,input quit to exit.");

    while (true) {
      System.out.print(">");
      String input =
          new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if ("quit".equals(input)) {
        System.exit(0);
      }
      System.out.println(cfgDataBean.toString());
    }
  }
}
