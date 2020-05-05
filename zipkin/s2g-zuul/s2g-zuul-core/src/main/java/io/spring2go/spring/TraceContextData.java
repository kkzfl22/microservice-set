package io.spring2go.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * 跟踪上下文对象
 *
 * @author liujun
 * @version 0.0.1
 */
public class TraceContextData {

  private Map<String, String> traceMap = new HashMap<>();

  public static TraceContextData getInstance() {
    return new TraceContextData();
  }

  public void putTraceId(String traceId) {
    traceMap.put(TractCfg.TRACE_ID_NAME, traceId);
  }

  public String getTraceId() {
    return traceMap.get(TractCfg.TRACE_ID_NAME);
  }

  public void putSpanId(String spanId) {
    traceMap.put(TractCfg.SPAN_ID_NAME, String.valueOf(spanId));
  }

  public String getSpanId() {
    return traceMap.get(TractCfg.SPAN_ID_NAME);
  }

  public void putParentId(String parentId) {
    traceMap.put(TractCfg.PARENT_SPAN_ID_NAME, String.valueOf(parentId));
  }

  public String getParentId() {
    return traceMap.get(TractCfg.PARENT_SPAN_ID_NAME);
  }
}
