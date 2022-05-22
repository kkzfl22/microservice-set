package org.example.user.facade;

import org.example.data.UserDataInterface;
import org.example.dto.UserInfoData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liujun
 * @since 2022/5/22
 */
@RestController
public class UserFacade implements UserDataInterface {


  @GetMapping({"/user/detail/msg"})
  @Override
  public List<UserInfoData> list() {
    return UserInfoDataBuilder.builderData(1);
  }

  @PostMapping({"/user/detail/query"})
  @Override
  public List<UserInfoData> update(UserInfoData userInfoData) {
    return UserInfoDataBuilder.builderData(3);
  }
}
