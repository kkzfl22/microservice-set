package com.liujun.microservice.oauth2.client.webclient.user;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author liujun
 * @version 0.0.1
 */
@Entity
@Data
public class AuthWebclientUser {

  /** 主键id的标识 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 用户名 */
  private String userName;

  /** 密码 */
  private String password;

  /** 授权的token */
  private String accessToken;

  /** token的有效期授权 */
  private Calendar accessTokenValidity;

  /** 刷新的token */
  private String refreshToken;

  @Transient private List<Entry> entries = new ArrayList<>();
}
