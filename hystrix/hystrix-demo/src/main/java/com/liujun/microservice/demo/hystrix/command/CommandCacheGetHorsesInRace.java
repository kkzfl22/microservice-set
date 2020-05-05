package com.liujun.microservice.demo.hystrix.command;

import com.liujun.microservice.demo.hystrix.domain.Horse;
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
public class CommandCacheGetHorsesInRace extends HystrixCommand<List<Horse>> {

  private final BettingService bettingService;

  private final String raceCourseId;

  public CommandCacheGetHorsesInRace(BettingService service, String raceCourseId) {
    super(
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BettingServiceGroup"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("BettingServicePool")));
    this.bettingService = service;
    this.raceCourseId = raceCourseId;
  }

  protected List<Horse> run() {
    return bettingService.getHorseInRace(raceCourseId);
  }

  /**
   * 降级函数
   *
   * @return
   */
  @Override
  protected List<Horse> getFallback() {
    return new ArrayList<Horse>(0);
  }

  @Override
  protected String getCacheKey() {
    return raceCourseId;
  }
}
