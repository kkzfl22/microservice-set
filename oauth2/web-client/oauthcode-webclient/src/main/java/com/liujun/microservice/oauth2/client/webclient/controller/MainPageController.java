package com.liujun.microservice.oauth2.client.webclient.controller;

import com.liujun.microservice.oauth2.client.webclient.auth.AuthorizationCodeTokenService;
import com.liujun.microservice.oauth2.client.webclient.auth.AuthorizationTokenService;
import com.liujun.microservice.oauth2.client.webclient.auth.OAuthToken;
import com.liujun.microservice.oauth2.client.webclient.repository.AdapterUserInfo;
import com.liujun.microservice.oauth2.client.webclient.repository.AuthUserRepository;
import com.liujun.microservice.oauth2.client.webclient.security.WebClientUserDetails;
import com.liujun.microservice.oauth2.client.webclient.user.AuthWebclientUser;
import com.liujun.microservice.oauth2.client.webclient.user.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 控制请求类操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Controller
public class MainPageController {

  /** 授权码请求 */
  @Autowired private AuthorizationCodeTokenService authCodeService;

  @Autowired private AuthorizationTokenService tokenService;

  @Autowired private AuthUserRepository userRepository;

  /**
   * 去主页
   *
   * @return
   */
  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/mainpage")
  public ModelAndView mainPage() {
    // 从请求中获取用户信息
    WebClientUserDetails userDetails =
        (WebClientUserDetails)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    AuthWebclientUser authUser = userDetails.getAuthWebClientUser();

    // 如果当前还没有用户信息，需要发起auth流程获取授权码并得到用户信息
    if (null == authUser.getAccessToken()) {
      // 1,获取授权码
      String authEndpoint = authCodeService.getAuthorizationEndpoint();
      return new ModelAndView("redirect:" + authEndpoint);
    }

    authUser.setEntries(Arrays.asList(new Entry("entry 1"), new Entry("entry 2")));

    ModelAndView mainView = new ModelAndView("mainpage");
    mainView.addObject("user", authUser);

    getUserInfo(mainView, authUser.getAccessToken());

    return mainView;
  }

  private void getUserInfo(ModelAndView mainPage, String token) {
    RestTemplate restTemplate = new RestTemplate();
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Authorization", "Bearer " + token);
    String endpoint = "http://localhost:8080/data/getUserInfo";

    RequestEntity<Object> request =
        new RequestEntity<>(headers, HttpMethod.GET, URI.create(endpoint));

    ResponseEntity<AdapterUserInfo> userInfoRsp =
        restTemplate.exchange(request, AdapterUserInfo.class);

    if (userInfoRsp.getStatusCode().is2xxSuccessful()) {
      mainPage.addObject("adapterUser", userInfoRsp.getBody());
    } else {
      throw new RuntimeException("it was not possible to retrieve user profile");
    }
  }

  @GetMapping("/callback")
  public ModelAndView callBack(String code, String state) {

    // 从请求中获取用户信息
    WebClientUserDetails userDetails =
        (WebClientUserDetails)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    AuthWebclientUser authUser = userDetails.getAuthWebClientUser();

    // 通过授权码获取token
    OAuthToken token = tokenService.getToken(code);
    authUser.setAccessToken(token.getAccessToken());

    // 设置有效期
    Calendar calInstance = Calendar.getInstance();
    calInstance.setTime(new Date(Long.parseLong(token.getExpiresIn())));
    authUser.setAccessTokenValidity(calInstance);

    // 保存token
    userRepository.save(authUser);

    return new ModelAndView("redirect:/mainpage");
  }
}
