package io.spring2go.spring;

/**
 * @author liujun
 * @version 0.0.1
 */
public class TractCfg {

  public static final String FLOW_KEY = "data_flow_key";

  /** 128 or 64-bit trace ID lower-hex encoded into 32 or 16 characters (required) */
  public static final String TRACE_ID_NAME = "X-B3-TraceId";
  /** 64-bit span ID lower-hex encoded into 16 characters (required) */
  public static final String SPAN_ID_NAME = "X-B3-SpanId";
  /** 64-bit parent span ID lower-hex encoded into 16 characters (absent on root span) */
  public static final String PARENT_SPAN_ID_NAME = "X-B3-ParentSpanId";

  /** 传递flag */
  public static final String FLAG = "X-B3-Flags";
}
