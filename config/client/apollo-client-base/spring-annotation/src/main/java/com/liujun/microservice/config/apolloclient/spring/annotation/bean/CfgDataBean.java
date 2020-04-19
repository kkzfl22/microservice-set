package com.liujun.microservice.config.apolloclient.spring.annotation.bean;

import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配制的数据信息
 *
 * <p>此测试只能读取配制，不能进行数据的最新获取
 *
 * @author liujun
 * @version 0.0.1
 */
@Component("cfgDataBean")
@Slf4j
public class CfgDataBean {

  private int timeout;

  private int batch;

  private String otherNamespaceData;

  private List<JsonBean> jsonValueList;

  private String yamldata;

  /**
   * 在application命名空间下，创建timeout
   *
   * @param timeout
   */
  @Value("${timeout:10}")
  public void setTimeout(int timeout) {
    log.info("update timeout oldvalue : {} ,newvalue {}", this.timeout, timeout);
    this.timeout = timeout;
  }

  /**
   * 在application命名空间下，创建batch
   *
   * @param batch
   */
  @Value("${batch:20}")
  public void setBatch(int batch) {
    log.info("update batch ,oldvalue: {} , new value {}", this.batch, batch);
    this.batch = batch;
  }

  /**
   * publidata在TEST1.publicnamespace命名空间下设置数据 publidatad=abced
   *
   * @param otherNamespaceData
   */
  @Value("${publidatad:abc}")
  public void setOtherNamespaceData(String otherNamespaceData) {
    log.info(
        "update otherNamespaceData ,oldvalue: {} , new value {}",
        this.otherNamespaceData,
        otherNamespaceData);
    this.otherNamespaceData = otherNamespaceData;
  }

  /**
   * 在application空间下创建jsonValue=[ { "key": 1231, "value": 321 } ]
   *
   * @param jsonValueList
   */
  @ApolloJsonValue("${jsonValue:[]}")
  public void setJsonValueList(List<JsonBean> jsonValueList) {
    log.info("update jsonValue ,old {} , newvalue {}", jsonValueList, jsonValueList);
    this.jsonValueList = jsonValueList;
  }

  /**
   * 在创建私有application的yaml命名空间,值 data: value: outdata: values
   *
   * @param value
   */
  @ApolloJsonValue("${data.value.outdata:abc}")
  public void setYamldata(String value) {
    log.info("value jsonValue ,old {} , newvalue {}", this.yamldata, value);
    this.yamldata = value;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CfgDataBean{");
    sb.append("timeout=").append(timeout);
    sb.append(", batch=").append(batch);
    sb.append(", jsonValueList=").append(jsonValueList);
    sb.append(", yamldata='").append(yamldata).append('\'');
    sb.append(", otherNamespaceData='").append(otherNamespaceData).append('\'');
    sb.append('}');
    return sb.toString();
  }

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
