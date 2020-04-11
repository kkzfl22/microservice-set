package com.liujun.microservice.oauth2.client.webclient.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author liujun
 * @version 0.0.1
 */
@Data
public class OAuthToken {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private String expiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;
}
