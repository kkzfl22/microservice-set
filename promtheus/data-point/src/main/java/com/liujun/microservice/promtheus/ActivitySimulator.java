package com.liujun.microservice.promtheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

import java.util.concurrent.ThreadLocalRandom;

/**
 * http request total统计总数
 *
 * @author liujun
 * @version 0.0.1
 */
public class ActivitySimulator implements Runnable {

  private SimulatorOpts opts;

  /** 标识当前是否为峰值模式 */
  private boolean spikeMode = false;

  /** 当前任务运行的标识 */
  private volatile boolean shutdown = false;

  /** 统计总数 */
  private final Counter httpRequestTotal =
      Counter.build()
          .name("http_request_total")
          .help("Total number of http requests by response status code")
          .labelNames("endpoint", "status")
          .register();

  /** 用于计算延迟的直方图 */
  private final Histogram httpRequestDurationMs =
      Histogram.build()
          .name("http_request_duration_milliseconds")
          .help("http request latency histogram")
          .exponentialBuckets(25, 2, 7)
          .labelNames("endpoint", "endpoint")
          .register();

  public ActivitySimulator(SimulatorOpts opts) {
    this.opts = opts;
    System.out.println(opts);
  }

  public boolean setSpikeMode(String mode) {
    boolean result = true;

    switch (mode) {
      case "on":
        opts.setSpikeMode(SpikeMode.ON);
        System.out.println("spike mode is set to " + mode);
        break;
      case "off":
        opts.setSpikeMode(SpikeMode.OFF);
        System.out.println("spike mode is set to " + mode);
        break;
      case "random":
        opts.setSpikeMode(SpikeMode.RANDOM);
        System.out.println("spike mode is set to " + mode);
        break;
      default:
        result = false;
        System.out.println("can't recongnize spike mode " + mode);
    }

    return result;
  }

  public void setErrorRate(int rate) {
    if (rate > 100) {
      rate = 100;
    }
    if (rate < 0) {
      rate = 0;
    }
    opts.setErrorRate(rate);
    System.out.println("error rate is set to " + rate);
  }

  /** 模拟数据请求及延迟 */
  public void simulatorActivity() {
    // 获取请求的速度
    int requestRate = this.opts.getRequestRate();
    if (this.giveSpikeMode()) {
      requestRate *= 5 + ThreadLocalRandom.current().nextInt(10);
    }
    int nbRequest = giveWithUncertainty(requestRate, this.opts.getRequestRateUncertainty());
    for (int i = 0; i < nbRequest; i++) {
      String statusCode = this.giveStatusCode();
      String endPoint = this.giveEndpoint();
      // 进行http请求端点的计数
      this.httpRequestTotal.labels(endPoint, statusCode).inc();
      // 随机产生延迟
      int latency = this.giveLatency(statusCode);
      if (this.spikeMode) {
        latency *= 2;
      }
      // observe用于记录延迟的值
      this.httpRequestDurationMs.labels(endPoint, statusCode).observe(latency);
    }
  }

  public int giveWithUncertainty(int base, int append) {
    int randData = ThreadLocalRandom.current().nextInt(base * append / 50) - (base * append / 100);
    return base + randData;
  }

  public boolean giveSpikeMode() {
    switch (this.opts.getSpikeMode()) {
      case ON:
        this.spikeMode = true;
      case OFF:
        this.spikeMode = false;
      case RANDOM:
        int randDs = ThreadLocalRandom.current().nextInt(100);
        if (!this.spikeMode && randDs < this.opts.getSpikeStartChange()) {
          this.spikeMode = true;
        } else if (this.spikeMode && randDs < this.opts.getSpikeEndChance()) {
          this.spikeMode = false;
        }
        break;
    }
    return this.spikeMode;
  }

  private String giveStatusCode() {
    int randValue = ThreadLocalRandom.current().nextInt(100);
    if (randValue < this.opts.getErrorRate()) {
      return "500";
    } else {
      return "200";
    }
  }

  private String giveEndpoint() {
    int rand = ThreadLocalRandom.current().nextInt(this.opts.getEndopints().length);
    return this.opts.getEndopints()[rand];
  }

  private int giveLatency(String statusCode) {
    if (!"200".equals(statusCode)) {
      return 5 + ThreadLocalRandom.current().nextInt(50);
    }
    int p = ThreadLocalRandom.current().nextInt(100);
    if (p < 50) {
      int scopeData =
          this.opts.getLatencyMin()
              + ThreadLocalRandom.current()
                  .nextInt(this.opts.getLatencyP50() - this.opts.getLatencyMin());
      return this.giveWithUncertainty(scopeData, this.opts.getLatencyUncertainty());
    }
    if (p < 90) {
      int scopeData =
          this.opts.getLatencyP50()
              + ThreadLocalRandom.current()
                  .nextInt(this.opts.getLatencyP90() - this.opts.getLatencyP50());
      return this.giveWithUncertainty(scopeData, this.opts.getLatencyUncertainty());
    }
    if (p < 99) {
      int scopeData =
          this.opts.getLatencyP90()
              + ThreadLocalRandom.current()
                  .nextInt(this.opts.getLatencyP99() - this.opts.getLatencyP90());
      return this.giveWithUncertainty(scopeData, this.opts.getLatencyUncertainty());
    }

    int scopeData =
        this.opts.getLatencyP90()
            + ThreadLocalRandom.current()
                .nextInt(this.opts.getLatencyMax() - this.opts.getLatencyP99());
    return this.giveWithUncertainty(scopeData, this.opts.getLatencyUncertainty());
  }

  /** 停止的方法 */
  public void shutdown() {
    shutdown = true;
  }

  @Override
  public void run() {
    while (!shutdown) {
      System.out.println("simulator is running...");
      // 激活进行数据产生
      simulatorActivity();
      // 进行休眠
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
