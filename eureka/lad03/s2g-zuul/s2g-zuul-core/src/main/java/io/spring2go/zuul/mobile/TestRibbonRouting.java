package io.spring2go.zuul.mobile;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import io.spring2go.zuul.common.ZuulException;
import io.spring2go.zuul.context.RequestContext;
import io.spring2go.zuul.filters.ZuulFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class TestRibbonRouting extends ZuulFilter {

  private static Logger logger = LoggerFactory.getLogger(TestRibbonRouting.class);

  private static final AtomicReference<HashMap<String, String>> routingTableRef =
      new AtomicReference<HashMap<String, String>>();

  private static final DynamicStringProperty ROUTING_TABLE_STRING_PROPERTY =
      DynamicPropertyFactory.getInstance().getStringProperty("zuul.routing_table_string", null);

  private DiscoveryEnabledServer getRibbon(String restClientName) {
    Application application =
        DiscoveryManager.getInstance().getDiscoveryClient().getApplication(restClientName);
    if (application == null) {
      throw new RuntimeException(
          "Service-NotFoud" + HttpServletResponse.SC_NOT_FOUND + restClientName + "服务未找到");
    }

    List<DiscoveryEnabledServer> instances = new ArrayList<>();
    for (InstanceInfo info : application.getInstances()) {
      if (info.getStatus() == InstanceInfo.InstanceStatus.UP) {
        instances.add(new DiscoveryEnabledServer(info, false, false));
      }
    }

    int reandIndex = ThreadLocalRandom.current().nextInt(0, instances.size());

    return instances.get(reandIndex);
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    return ctx.sendZuulResponse();
  }

  // sample url
  // http://api.spring2go.com/api/hello
  @Override
  public Object run() throws ZuulException {
    RequestContext ctx = RequestContext.getCurrentContext();
    String path = ctx.getRequest().getRequestURI();
    String requestPath = "";
    String serviceName = "";
    String requestMethod = "";
    int index = path.lastIndexOf("/ribbon/");
    if (index > 0) {
      requestPath = path.substring(index + 8);
      if (StringUtils.isNotEmpty(requestPath)) {
        int index2 = requestPath.indexOf("/");
        if (index2 > 0) {
          serviceName = requestPath.substring(0, index2);
          requestMethod = requestPath.substring(index2 + 1);
        } else {
          serviceName = requestPath;
        }
      }
    }
    if (StringUtils.isNotEmpty(serviceName)) {
      String urlString = serviceName;

      DiscoveryEnabledServer server = this.getRibbon(urlString);

      URL url;
      try {
        String targetUrl =
            "http://" + server.getHost() + ":" + server.getPort() + "/" + requestMethod;
        url = new URL(targetUrl);
        ctx.setRouteUrl(url);
      } catch (MalformedURLException e) {
        throw new ZuulException(e, "Malformed URL exception", 500, "Malformed URL exception");
      }
    }

    if (ctx.getRouteUrl() == null) {
      throw new ZuulException("No route found", 404, "No route found");
    }

    return null;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 19;
  }
}
