package com.liujun.microservice.demo.cat.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 执行cat的埋点操作,此在进入业务服务之前
 *
 * @author liujun
 * @version 0.0.1
 */
public class CatFilter implements Filter {

  private String[] urlPattern = new String[0];

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String patterns = filterConfig.getInitParameter("CatHttpModuleUrlPatterns");
    if (null != patterns) {
      patterns = patterns.trim();
      urlPattern = patterns.split(",");
      for (int i = 0; i < urlPattern.length; i++) {
        urlPattern[i] = urlPattern[i].trim();
      }
    }
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    String url = request.getRequestURL().toString();
    // 优先进行模式串的匹配操作
    for (String patternItem : urlPattern) {
      if (url.startsWith(patternItem)) {
        url = patternItem;
      }
    }

    CatContext context = new CatContext();
    context.addProperty(
        Cat.Context.ROOT, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID));
    context.addProperty(
        Cat.Context.CHILD, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID));
    context.addProperty(
        Cat.Context.PARENT, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID));

    Transaction tran = Cat.newTransaction(CatConstants.TYPE_URL, url);

    try {
      // 注册事件
      Cat.logEvent(
          "Service.method",
          request.getMethod(),
          Message.SUCCESS,
          request.getRequestURL().toString());
      Cat.logEvent("Service.client", request.getRemoteHost());

      // 过滤链继续调用
      filterChain.doFilter(servletRequest, servletResponse);

      // 将当前调用链标识为成功
      tran.setStatus(Transaction.SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      // 将异常记录到调用链中
      tran.setStatus(e);
      // 记录下错误日志
      Cat.logError(e);
    } finally {
      // 将当前调用链做完成处理
      tran.complete();
    }
  }
}
