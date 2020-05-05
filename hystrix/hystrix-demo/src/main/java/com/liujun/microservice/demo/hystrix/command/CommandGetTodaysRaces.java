package com.liujun.microservice.demo.hystrix.command;

import com.liujun.microservice.demo.hystrix.domain.RaceCourse;
import com.liujun.microservice.demo.hystrix.exception.RemoteServiceException;
import com.liujun.microservice.demo.hystrix.service.BettingService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取当前的比赛结果
 *
 * @author liujun
 * @version 0.0.1
 */
public class CommandGetTodaysRaces extends HystrixCommand<List<RaceCourse>> {

  private final BettingService bettingService;

  private final boolean failSilently;

  public CommandGetTodaysRaces(BettingService service, boolean failSilently) {
    super(
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BettingServiceGroup"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("BettingServicePool")));
    this.bettingService = service;
    this.failSilently = failSilently;
  }

  public CommandGetTodaysRaces(BettingService service) {
    this(service, true);
  }

  protected List<RaceCourse> run() {
    return bettingService.getTodaysRaces();
  }

  /**
   * 降级函数
   *
   * @return
   */
  @Override
  protected List<RaceCourse> getFallback() {
    // 如果静态失败的话，则返回一个空对象
    if (failSilently) {
      return new ArrayList<RaceCourse>(0);
    }

    throw new RemoteServiceException("getFallback getTodaysRaces exception");
  }
}
