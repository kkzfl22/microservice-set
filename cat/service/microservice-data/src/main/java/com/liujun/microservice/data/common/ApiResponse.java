package com.liujun.microservice.data.common;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 公共的返回结果对象
 *
 * @author liujun
 * @version 0.0.1
 */
@Builder
@Data
@ToString
public class ApiResponse {

  /** 返回的枚举信息 */
  private Boolean result;

  /** 错误码 */
  private int code;

  /** 错误信息 */
  private String msg;

  /** 返回结果 */
  private Object data;

  /** 数据返回的条数 */
  private Long count;

  /**
   * 默认的成功对象
   *
   * @return
   */
  public static ApiResponse ok() {
    return ApiResponse.builder()
        .result(Boolean.TRUE)
        .code(APICodeEnum.SUCCESS.getCode())
        .msg(APICodeEnum.SUCCESS.getMsg())
        .build();
  }

  /**
   * 成功并且携带信息
   *
   * @param data
   * @return
   */
  public static ApiResponse ok(Object data) {
    return ApiResponse.builder()
        .result(Boolean.TRUE)
        .code(APICodeEnum.SUCCESS.getCode())
        .msg(APICodeEnum.SUCCESS.getMsg())
        .data(data)
        .build();
  }

  /**
   * 成功并且携带信息
   *
   * @param data
   * @return
   */
  public static ApiResponse ok(List data) {

    // 设置返回结果大小
    long size = 0;
    if (null != data && !data.isEmpty()) {
      size = data.size();
    }

    return ApiResponse.builder()
        .result(Boolean.TRUE)
        .code(APICodeEnum.SUCCESS.getCode())
        .msg(APICodeEnum.SUCCESS.getMsg())
        .data(data)
        .count(size)
        .build();
  }

  /**
   * 成功并且携带信息
   *
   * @param data
   * @return
   */
  public static ApiResponse ok(List data, long count) {

    return ApiResponse.builder()
        .result(Boolean.TRUE)
        .code(APICodeEnum.SUCCESS.getCode())
        .msg(APICodeEnum.SUCCESS.getMsg())
        .data(data)
        .count(count)
        .build();
  }

  /**
   * 成功并且携带信息
   *
   * @param code
   * @param data
   * @return
   */
  public static ApiResponse ok(APICodeEnum code, Object data) {
    return ApiResponse.builder()
        .result(Boolean.TRUE)
        .code(code.getCode())
        .msg(code.getMsg())
        .data(data)
        .build();
  }

  /**
   * 失败提示
   *
   * @return
   */
  public static ApiResponse fail() {
    return ApiResponse.builder()
        .result(Boolean.FALSE)
        .code(APICodeEnum.FAIL.getCode())
        .msg(APICodeEnum.FAIL.getMsg())
        .build();
  }

  /**
   * 失败提示
   *
   * @return
   */
  public static ApiResponse fail(int code, String msg) {
    return ApiResponse.builder().result(Boolean.FALSE).code(code).msg(msg).build();
  }

  /**
   * 失败携带信息
   *
   * @param data
   * @return
   */
  public static ApiResponse fail(Object data) {
    return ApiResponse.builder()
        .result(Boolean.FALSE)
        .code(APICodeEnum.FAIL.getCode())
        .msg(APICodeEnum.FAIL.getMsg())
        .data(data)
        .build();
  }

  /**
   * 失败携带信息
   *
   * @param data
   * @return
   */
  public static ApiResponse fail(APICodeEnum code, Object data) {

    return ApiResponse.builder()
        .result(Boolean.FALSE)
        .code(code.getCode())
        .msg(code.getMsg())
        .data(data)
        .build();
  }
}
