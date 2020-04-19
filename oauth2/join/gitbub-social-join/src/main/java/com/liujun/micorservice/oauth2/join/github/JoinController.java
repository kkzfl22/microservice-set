package com.liujun.micorservice.oauth2.join.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

/**
 * 接入的控制操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Controller
public class JoinController {

  /** github */
  @Autowired private GitHub github;

  /** 连接令牌 */
  @Autowired private ConnectionRepository connectionRepository;

  /** 加载出所有的项目列表 */
  @GetMapping
  public String repositories(Model model) {
    // 检查是否已经存在连接
    if (connectionRepository.findPrimaryConnection(GitHub.class) == null) {
      return "redirect:/connect/github";
    }

    String name = github.userOperations().getUserProfile().getName();
    String userName = github.userOperations().getUserProfile().getUsername();

    // 将令牌添加attribute中
    model.addAttribute("name", name);

    String uri = "https://api.github.com/users/{user}/repos";
    GitHubRepo[] repos = github.restOperations().getForObject(uri, GitHubRepo[].class, userName);
    model.addAttribute("repositories", Arrays.asList(repos));

    return "repositories";
  }
}
