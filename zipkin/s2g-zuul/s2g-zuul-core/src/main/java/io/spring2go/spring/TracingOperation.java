package io.spring2go.spring;

import brave.Tracing;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.io.IOException;

/**
 * @author liujun
 * @version 0.0.1
 */
public class TracingOperation {

  public static final TracingOperation INSTANCE = new TracingOperation();

  private Sender sender = OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans");

  private AsyncReporter<Span> spanReporter = AsyncReporter.create(sender);

  private Tracing tracing =
      Tracing.newBuilder().localServiceName("s2g-zuul").spanReporter(spanReporter).build();

  public Tracing getTracing() {
    return tracing;
  }

  public void close() {
    try {
      tracing.close(); // disables Tracing.current()
      spanReporter.close(); // stops reporting thread and flushes data
      sender.close(); // closes any transport resources
    } catch (IOException e) {
      // do something real
    }
  }
}
