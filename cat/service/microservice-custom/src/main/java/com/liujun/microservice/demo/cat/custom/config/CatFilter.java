package com.liujun.microservice.demo.cat.custom.config;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liujun
 * @version 0.0.1
 */
public class CatFilter implements Filter {

  private String[] urlPatterns = new String[0];

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String patterns = filterConfig.getInitParameter("CatHttpModuleUrlPatterns");
    if (null != patterns) {
      patterns = patterns.trim();
      urlPatterns = patterns.split(",");

      for (int i = 0; i < urlPatterns.length; i++) {
        urlPatterns[i] = urlPatterns[i].trim();
      }
    }
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    String url = request.getRequestURL().toString();

    for (String urlPattern : urlPatterns) {
      if (url.startsWith(urlPattern)) {
        url = urlPattern;
      }
    }

    CatContext catContext = new CatContext();
    catContext.addProperty(
        Cat.Context.ROOT, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID));
    catContext.addProperty(
        Cat.Context.PARENT, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID));
    catContext.addProperty(
        Cat.Context.CHILD, request.getHeader(CatHttpConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID));
    Cat.logRemoteCallServer(catContext);

    Transaction trans = Cat.newTransaction(CatConstants.TYPE_SERVICE, url);
    try {
      Cat.logEvent(
          "Service.method",
          request.getMethod(),
          Message.SUCCESS,
          request.getRequestURL().toString());
      Cat.logEvent("Service.client", request.getRemoteHost());

      filterChain.doFilter(servletRequest, servletResponse);

      trans.setStatus(Transaction.SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      trans.setStatus(e);
      Cat.logError(e);
      throw e;
    } finally {
      trans.complete();
    }
  }
}
