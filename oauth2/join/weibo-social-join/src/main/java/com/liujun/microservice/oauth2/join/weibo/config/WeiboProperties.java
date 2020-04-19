package com.liujun.microservice.oauth2.join.weibo.config;

import org.springframework.boot.autoconfigure.social.SocialProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liujun
 * @version 0.0.1
 */
@ConfigurationProperties(prefix = "spring.social.weibo")
public class WeiboProperties extends SocialProperties {
}
