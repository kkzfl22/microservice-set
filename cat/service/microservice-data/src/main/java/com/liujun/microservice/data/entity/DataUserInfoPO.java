package com.liujun.microservice.data.entity;

import lombok.Data;
import lombok.ToString;
/**
 * 用户信息表的实体信息
 *
 * @version 0.0.1
 * @author liujun
 */
@Data
@ToString
public class DataUserInfoPO {

  /** 用户手机 */
  private String userMobile;

  /** 用户邮件地址 */
  private String userEmail;

  /** 账号状态:, 0:停用, 1:正常, 2:密码错误而锁定, 3:管理员强制锁定 */
  private Integer status;

  /** 用户创建时间 */
  private Long createTime;

  /** 用户的id */
  private String oid;

  /** 用户名 */
  private String userName;

  /** 用户密码 */
  private String userPassword;

  /** 用户描述 */
  private String remark;

  private int page;

  private int pageSize;
}
