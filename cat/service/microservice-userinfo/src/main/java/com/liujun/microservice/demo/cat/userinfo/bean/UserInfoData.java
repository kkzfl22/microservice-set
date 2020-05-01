package com.liujun.microservice.demo.cat.userinfo.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class UserInfoData {

  /** 用户的id */
  private String userId;

  /** 用户名 */
  private String userName;

  /** 用户性别 */
  private String sex;

  /** 身高 */
  private Integer high;
}
