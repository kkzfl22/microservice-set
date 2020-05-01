package com.liujun.microservice.demo.cat.filter;

/**
 * 定义cat调用链上下文衔接的标识
 *
 * @author liujun
 * @version 0.0.1
 */
public class CatHttpConstants {

  public static final String CAT_HTTP_HEADER_CHILD_MESSAGE_ID = "X-CAT-CHILD-ID";
  public static final String CAT_HTTP_HEADER_PARENT_MESSAGE_ID = "X-CAT-PARENT-ID";
  public static final String CAT_HTTP_HEADER_ROOT_MESSAGE_ID = "X-CAT-ROOT-ID";
}
