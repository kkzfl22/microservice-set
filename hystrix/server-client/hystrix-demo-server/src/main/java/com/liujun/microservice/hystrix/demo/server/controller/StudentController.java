package com.liujun.microservice.hystrix.demo.server.controller;

import com.liujun.microservice.hystrix.demo.server.entity.Student;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 学生控制类
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/studentController")
public class StudentController {

  /** map信息 */
  private static final Map<String, List<Student>> DATA_MAP = new HashMap<>();

  private static final int ITEM_SIZE = 3;

  private static final String KEY1 = "school";
  private static final String KEY2 = "student";

  /** random sleep by number */
  private static final int SLEEP_NUM = 3;

  // 数据构建
  static {
    DATA_MAP.put(KEY1, getList());
    DATA_MAP.put(KEY2, getList());
  }

  private static List<Student> getList() {
    List<Student> dataList = new ArrayList<>(ITEM_SIZE);
    for (int j = 0; j < ITEM_SIZE; j++) {
      Student student =
          Student.builder()
              .studentId(ThreadLocalRandom.current().nextInt())
              .name(RandomStringUtils.randomAlphabetic(6))
              .address(RandomStringUtils.randomAlphabetic(21))
              .build();
      dataList.add(student);
    }
    return dataList;
  }

  /**
   * 学生信息
   *
   * @param schoolName 获取的key信息
   * @return 结果
   */
  @RequestMapping(value = "/getStudentBySchool/{schoolName}", method = RequestMethod.GET)
  public List<Student> getStudent(@PathVariable String schoolName) {

    randomSleep();

    if (StringUtils.isAllEmpty(schoolName)) {
      return Collections.EMPTY_LIST;
    }

    List<Student> dataList = DATA_MAP.get(schoolName);

    if (dataList == null) {
      return Collections.EMPTY_LIST;
    }
    return dataList;
  }

  /** random sleep */
  private void randomSleep() {
    // random number of third scope
    int value = ThreadLocalRandom.current().nextInt(SLEEP_NUM) + 1;

    if (value == SLEEP_NUM) {
      sleep();
    }
  }

  private void sleep() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
