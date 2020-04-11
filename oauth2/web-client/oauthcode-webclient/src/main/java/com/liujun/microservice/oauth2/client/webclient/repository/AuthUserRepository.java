package com.liujun.microservice.oauth2.client.webclient.repository;

import com.liujun.microservice.oauth2.client.webclient.user.AuthWebclientUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 用户授权的数据库操作
 *
 * @author liujun
 * @version 0.0.1
 */
public interface AuthUserRepository extends CrudRepository<AuthWebclientUser, Long> {

  /**
   * 根据用户名查询用户令牌令牌
   *
   * @param userName
   * @return
   */
  Optional<AuthWebclientUser> findByUserName(String userName);
}
