package com.liujun.microservice.config.apolloclient.springxml.xml.bean;

import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 配制的实体数据信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class DataBean {

  private int timeout;

  private int batch;

  private String publidatad;

  @ApolloJsonValue("${jsonValue:[]}")
  private List<JsonBean> jsonValue;

  private String yamlData;

  private static class JsonBean {
    private String key;
    private String value;

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("JsonBean{");
      sb.append("key='").append(key).append('\'');
      sb.append(", value='").append(value).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }
}
