package com.liujun.micorservice.demo.hystrix.controller;

import com.liujun.micorservice.demo.hystrix.service.StudentServiceDelegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学校的控制类
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/schoolServiceAction")
@Slf4j
public class SchoolServiceController {

  @Autowired private StudentServiceDelegate studentServiceDelegate;

  @RequestMapping(value = "/getSchoolDetail/{schoolName}", method = RequestMethod.GET)
  public String getStudentDetail(@PathVariable String schoolName) {
    log.info("call to invoke getStudent ");
    return studentServiceDelegate.callStudentService(schoolName);
  }
}
