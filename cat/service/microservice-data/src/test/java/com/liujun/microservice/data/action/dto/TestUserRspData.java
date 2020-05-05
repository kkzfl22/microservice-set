package com.liujun.microservice.data.action.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 用户查询响应信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class TestUserRspData {

  /** 返回的枚举信息 */
  private Boolean result;

  /** 错误码 */
  private int code;

  /** 错误信息 */
  private String msg;

  /** 返回结果 */
  private TestDataUserDTO data;

  /** 数据返回的条数 */
  private Long count;
}
