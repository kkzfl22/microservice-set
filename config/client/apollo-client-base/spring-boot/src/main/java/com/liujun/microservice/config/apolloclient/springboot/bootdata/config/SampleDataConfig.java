package com.liujun.microservice.config.apolloclient.springboot.bootdata.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于演示springboot与apollo过行集成，因为数据不能自动刷新，需要配制refresh事件进行
 *
 * <p>实验时，先在配制中心将data.cache.enabled设置为true
 *
 * <p>data.cache.enabled=true，此模块才会被启用
 *
 * <p>@ConfigurationProperties(prefix = "redis.cache")用于指定前缀
 *
 * @author liujun
 * @version 0.0.1
 */
@ConditionalOnProperty("data.cache.enabled")
@ConfigurationProperties(prefix = "data.cache")
@Service("sampleDataConfig")
@RefreshScope
@Slf4j
@Data
@ToString
public class SampleDataConfig {

  private int maxCfg;

  private String dataRemark;

  private Map<String, String> dataMap = Maps.newHashMap();

  private List<String> listdata = Lists.newArrayList();

  @PostConstruct
  private void init() {
    log.info(
        "SampleDataConfig init max : {} ,dataRemark : {} ,dataRemark : {} ,dataMap : {} ,listdata : {}",
        maxCfg,
        dataRemark,
        dataMap,
        listdata);
  }
}
