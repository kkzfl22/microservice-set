package com.liujun.microservice.config.apolloclient.spring.annotation.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 加载默认命名空间下配制数据
 *
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@EnableApolloConfig(
    value = {"application"},
    order = 10)
public class AppConfig {}
