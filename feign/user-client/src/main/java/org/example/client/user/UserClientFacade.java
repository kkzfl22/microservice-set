package org.example.client.user;

import org.example.data.UserDataInterface;
import org.example.dto.UserInfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liujun
 * @since 2022/5/22
 */
@RestController
public class UserClientFacade {

  @Autowired
  private UserDataInterface userDataInterface;

  @GetMapping({"/user/client/detail/msg"})
  public List<UserInfoData> list() {
    return userDataInterface.list();
  }

  @PostMapping({"/user/client/detail/query"})
  public List<UserInfoData> update(UserInfoData userInfoData) {
    return userDataInterface.update(userInfoData);
  }
}
