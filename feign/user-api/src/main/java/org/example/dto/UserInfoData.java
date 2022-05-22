package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户对象
 *
 * @author liujun
 * @since 2022/5/21
 */
@Getter
@Setter
@ToString
public class UserInfoData {

  /** 主键的id */
  private String id;

  /** 用户名 */
  private String userName;

  /** 密码信息 */
  private String pwd;

  /** 用户姓名 */
  private String fullName;

  /** 手机号 */
  private String phone;

  /** 邮件地址 */
  private String email;

  /** 身份证号 */
  private String cardId;

  /** 住地信息 */
  private String address;

  /** 常住城市 */
  private String city;

  /** 创建时间 */
  private Long createTime;
}
