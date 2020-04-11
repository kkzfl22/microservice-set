package com.liujun.microservice.oauth2.client.webclient.auth;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;

/**
 * 授权码获取操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Service
public class AuthorizationTokenService {

  private String encodeCredentials(String clientId, String password) {
    String credentials = clientId + ":" + password;
    String encode = new String(Base64.getEncoder().encode(credentials.getBytes()));

    return encode;
  }

  private MultiValueMap<String, String> getBody(String authorizationCode) {
    MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();

    bodyMap.add("grant_type", "authorization_code");
    bodyMap.add("scope", "read");
    bodyMap.add("code", authorizationCode);
    bodyMap.add("redirect_uri", "http://localhost:9002/callback");

    return bodyMap;
  }

  private HttpHeaders getHeader(String clientAuthentication) {
    HttpHeaders httpHeaders = new HttpHeaders();

    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    httpHeaders.add("Authorization", "Basic " + clientAuthentication);

    return httpHeaders;
  }

  public OAuthToken getToken(String code) {
    RestTemplate rest = new RestTemplate();
    String credentials = this.encodeCredentials("liujunapp", "123456");
    RequestEntity<MultiValueMap<String, String>> requestEntity =
        new RequestEntity<>(
            this.getBody(code),
            this.getHeader(credentials),
            HttpMethod.POST,
            URI.create("http://localhost:8080/oauth/token"));

    ResponseEntity<OAuthToken> responseEntity = rest.exchange(requestEntity, OAuthToken.class);

    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      return responseEntity.getBody();
    }

    throw new RuntimeException("error trying to retrive access token");
  }
}
