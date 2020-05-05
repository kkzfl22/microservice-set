package com.liujun.microservice.demo.hystrix.service;

import com.liujun.microservice.demo.hystrix.command.CommandCacheGetHorsesInRace;
import com.liujun.microservice.demo.hystrix.command.CommandGetTodaysRaces;
import com.liujun.microservice.demo.hystrix.domain.Horse;
import com.liujun.microservice.demo.hystrix.domain.RaceCourse;
import com.liujun.microservice.demo.hystrix.exception.RemoteServiceException;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixContextRunnable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

/**
 * 进行hystrix的基本用法
 *
 * <p>测试跑马的服务
 *
 * <p>使用hystrix调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class BettingServiceTest {

  /** 跑马赛信息 */
  private static final String RACE_1 = "course_france";

  /** 白马信息 */
  private static final String HORSE_1 = "horse_white";

  /** 黑马信息 */
  private static final String HORSE_2 = "horse_black";

  /** 比赛胜率 */
  private static final String ODDS_RACE_1_HORSE_1 = "10/1";

  /** 胜率 */
  private static final String ODDS_RACE_1_HORSE_2 = "100/1";

  private static final HystrixCommandKey GETTER_KEY =
      HystrixCommandKey.Factory.asKey("GetterCommand");

  private BettingService mockService;

  /** 使用mock进行模拟调用 */
  @Before
  public void setUp() {
    // 模拟生成对象
    mockService = mock(BettingService.class);
    // 当调用getTodaysRaces时，返回一个集合对象
    when(mockService.getTodaysRaces()).thenReturn(getRaceCourses());
    // 当调用getHorseInRace()，返回一个
    when(mockService.getHorseInRace(RACE_1)).thenReturn(getHorsesAtFrance());
    // 当调用获取某比赛的结果
    when(mockService.getOddsForHorse(RACE_1, HORSE_1)).thenReturn(ODDS_RACE_1_HORSE_1);
    when(mockService.getOddsForHorse(RACE_1, HORSE_2)).thenReturn(ODDS_RACE_1_HORSE_2);
  }

  private List<RaceCourse> getRaceCourses() {
    RaceCourse course1 = new RaceCourse(RACE_1, "France");
    return Arrays.asList(course1);
  }

  private List<Horse> getHorsesAtFrance() {
    Horse horse1 = new Horse(HORSE_1, "White");
    Horse horse2 = new Horse(HORSE_2, "Black");
    return Arrays.asList(horse1, horse2);
  }

  /** 测试同步调用并且成功 */
  @Test
  public void testSynchronous() {
    CommandGetTodaysRaces commandGetTodaysRaces = new CommandGetTodaysRaces(mockService);
    Assert.assertEquals(getRaceCourses(), commandGetTodaysRaces.execute());

    // 验证是否被调用getTodaysRaces方法
    verify(mockService).getTodaysRaces();
    // 冗余调用检查,检查是否存在未被验证的行为
    verifyNoMoreInteractions(mockService);
  }

  /** 同步调用并且是安静的失败 */
  @Test
  public void testSynchronousFailSilently() {
    CommandGetTodaysRaces commandGetTodaysRaces = new CommandGetTodaysRaces(mockService);
    when(mockService.getTodaysRaces()).thenThrow(new RuntimeException("Error!!"));
    Assert.assertEquals(new ArrayList<RaceCourse>(0), commandGetTodaysRaces.execute());

    // 检查方法是否被调用
    verify(mockService).getTodaysRaces();
    // 冗余调用检查
    verifyNoMoreInteractions(mockService);
  }

  /** 同步调用并且快速失败 */
  @Test
  public void testSynchronousFailFast() {
    CommandGetTodaysRaces commandGetTodaysRaces = new CommandGetTodaysRaces(mockService, false);
    when(mockService.getTodaysRaces()).thenThrow(new RuntimeException("error!!"));

    try {
      commandGetTodaysRaces.execute();
    } catch (HystrixRuntimeException e) {
      e.printStackTrace();
      Assert.assertEquals(RemoteServiceException.class, e.getFallbackException().getClass());
    }

    verify(mockService).getTodaysRaces();
    verifyNoMoreInteractions(mockService);
  }

  /**
   * 以异常方式的调用
   *
   * <p>异步会加入到队列中执行
   */
  @Test
  public void testAsynchronous() throws ExecutionException, InterruptedException {

    CommandGetTodaysRaces commandGetTodaysRaces = new CommandGetTodaysRaces(mockService);
    Future<List<RaceCourse>> futureList = commandGetTodaysRaces.queue();

    // 执行异步调用
    Assert.assertEquals(getRaceCourses(), futureList.get());

    // 检查执行结果
    verify(mockService).getTodaysRaces();
    verifyNoMoreInteractions(mockService);
  }

  /** 以响应方式的调用 */
  @Test
  public void testObservable() {
    CommandGetTodaysRaces commandGetTodaysRaces = new CommandGetTodaysRaces(mockService);
    Observable<List<RaceCourse>> observable = commandGetTodaysRaces.observe();
    // 结果检查
    Assert.assertEquals(getRaceCourses(), observable.toBlocking().single());
    verify(mockService).getTodaysRaces();
    verifyNoMoreInteractions(mockService);
  }

  /** 带缓存的调用 */
  @Test
  public void testCacheCommand() {
    HystrixRequestContext context = HystrixRequestContext.initializeContext();

    try {
      // 声明带缓存的对象
      CommandCacheGetHorsesInRace cacheFirst = new CommandCacheGetHorsesInRace(mockService, RACE_1);
      CommandCacheGetHorsesInRace cacheSecond =
          new CommandCacheGetHorsesInRace(mockService, RACE_1);

      // 第一次调用
      cacheFirst.execute();

      // 检查缓存标识，当前数据还未缓存
      Assert.assertEquals(false, cacheFirst.isResponseFromCache());

      // 检查方法是否被调用
      verify(mockService).getHorseInRace(RACE_1);

      // 对象2调用
      cacheSecond.execute();

      // 检查对象是否被缓存
      Assert.assertEquals(true, cacheSecond.isResponseFromCache());
    } finally {
      context.shutdown();
    }

    // 再次启动上下文对象
    context = HystrixRequestContext.initializeContext();

    try {
      CommandCacheGetHorsesInRace cacheThird = new CommandCacheGetHorsesInRace(mockService, RACE_1);
      cacheThird.execute();

      Assert.assertEquals(false, cacheThird.isResponseFromCache());

      // 调用清空缓存数据
      HystrixRequestCache.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance())
          .clear(RACE_1);
    } finally {
      context.shutdown();
    }
  }
}
