package com.liujun.microservice.demo.hystrix.service;

import com.liujun.microservice.demo.hystrix.domain.Horse;
import com.liujun.microservice.demo.hystrix.domain.RaceCourse;

import java.util.List;

/**
 * 跑马大赛的服务,模拟跑马服务
 *
 * @author liujun
 * @version 0.0.1
 */
public interface BettingService {

  /**
   * 获取当天的跑马赛信息
   *
   * @return
   */
  List<RaceCourse> getTodaysRaces();

  /**
   * 获取某场跑马赛中的马
   *
   * @param raceCourseId
   * @return
   */
  List<Horse> getHorseInRace(String raceCourseId);

  /**
   * 获取某马获胜的概率
   *
   * @param raceCourseId 某场比赛
   * @param horseId 某马信息
   * @return
   */
  String getOddsForHorse(String raceCourseId, String horseId);
}
