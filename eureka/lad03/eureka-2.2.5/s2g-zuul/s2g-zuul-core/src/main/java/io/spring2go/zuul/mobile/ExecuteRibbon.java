package io.spring2go.zuul.mobile;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientException;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpRequest.Builder;
import com.netflix.client.http.HttpRequest.Verb;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import io.spring2go.zuul.common.Constants;
import io.spring2go.zuul.common.ZuulException;
import io.spring2go.zuul.common.ZuulHeaders;
import io.spring2go.zuul.context.RequestContext;
import io.spring2go.zuul.filters.ZuulFilter;
import io.spring2go.zuul.hystrix.*;
import io.spring2go.zuul.util.Debug;
import io.spring2go.zuul.util.HTTPRequestUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ExecuteRibbon extends ZuulFilter {
  private static Logger logger = LoggerFactory.getLogger(ExecuteRibbon.class);

  private static final DynamicStringProperty clientRefresher =
      DynamicPropertyFactory.getInstance().getStringProperty("ribbon.refresh.serviceclient", "");

  static {
    clientRefresher.addCallback(
        new Runnable() {
          @Override
          public void run() {
            RestClientFactory.closeRestClient(clientRefresher.get().trim());
          }
        });
  }

  @Override
  public String filterType() {
    return "route";
  }

  @Override
  public int filterOrder() {
    return 20;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    return ctx.getRouteUrl() != null && ctx.sendZuulResponse();
  }

  @Override
  public Object run() throws ZuulException {

    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    String uri = ctx.getRouteUrl().toString();

    String serviceName = ctx.getServiceName();

    if(serviceName == null)
    {
        serviceName = "data-client";
    }

    String groupName = ctx.getRouteGroup();
    String routeName = ctx.getRouteName();

    if (groupName == null) {
      groupName = Constants.DEFAULT_GROUP;
    }
    if (routeName == null) {
      routeName = Constants.DEFAULT_NAME;
    }

    try {

      int contentLength = request.getContentLength();
      String verb = request.getMethod().toUpperCase();
      Collection<Header> headers =
          buildZuulRequestHeaders(RequestContext.getCurrentContext().getRequest());
      InputStream requestEntity = getRequestBody(RequestContext.getCurrentContext().getRequest());
      IClientConfig clientConfig = buildRequestConfig(serviceName, routeName, groupName);

      RestClient client = RestClientFactory.getRestClient(serviceName, clientConfig);

      HttpResponse response =
          forward(
              client,
              clientConfig,
              verb,
              uri,
              serviceName,
              headers,
              requestEntity,
              contentLength,
              groupName,
              routeName);
      setResponse(response);
    } catch (Exception e) {
      Exception ex = e;
      String errorMsg = "[${ex.class.simpleName}]{${ex.message}}   ";
      Throwable cause = null;
      while ((cause = ex.getCause()) != null) {
        ex = (Exception) cause;
        errorMsg = "${errorMsg}[${ex.class.simpleName}]{${ex.message}}   ";
      }
      ex.printStackTrace();

      logger.error(
          "Service Execution Error,serviceName: {}\nCause: {}\nUri:{}\nrouteName:{}\ngroupName:{}",
          serviceName,
          errorMsg,
          uri,
          routeName,
          groupName,
          e);
      throw new ZuulException(
          errorMsg, 500, ",serviceName: ${serviceName}\nCause: ${errorMsg}\nUri:${uri}");
    }
    return null;
  }

  public HttpResponse forward(
      AbstractLoadBalancerAwareClient client,
      IClientConfig clientConfig,
      String verb,
      String uri,
      String serviceName,
      Collection<Header> headers,
      InputStream entity,
      int contentLength,
      String groupName,
      String routeName)
      throws IOException, URISyntaxException {
    entity = debug(verb, serviceName, headers, entity, contentLength);
    HttpRequest request;
    Builder builder = HttpRequest.newBuilder();

    for (Header header : headers) {
      if (Constants.CAT_PARENT_MESSAGE_ID.equalsIgnoreCase(header.getName())
          || Constants.CAT_CHILD_MESSAGE_ID.equalsIgnoreCase(header.getName())
          || Constants.CAT_ROOT_MESSAGE_ID.equalsIgnoreCase(header.getName())) continue;

      builder.header(header.getName(), header.getValue());
    }
    //		Context ctx = new CatContext();
    //		//Cat.logRemoteCallClient(ctx);
    //		builder.header(Constants.CAT_ROOT_MESSAGE_ID, ctx.getProperty(Cat.Context.ROOT));
    //		builder.header(Constants.CAT_PARENT_MESSAGE_ID, ctx.getProperty(Cat.Context.PARENT));
    //		builder.header(Constants.CAT_CHILD_MESSAGE_ID, ctx.getProperty(Cat.Context.CHILD));

    switch (verb) {
      case "POST":
        builder.verb(Verb.POST);
        request = builder.entity(entity).overrideConfig(clientConfig).uri(new URI(uri)).build();
        break;
      case "PUT":
        builder.verb(Verb.PUT);
        request = builder.entity(entity).overrideConfig(clientConfig).uri(new URI(uri)).build();
        break;
      default:
        builder.verb(getVerb(verb));
        request = builder.entity(entity).overrideConfig(clientConfig).uri(new URI(uri)).build();
    }

    String isolationStrategy =
        DynamicPropertyFactory.getInstance()
            .getStringProperty(routeName + ".isolation.strategy", null)
            .get();
    if (isolationStrategy == null) {
      isolationStrategy =
          DynamicPropertyFactory.getInstance()
              .getStringProperty(groupName + ".isolation.strategy", null)
              .get();
    }
    if (isolationStrategy == null) {
      isolationStrategy =
          DynamicPropertyFactory.getInstance()
              .getStringProperty("zuul.isolation.strategy.global", "SEMAPHORE")
              .get();
    }

    long start = System.currentTimeMillis();
    try {
      if ("THREAD".equalsIgnoreCase(isolationStrategy)) {
        return new RibbonRequestCommandForThreadIsolation(
                client, request, serviceName, groupName, routeName)
            .execute();
      } else {
        return new RibbonRequestCommandForSemaphoreIsolation(
                client, request, serviceName, groupName, routeName)
            .execute();
      }
    } finally {
      RequestContext.getCurrentContext().set("remoteCallCost", System.currentTimeMillis() - start);
    }
  }

  void setResponse(HttpResponse response) throws ClientException, IOException {
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.setResponseStatusCode(response.getStatus());

    boolean isOriginResponseGZipped = false;
    String headerValue;
    Map<String, Collection<String>> map = response.getHeaders();
    for (String headerName : map.keySet()) {

      headerValue = (map.get(headerName).toArray()[0]).toString();
      ctx.addOriginResponseHeader(headerName, headerValue);
      if (headerName.equalsIgnoreCase(ZuulHeaders.CONTENT_LENGTH)) {
        ctx.setOriginContentLength(headerValue);
      }
      if (isValidZuulResponseHeader(headerName)) {
        ctx.addZuulResponseHeader(headerName, headerValue);
      }
      if (headerName.equalsIgnoreCase(ZuulHeaders.CONTENT_ENCODING)) {
        if (HTTPRequestUtil.isGzipped(headerValue)) {
          isOriginResponseGZipped = true;
        }
      }
      if (Debug.debugRequest()) {
        Debug.addRequestDebug("ORIGIN_RESPONSE:: < ${headerName}, ${headerValue}");
      }
    }

    ctx.setResponseGZipped(isOriginResponseGZipped);

    InputStream inputStream = response.getInputStream();
    if (Debug.debugRequest()) {
      if (inputStream == null) {
        Debug.addRequestDebug("ORIGIN_RESPONSE:: < null ");
      } else {
        int avaiLength = inputStream.available();
        byte[] origBytes = new byte[avaiLength];
        inputStream.read(origBytes);
        byte[] contentBytes = origBytes;
        if (isOriginResponseGZipped) {
          ByteArrayInputStream inputStream1 = new ByteArrayInputStream(origBytes);
          new GZIPInputStream(inputStream1).read(contentBytes);
        }
        String entity = new String(contentBytes);
        Debug.addRequestDebug("ORIGIN_RESPONSE:: < ${entity}");

        inputStream = new ByteArrayInputStream(origBytes);
      }
    }
    ctx.setResponseDataStream(inputStream);
  }

  public InputStream debug(
      String verb,
      String url,
      Collection<Header> headers,
      InputStream requestEntity,
      int contentLength)
      throws IOException {
    if (Debug.debugRequest()) {
      RequestContext.getCurrentContext().addZuulResponseHeader("x-target-url", url);
      Debug.addRequestDebug("ZUUL:: url=${url}");
      //            headers.each {
      //                Debug.addRequestDebug("ZUUL::> ${it.name}  ${it.value}");
      //            }
      for (Header head : headers) {
        Debug.addRequestDebug("ZUUL::> " + head.getName() + "  " + head.getValue() + "");
      }
      if (requestEntity != null) {
        requestEntity = debugRequestEntity(requestEntity);
      }
    }
    return requestEntity;
  }

  boolean isValidZuulResponseHeader(String name) {
    switch (name.toLowerCase()) {
      case "connection":
      case "content-length":
      case "content-encoding":
      case "server":
      case "transfer-encoding":
      case "access-control-allow-origin":
      case "access-control-allow-headers":
        return false;
      default:
        return true;
    }
  }

  InputStream debugRequestEntity(InputStream inputStream) throws IOException {
    if (Debug.debugRequestHeadersOnly()) {
      return inputStream;
    }
    if (inputStream == null) {
      return null;
    }
    int availLength = inputStream.available();
    byte[] entityBytes = new byte[availLength];
    inputStream.read(entityBytes);
    String entity = new String(entityBytes);
    Debug.addRequestDebug("ZUUL::> " + entity);
    return new ByteArrayInputStream(entityBytes);
  }

  private Collection<Header> buildZuulRequestHeaders(HttpServletRequest request) {
    Map<String, Header> headersMap = new HashMap<>();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = ((String) headerNames.nextElement()).toLowerCase();
      String value = request.getHeader(name);
      if (isValidZuulRequestHeader(name)) {
        headersMap.put(name, new BasicHeader(name, value));
      }
    }

    Map<String, String> zuulRequestHeaders =
        RequestContext.getCurrentContext().getZuulRequestHeaders();
    for (String key : zuulRequestHeaders.keySet()) {
      String name = key.toLowerCase();
      String value = zuulRequestHeaders.get(key);
      headersMap.put(name, new BasicHeader(name, value));
    }

    if (RequestContext.getCurrentContext().getResponseGZipped()) {
      String name = "accept-encoding";
      String value = "gzip";
      headersMap.put(name, new BasicHeader(name, value));
    }
    return headersMap.values();
  }

  public boolean isValidZuulRequestHeader(String name) {
    if (name.toLowerCase().contains("content-length")) {
      return false;
    }
    if (!RequestContext.getCurrentContext().getResponseGZipped()) {
      if (name.toLowerCase().contains("accept-encoding")) {
        return false;
      }
    }
    return true;
  }

  private InputStream getRequestBody(HttpServletRequest request) {
    InputStream requestEntity = null;
    try {
      requestEntity = request.getInputStream();
    } catch (IOException e) {
      // no requestBody is ok.
    }
    return requestEntity;
  }

  private IClientConfig buildRequestConfig(
      String serviceName, String routeName, String routeGroup) {
    DefaultClientConfigImpl clientConfig = new DefaultClientConfigImpl();
    clientConfig.loadProperties(serviceName);
    clientConfig.setProperty(
        CommonClientConfigKey.NIWSServerListClassName,
        "io.spring2go.zuul.hystrix.DiscoveryServerList");
    clientConfig.setProperty(
        CommonClientConfigKey.ClientClassName, "io.spring2go.zuul.hystrix.RestClient");
    // clientConfig.setProperty(CommonClientConfigKey.NIWSServerListClassName,
    // "com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList");
    clientConfig.setProperty(
        CommonClientConfigKey.NFLoadBalancerRuleClassName,
        ZuulCommandHelper.getRibbonLoadBalanceRule(routeGroup, routeName));
    clientConfig.setProperty(
        CommonClientConfigKey.MaxHttpConnectionsPerHost,
        ZuulCommandHelper.getRibbonMaxHttpConnectionsPerHost(serviceName));
    clientConfig.setProperty(
        CommonClientConfigKey.MaxTotalHttpConnections,
        ZuulCommandHelper.getRibbonMaxTotalHttpConnections(serviceName));
    clientConfig.setProperty(
        CommonClientConfigKey.MaxAutoRetries,
        ZuulCommandHelper.getRibbonMaxAutoRetries(serviceName));
    clientConfig.setProperty(
        CommonClientConfigKey.MaxAutoRetriesNextServer,
        ZuulCommandHelper.getRibbonMaxAutoRetriesNextServer(serviceName));
    clientConfig.setProperty(
        CommonClientConfigKey.ConnectTimeout,
        ZuulCommandHelper.getRibbonConnectTimeout(routeGroup, routeName));
    clientConfig.setProperty(
        CommonClientConfigKey.ReadTimeout,
        ZuulCommandHelper.getRibbonReadTimeout(routeGroup, routeName));
    clientConfig.setProperty(CommonClientConfigKey.RequestSpecificRetryOn, true);

    return clientConfig;
  }

  private Verb getVerb(String verb) {
    switch (verb) {
      case "POST":
        return Verb.POST;
      case "PUT":
        return Verb.PUT;
      case "DELETE":
        return Verb.DELETE;
      case "HEAD":
        return Verb.HEAD;
      case "OPTIONS":
        return Verb.OPTIONS;
      case "GET":
        return Verb.GET;
      default:
        return Verb.GET;
    }
  }
}
