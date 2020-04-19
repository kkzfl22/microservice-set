package com.liujun.microservice.config.apolloclient.springboot.bootdata.config;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * 刷新的配制 @ConditionalOnProperty("data.cache.enabled")这个启用时，才进行刷新操作
 *
 * @author liujun
 * @version 0.0.1
 */
@ConditionalOnProperty("data.cache.enabled")
@Service
@Slf4j
public class SampleDataRefreshConfig {

  private final SampleDataConfig sampleDataConfig;

  private final RefreshScope refreshScope;

  public SampleDataRefreshConfig(
      final SampleDataConfig sampleDataConfig, final RefreshScope refreshScope) {
    this.sampleDataConfig = sampleDataConfig;
    this.refreshScope = refreshScope;
  }

  /** 当发生数据变更时通知 */
  @ApolloConfigChangeListener(
      value = {ConfigConsts.NAMESPACE_APPLICATION, "TEST1.publicnamespace", "application.yaml"},
      interestedKeyPrefixes = {"data.cache."})
  public void onChange(ConfigChangeEvent event) {
    log.info("before refresh {}", sampleDataConfig.toString());
    refreshScope.refresh("sampleDataConfig");
    log.info("after refresh {}", sampleDataConfig.toString());
  }
}
