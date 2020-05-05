package io.spring2go.spring;

import brave.servlet.TracingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

/**
 * @author liujun
 * @version 0.0.1
 */
public class TracingServletContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    servletContextEvent
        .getServletContext()
        .addFilter("tracingFilter", TracingFilter.create(TracingOperation.INSTANCE.getTracing()))
        .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    TracingOperation.INSTANCE.close();
  }
}
