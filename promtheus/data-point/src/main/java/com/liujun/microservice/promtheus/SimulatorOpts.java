package com.liujun.microservice.promtheus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujun
 * @version 0.0.1
 */
@Configuration
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@ToString
public class SimulatorOpts {

  /** Endpoint ,Weighted map of endpoints to simulate(对外的端点) */
  @Value("${opts.endpoints}")
  private String[] endopints;

  /** RequestRate ,requests per(每) second,每秒的请求 */
  @Value("${opts.request_rate}")
  private int requestRate;

  /** RequestRateUncertainty,Percentage of uncertainty when generating request(+/-)生成请求的不确定性的百分比 */
  @Value("${opts.request_rate_uncertainty}")
  private int requestRateUncertainty;

  /** latency Min in milliseconds 延迟的最小时间毫秒数 */
  @Value("${opts.latency_min}")
  private int latencyMin;

  /** latencyP50 in millisecond 50%的延迟时间 */
  @Value("${opts.latency_p50}")
  private int latencyP50;

  /** latencyP90 in milliseconds 90%的延迟时间 */
  @Value("${opts.latency_p90}")
  private int latencyP90;

  /** latencyP99 in milliseconds 99%的延迟时间 */
  @Value("${opts.latency_p99}")
  private int latencyP99;

  /** latency max in milliseconds 最大的延迟时间 */
  @Value("${opts.latency_max}")
  private int latencyMax;

  /** latencyUncertainty,Percentage of uncertainty when generating latency (+/-)生成延迟不确定性的百分比 */
  @Value("${opts.latency_uncertainty}")
  private int latencyUncertainty;

  /** ErrorRate,Percentage of chance of request causing 500 */
  @Value("${opts.error_rate}")
  private int errorRate;

  /** spikeStartChance,Percentage of chance of entering spike mode */
  @Value("${opts.spike_start_chance}")
  private int spikeStartChange;

  /** spikeStartChance ,percentage of chance of exiting spike mode */
  @Value("${opts.spike_end_chance}")
  private int spikeEndChance;

  /** spikeModeStatus ON/OFF/RANDOM */
  private SpikeMode spikeMode = SpikeMode.OFF;
}
