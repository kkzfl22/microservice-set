package com.liujun.microservice.demo.hystrix.exception;

/**
 * 远程调用异常
 *
 * @author liujun
 * @version 0.0.1
 */
public class RemoteServiceException extends RuntimeException {

  public RemoteServiceException(String message) {
    super(message);
  }
}
