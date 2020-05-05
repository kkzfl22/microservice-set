package io.spring2go.zuul.hystrix;

import brave.Span;
import brave.propagation.TraceContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import io.spring2go.spring.TraceContextData;
import io.spring2go.spring.TractCfg;
import io.spring2go.zuul.common.Constants;
import io.spring2go.zuul.context.RequestContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class ZuulRequestCommandForSemaphoreIsolation extends HystrixCommand<HttpResponse> {

  HttpClient httpclient;
  HttpUriRequest httpUriRequest;
  HttpContext httpContext;

  static final char[] HEX_DIGITS = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public ZuulRequestCommandForSemaphoreIsolation(
      HttpClient httpclient,
      HttpUriRequest httpUriRequest,
      String commandGroup,
      String commandKey) {
    this(httpclient, httpUriRequest, null, commandGroup, commandKey);
  }

  public ZuulRequestCommandForSemaphoreIsolation(
      HttpClient httpclient,
      HttpUriRequest httpUriRequest,
      HttpContext httpContext,
      String commandGroup,
      String commandKey) {
    super(
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroup))
            .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
            .andCommandPropertiesDefaults(
                // we want to default to semaphore-isolation since this wraps
                // 2 others commands that are already thread isolated
                HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(
                        HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));

    this.httpclient = httpclient;
    this.httpUriRequest = httpUriRequest;
    this.httpContext = httpContext;
  }

  @Override
  protected HttpResponse run() throws Exception {

    try {
      HttpResponse response = forward();

      return response;
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  HttpResponse forward() throws IOException {

    RequestContext zuulContext = RequestContext.getCurrentContext();

    httpUriRequest.addHeader(
        TractCfg.TRACE_ID_NAME, String.valueOf(zuulContext.get(TractCfg.TRACE_ID_NAME)));

    String spanId = generateId();

    // 调用链传递时此span的id需要新生成一个
    httpUriRequest.addHeader(TractCfg.SPAN_ID_NAME, spanId);

    httpUriRequest.addHeader(
        TractCfg.PARENT_SPAN_ID_NAME, String.valueOf(zuulContext.get(TractCfg.SPAN_ID_NAME)));
    httpUriRequest.addHeader(TractCfg.FLAG, "1");
    httpUriRequest.addHeader("X-B3-Sampled", "1");
    httpUriRequest.addHeader("X-Span-Name", "http:/ui/getData");
    httpUriRequest.addHeader("X-Span-Uri", "/ui/getData");

    System.out.println(
        "data out SPAN_ID_NAME ============"
            + String.valueOf(zuulContext.get(TractCfg.TRACE_ID_NAME)));

    System.out.println("data out  SPAN_ID_NAME ============" + spanId);

    System.out.println(
        "data out  X-B3-ParentSpanId ============" + zuulContext.get(TractCfg.SPAN_ID_NAME));

    return httpclient.execute(httpUriRequest, httpContext);
  }

  private String generateId() {
    return idToHex(ThreadLocalRandom.current().nextLong());
  }

  /**
   * Represents given long id as 16-character lower-hex string
   *
   * @see #traceIdString()
   */
  public static String idToHex(long id) {
    char[] data = new char[16];
    writeHexLong(data, 0, id);
    return new String(data);
  }

  /** Inspired by {@code okio.Buffer.writeLong} */
  static void writeHexLong(char[] data, int pos, long v) {
    writeHexByte(data, pos + 0, (byte) ((v >>> 56L) & 0xff));
    writeHexByte(data, pos + 2, (byte) ((v >>> 48L) & 0xff));
    writeHexByte(data, pos + 4, (byte) ((v >>> 40L) & 0xff));
    writeHexByte(data, pos + 6, (byte) ((v >>> 32L) & 0xff));
    writeHexByte(data, pos + 8, (byte) ((v >>> 24L) & 0xff));
    writeHexByte(data, pos + 10, (byte) ((v >>> 16L) & 0xff));
    writeHexByte(data, pos + 12, (byte) ((v >>> 8L) & 0xff));
    writeHexByte(data, pos + 14, (byte) (v & 0xff));
  }

  static void writeHexByte(char[] data, int pos, byte b) {
    data[pos + 0] = HEX_DIGITS[(b >> 4) & 0xf];
    data[pos + 1] = HEX_DIGITS[b & 0xf];
  }
}
