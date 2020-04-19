package com.liujun.microservice.oauth2.join.weibo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.weibo.api.Weibo;
import org.springframework.social.weibo.api.WeiboProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 接入的控制操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Controller
public class JoinController {

  /** github */
  @Autowired private Weibo weibo;

  /** 连接令牌 */
  @Autowired private ConnectionRepository connectionRepository;

  /** 加载出所有的项目列表 */
  @GetMapping
  public String repositories(Model model) {
    // 检查是否已经存在连接
    if (connectionRepository.findPrimaryConnection(Weibo.class) == null) {
      return "redirect:/connect/weibo";
    }

    WeiboProfile profile = weibo.userOperations().getUserProfileById(1405268701);

    // 将令牌添加attribute中
    model.addAttribute("userinfo", profile);

    return "repositories";
  }
}
