package com.liujun.microservice.oauth2.client.webclient.repository;

import lombok.Data;

/**
 * 接口获取的用户信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
public class AdapterUserInfo {

  private String id;

  private String username;

  private String userage;

  private String hight;

  private String email;
}
