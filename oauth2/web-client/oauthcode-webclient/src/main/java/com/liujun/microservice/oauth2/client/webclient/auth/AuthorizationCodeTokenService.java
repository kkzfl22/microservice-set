package com.liujun.microservice.oauth2.client.webclient.auth;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权码获取操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Service
public class AuthorizationCodeTokenService {

  public String getAuthorizationEndpoint() {

    String endpoint = "http://localhost:8080/oauth/authorize";

    Map<String, String> authParameter = new HashMap<>();
    authParameter.put("client_id", "liujunapp");
    authParameter.put("response_type", "code");
    authParameter.put("redirect_uri", "http://localhost:9002/callback");
    authParameter.put("scope", getEncodedUrl("read"));

    return this.buildUrl(endpoint, authParameter);
  }

  private String buildUrl(String endPoint, Map<String, String> param) {
    StringBuilder outUrl = new StringBuilder();
    outUrl.append(endPoint);
    outUrl.append("?");
    for (Map.Entry<String, String> entryItem : param.entrySet()) {
      outUrl.append(entryItem.getKey()).append("=").append(entryItem.getValue()).append("&");
    }
    outUrl.deleteCharAt(outUrl.length() - 1);

    return outUrl.toString();
  }

  private String getEncodedUrl(String url) {
    try {
      return URLEncoder.encode(url, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
