package io.spring2go.zuul.servlet;

import brave.SpanCustomizer;
import brave.propagation.TraceContext;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import io.spring2go.spring.TraceContextData;
import io.spring2go.zuul.common.Constants;
import io.spring2go.zuul.core.ZuulCallable;
import io.spring2go.zuul.core.ZuulRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncZuulServlet extends HttpServlet {
  private static final long serialVersionUID = 2723461074152665339L;

  private static Logger LOGGER = LoggerFactory.getLogger(AsyncZuulServlet.class);

  private DynamicIntProperty asyncTimeout =
      DynamicPropertyFactory.getInstance()
          .getIntProperty(Constants.ZUUL_SERVLET_ASYNC_TIMEOUT, 20000);
  private DynamicIntProperty coreSize =
      DynamicPropertyFactory.getInstance().getIntProperty(Constants.ZUUL_THREADPOOL_CODE_SIZE, 200);
  private DynamicIntProperty maximumSize =
      DynamicPropertyFactory.getInstance().getIntProperty(Constants.ZUUL_THREADPOOL_MAX_SIZE, 2000);
  private DynamicLongProperty aliveTime =
      DynamicPropertyFactory.getInstance()
          .getLongProperty(Constants.ZUUL_THREADPOOL_ALIVE_TIME, 1000 * 60 * 5);

  private ZuulRunner zuulRunner = new ZuulRunner();
  private AtomicReference<ThreadPoolExecutor> poolExecutorRef =
      new AtomicReference<ThreadPoolExecutor>();
  private AtomicLong rejectedRequests = new AtomicLong(0);

  @Override
  public void init() throws ServletException {
    reNewThreadPool();
    Runnable c =
        new Runnable() {
          @Override
          public void run() {
            ThreadPoolExecutor p = poolExecutorRef.get();
            p.setCorePoolSize(coreSize.get());
            p.setMaximumPoolSize(maximumSize.get());
            p.setKeepAliveTime(aliveTime.get(), TimeUnit.MILLISECONDS);
          }
        };

    coreSize.addCallback(c);
    maximumSize.addCallback(c);
    aliveTime.addCallback(c);

    // TODO metrics reporting
  }

  private void reNewThreadPool() {
    ThreadPoolExecutor poolExecutor =
        new ThreadPoolExecutor(
            coreSize.get(),
            maximumSize.get(),
            aliveTime.get(),
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<Runnable>());
    ThreadPoolExecutor old = poolExecutorRef.getAndSet(poolExecutor);
    if (old != null) {
      shutdownPoolExecutor(old);
    }
  }

  private void shutdownPoolExecutor(ThreadPoolExecutor old) {
    try {
      old.awaitTermination(5, TimeUnit.MINUTES);
      old.shutdown();
    } catch (InterruptedException e) {
      old.shutdownNow();
      LOGGER.error("Shutdown Zuul Thread Pool:", e);
    }
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    SpanCustomizer customizer = (SpanCustomizer) req.getAttribute(SpanCustomizer.class.getName());
    if (customizer != null) {
      customizer.tag("flow", "service start ");
    }

    req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
    AsyncContext asyncContext = req.startAsync();
    asyncContext.setTimeout(asyncTimeout.get());
    asyncContext.addListener(new AsyncZuulListener());
    try {
      poolExecutorRef.get().submit(new ZuulCallable(asyncContext, zuulRunner, req));
    } catch (RuntimeException e) {
      rejectedRequests.incrementAndGet();
      throw e;
    }
  }

  @Override
  public void destroy() {
    shutdownPoolExecutor(poolExecutorRef.get());
  }
}
