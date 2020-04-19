package com.liujun.micorservice.oauth2.join.github.config;

import org.springframework.boot.autoconfigure.social.SocialProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liujun
 * @version 0.0.1
 */
@ConfigurationProperties(prefix = "spring.social.github")
public class GitHubProperties extends SocialProperties {
}
