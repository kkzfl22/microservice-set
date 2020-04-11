package com.liujun.microservice.oauth2.client.webclient.repository;

import com.liujun.microservice.oauth2.client.webclient.TestParent;
import com.liujun.microservice.oauth2.client.webclient.user.AuthWebclientUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * 测试用户数据库操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class TestAuthUserRepository extends TestParent {

  @Autowired private AuthUserRepository userRepository;

  @Test
  public void testFindByUserName() {
    Optional<AuthWebclientUser> auth = userRepository.findByUserName("liujun");
    Assert.assertNotNull(auth.get());
    Assert.assertNotNull(auth.get().getPassword(), "feifei");
  }

  @Test
  public void testSaveOrDelete() {
    AuthWebclientUser user = new AuthWebclientUser();
    user.setUserName("liufei");
    user.setPassword("feifei123");
    AuthWebclientUser addRs = userRepository.save(user);

    userRepository.delete(user);
  }
}
