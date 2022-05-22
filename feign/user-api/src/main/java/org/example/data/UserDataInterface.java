package org.example.data;

import org.example.dto.UserInfoData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 使用feign
 *
 * @author liujun
 * @since 2022/5/22
 */
@FeignClient(name = "user-data",url = "http://127.0.0.1:8021/user-biz",contextId = "UserDataInterface")
public interface UserDataInterface {

  @GetMapping("/user/detail/msg")
  List<UserInfoData> list();

  @PostMapping("/user/detail/query")
  List<UserInfoData> update(UserInfoData data);
}
