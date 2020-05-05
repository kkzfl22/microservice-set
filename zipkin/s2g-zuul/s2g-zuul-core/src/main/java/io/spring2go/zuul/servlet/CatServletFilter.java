package io.spring2go.zuul.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CatServletFilter implements Filter {

  private String[] urlPatterns = new String[0];

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String patterns = filterConfig.getInitParameter("CatHttpModuleUrlPatterns");
    if (patterns != null) {
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

    // Transaction t = Cat.newTransaction("Service", url);

    try {

      filterChain.doFilter(servletRequest, servletResponse);

    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  @Override
  public void destroy() {}

}
