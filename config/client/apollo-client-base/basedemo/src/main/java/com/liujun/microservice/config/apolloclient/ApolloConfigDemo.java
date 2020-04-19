package com.liujun.microservice.config.apolloclient;

import com.ctrip.framework.apollo.*;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.internals.JsonConfigFile;
import com.ctrip.framework.apollo.internals.YamlConfigFile;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.ctrip.framework.foundation.Foundation;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 用于演示数据读取与通知
 *
 * <p>在vm options中配制-Dapp.id=SampleApp -Denv=dev -Ddev_meta=http://192.168.3.117:8080
 *
 * @author liujun
 * @version 0.0.1
 */
@Slf4j
public class ApolloConfigDemo {

  private static final Logger logger = LoggerFactory.getLogger(ApolloConfigDemo.class);

  /** 默认值 */
  private String DEFAULT_VALUE = "undefined";

  /** 配制中心的私有属性的配制 */
  private Config config;

  /** 配制中心的yaml配制的属性信息 */
  private Config yamlConfig;

  /** 公有域属性信息 */
  private Config publicConfig;

  /** 私有属性的配制文件信息 */
  private ConfigFile applicationConfigFile;

  /** xml文件的配制 */
  private ConfigFile xmlConfigFile;

  /** yaml文件的配制 */
  private YamlConfigFile yamlConfigFile;

  /** json数据 */
  private JsonConfigFile jsonConfigFile;

  private static final String DEFAULT_APPLICATION = "application";
  private static final String XML_APPLICATION = "xml";
  private static final String YAML_APPLICATION = "yaml";
  private static final String JSON_APPLICATION = "json";

  /** 构建方法初始化相应的参数 */
  public ApolloConfigDemo() {
    // 1,初始化监听更新器
    ConfigChangeListener changeListener =
        new ConfigChangeListener() {
          public void onChange(ConfigChangeEvent changeEvent) {
            logger.info("change for namespace : {}", changeEvent.getNamespace());
            // 遍历更新的数据key，进行提示
            for (String key : changeEvent.changedKeys()) {
              ConfigChange change = changeEvent.getChange(key);
              logger.info(
                  "Change - key : {} ,olevalue : {} newValue : {} changeType:{}",
                  change.getPropertyName(),
                  change.getOldValue(),
                  change.getNewValue(),
                  change.getChangeType());
            }
          }
        };
    // 初始化相关对象
    config = ConfigService.getAppConfig();
    // 设置监听器
    config.addChangeListener(changeListener);
    // yaml文件的读取
    yamlConfig = ConfigService.getConfig("application.yaml");
    // 设置监听器
    yamlConfig.addChangeListener(changeListener);
    // 公共命名空间的数据读取
    publicConfig = ConfigService.getConfig("test1.publicnamespace");
    publicConfig.addChangeListener(changeListener);

    // 将application空间下的所有属性读取到文件中
    applicationConfigFile =
        ConfigService.getConfigFile(DEFAULT_APPLICATION, ConfigFileFormat.Properties);
    // 将xml文件的配制读取到文件中
    xmlConfigFile = ConfigService.getConfigFile("readxml", ConfigFileFormat.XML);
    xmlConfigFile.addChangeListener(
        new ConfigFileChangeListener() {
          public void onChange(ConfigFileChangeEvent changeEvent) {
            logger.info("xml file change :" + changeEvent.toString());
          }
        });
    yamlConfigFile =
        (YamlConfigFile) ConfigService.getConfigFile("application", ConfigFileFormat.YAML);
    yamlConfigFile.addChangeListener(
        new ConfigFileChangeListener() {
          public void onChange(ConfigFileChangeEvent changeEvent) {
            logger.info("yaml file change :" + changeEvent.toString());
          }
        });

    jsonConfigFile =
        (JsonConfigFile) ConfigService.getConfigFile("jsonValue", ConfigFileFormat.JSON);
    jsonConfigFile.addChangeListener(
        new ConfigFileChangeListener() {
          public void onChange(ConfigFileChangeEvent changeEvent) {
            logger.info("json file change :" + changeEvent.toString());
          }
        });
  }

  public void printEnvInfo() {
    String messageData =
        String.format(
            "Appid : %s ,env : %s , dc %s , IP : %s ",
            Foundation.app(),
            Foundation.net(),
            Foundation.server().getEnvType(),
            Foundation.server().getDataCenter(),
            Foundation.net().getHostAddress());
    System.out.println(messageData);
  }

  private void print(String namespace) {
    switch (namespace) {
      case DEFAULT_APPLICATION:
        print(applicationConfigFile);
        break;
      case XML_APPLICATION:
        print(xmlConfigFile);
        break;
      case YAML_APPLICATION:
        print(yamlConfigFile);
        break;
      case JSON_APPLICATION:
        print(jsonConfigFile);
        break;
    }
  }

  private void print(ConfigFile cfgFile) {
    // 如果当前不包括文件内容
    if (!cfgFile.hasContent()) {
      System.out.println("no config file fount for :" + cfgFile.getNamespace());
      return;
    }
    System.out.println("config context for " + cfgFile.getNamespace() + " is as allows:");
    System.out.println(cfgFile.getContent());
  }

  private void print(YamlConfigFile cfgFile) {
    // 如果当前不包括文件内容
    if (!cfgFile.hasContent()) {
      System.out.println("yaml no config file fount for :" + cfgFile.getNamespace());
      return;
    }
    System.out.println("yaml config context for " + cfgFile.getNamespace() + " is as allows:");
    System.out.println(cfgFile.asProperties());
  }

  private void print(JsonConfigFile cfgFile) {
    // 如果当前不包括文件内容
    if (!cfgFile.hasContent()) {
      System.out.println("json no config file fount for :" + cfgFile.getNamespace());
      return;
    }
    System.out.println("json config context for " + cfgFile.getNamespace() + " is as allows:");
    System.out.println(cfgFile.getContent());
  }

  private void printByKey(String key) {
    String result = config.getProperty(key, DEFAULT_VALUE);
    // 如果未取到值，则从公共的namespace去取数据
    if (DEFAULT_VALUE.equals(result)) {
      result = publicConfig.getProperty(key, DEFAULT_VALUE);
    }

    // 如果公共也取不到数据，则从yaml中获取法
    if (DEFAULT_VALUE.equals(result)) {
      result = yamlConfig.getProperty(key, DEFAULT_VALUE);
    }
    String outdata = String.format("load key %s ,value : %s", key, result);
    System.out.println(outdata);
  }

  public static void main(String[] args) throws IOException {
    System.out.println("start...");
    ApolloConfigDemo configDemo = new ApolloConfigDemo();
    // 打印环境信息
    configDemo.printEnvInfo();
    System.out.println("Apollo Config demo,please input key to get the value:");
    while (true) {
      System.out.print(">");
      String input =
          new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8)).readLine();
      if (null == input || input.isEmpty()) {
        continue;
      }
      input = input.trim();

      try {
        // 如果输入application则打印默认的命名空间下的所有数据信息
        if (input.equals(DEFAULT_APPLICATION)) {
          configDemo.print(DEFAULT_APPLICATION);
          continue;
        }
        // 如果输入的是xml
        if (input.equals(XML_APPLICATION)) {
          configDemo.print(XML_APPLICATION);
          continue;
        }

        // 如果输入的是yaml文件
        if (input.equals(YAML_APPLICATION)) {
          configDemo.print(YAML_APPLICATION);
          continue;
        }

        // 如果输入的是json文件
        if (input.equals(JSON_APPLICATION)) {
          configDemo.print(JSON_APPLICATION);
          continue;
        }

        // 其他情况打印输入的key信息
        configDemo.printByKey(input);
      } catch (Exception e) {
        logger.error("ApolloConfigDemo error", e);
      }
    }
  }
}
