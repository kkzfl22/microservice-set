package com.liujun.microservice.data.common;

import lombok.Getter;
import lombok.ToString;

/**
 * @author liujun
 * @version 0.0.1
 */
@Getter
@ToString
public enum APICodeEnum {
  SUCCESS(1, "操作成功"),

  FAIL(-1, "操作失败"),
  ;

  private int code;

  private String msg;

  APICodeEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
