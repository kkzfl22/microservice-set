package com.liujun.microservice.demo.cat.userinfo;

import com.liujun.microservice.demo.cat.userinfo.bean.UserInfoData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户信息的微服务模拟
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/userinfo")
public class UserinfoAction {

  @RequestMapping(method = RequestMethod.GET)
  public UserInfoData getUserInfo() {
    UserInfoData userInfo = new UserInfoData();
    userInfo.setUserId("12312");
    userInfo.setUserName("liujun");
    userInfo.setHigh(172);
    userInfo.setSex("男");

    return userInfo;
  }
}
