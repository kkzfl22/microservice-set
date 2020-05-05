package io.spring2go.zuul.core;

import brave.propagation.TraceContext;
import io.spring2go.spring.TraceContextData;
import io.spring2go.spring.TractCfg;
import io.spring2go.zuul.common.ZuulException;
import io.spring2go.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.concurrent.Callable;

public class ZuulCallable implements Callable {

  private static Logger LOGGER = LoggerFactory.getLogger(ZuulCallable.class);

  private AsyncContext ctx;
  private ZuulRunner zuulRunner;

  private HttpServletRequest request;

  public ZuulCallable(
      AsyncContext asyncContext, ZuulRunner zuulRunner, HttpServletRequest request) {
    this.ctx = asyncContext;
    this.zuulRunner = zuulRunner;
    this.request = request;
  }

  @Override
  public Object call() throws Exception {

    RequestContext.getCurrentContext().unset();

    TraceContext traceContext =
        (TraceContext) ctx.getRequest().getAttribute(TraceContext.class.getName());
    //    TraceContextData contextData = TraceContextData.getInstance();
    //    contextData.putTraceId(traceContext.traceIdString());
    //    contextData.putSpanId(traceContext.spanIdString());
    //    contextData.putParentId(traceContext.parentIdString());
    //
    RequestContext zuulContext = RequestContext.getCurrentContext();
    zuulContext.put(TractCfg.TRACE_ID_NAME, traceContext.traceIdString());
    zuulContext.put(TractCfg.SPAN_ID_NAME, traceContext.spanIdString());

    long start = System.currentTimeMillis();
    try {
      service(ctx.getRequest(), ctx.getResponse());

    } catch (Throwable t) {
      LOGGER.error("ZuulCallable execute error.", t);

    } finally {
      try {
        reportStat(zuulContext, start);
      } catch (Throwable t) {
        t.printStackTrace();
      }
      try {
        ctx.complete();
      } catch (Throwable t) {
        t.printStackTrace();
      }
      zuulContext.unset();
    }
    return null;
  }

  private void service(ServletRequest req, ServletResponse res) {
    try {

      init((HttpServletRequest) req, (HttpServletResponse) res);

      // marks this request as having passed through the "Zuul engine", as
      // opposed to servlets
      // explicitly bound in web.xml, for which requests will not have the
      // same data attached
      RequestContext.getCurrentContext().setZuulEngineRan();

      try {
        preRoute();
      } catch (ZuulException e) {
        error(e);
        postRoute();
        return;
      }
      try {
        route();
      } catch (ZuulException e) {
        error(e);
        postRoute();
        return;
      }
      try {
        postRoute();
      } catch (ZuulException e) {
        error(e);
        return;
      }

    } catch (Throwable e) {
      error(new ZuulException(e, 500, "UNHANDLED_EXCEPTION_" + e.getClass().getName()));
    }
  }

  /**
   * executes "post" ZuulFilters
   *
   * @throws ZuulException
   */
  private void postRoute() throws ZuulException {

    try {
      zuulRunner.postRoute();

    } catch (Throwable e) {

      throw e;
    }
  }

  /**
   * executes "route" filters
   *
   * @throws ZuulException
   */
  private void route() throws ZuulException {
    try {
      zuulRunner.route();
    } catch (Throwable e) {
      throw e;
    }
  }

  /**
   * executes "pre" filters
   *
   * @throws ZuulException
   */
  private void preRoute() throws ZuulException {

    try {
      zuulRunner.preRoute();
    } catch (Throwable e) {
      throw e;
    }
  }

  /**
   * initializes request
   *
   * @param servletRequest
   * @param servletResponse
   */
  private void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    zuulRunner.init(servletRequest, servletResponse);
  }

  /**
   * sets error context info and executes "error" filters
   *
   * @param e
   */
  private void error(ZuulException e) {
    try {
      RequestContext.getCurrentContext().setThrowable(e);
      zuulRunner.error();
    } catch (Throwable t) {
    }
  }

  private void reportStat(RequestContext zuulContext, long start) {

    long remoteServiceCost = 0l;
    Object remoteCallCost = zuulContext.get("remoteCallCost");
    if (remoteCallCost != null) {
      try {
        remoteServiceCost = Long.parseLong(remoteCallCost.toString());
      } catch (Exception ignore) {
      }
    }

    long replyClientCost = 0l;
    Object sendResponseCost = zuulContext.get("sendResponseCost");
    if (sendResponseCost != null) {
      try {
        replyClientCost = Long.parseLong(sendResponseCost.toString());
      } catch (Exception ignore) {
      }
    }

    long replyClientReadCost = 0L;
    Object sendResponseReadCost = zuulContext.get("sendResponseCost:read");
    if (sendResponseReadCost != null) {
      try {
        replyClientReadCost = Long.parseLong(sendResponseReadCost.toString());
      } catch (Exception ignore) {
      }
    }

    long replyClientWriteCost = 0L;
    Object sendResponseWriteCost = zuulContext.get("sendResponseCost:write");
    if (sendResponseWriteCost != null) {
      try {
        replyClientWriteCost = Long.parseLong(sendResponseWriteCost.toString());
      } catch (Exception ignore) {
      }
    }

    if (zuulContext.sendZuulResponse()) {
      URL routeUrl = zuulContext.getRouteUrl();
      if (routeUrl == null) {
        LOGGER.warn("Unknown Route: [ {" + zuulContext.getRequest().getRequestURL() + "} ]");
      }
    }

    // TODO report metrics
  }
}
