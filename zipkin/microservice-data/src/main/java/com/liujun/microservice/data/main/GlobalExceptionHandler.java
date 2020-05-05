package com.liujun.microservice.data.main;

import com.google.gson.Gson;
import com.liujun.microservice.data.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局的异常处理
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/10/22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /** json信息转换 */
  private Gson gson = new Gson();

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResponseEntity<String> othersErrorHandler(HttpServletRequest req, Exception e) {
    String url = req.getRequestURI();
    logger.error("request error at " + url, e);
    ApiResponse result = ApiResponse.fail();

    //    // 当存在错误信息时，则返回错误信息
    //    if (e.getMessage() != null) {
    //      result.setReturnMsg(e.getMessage());
    //    }

    String errorCode = gson.toJson(result);
    return new ResponseEntity(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
