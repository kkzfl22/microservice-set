package com.liujun.microservice.data.action.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表的实体传输实体信息
 *
 * @version 0.0.1
 * @author liujun
 */
@Data
@ToString
public class TestDataUserDTO {

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

  /** 当前页 */
  private Integer pageNum;

  /** 每页显示的条数 */
  private Integer pageSize;

  /** 用来保存会话信息 */
  private List<String> cookie;
}
