package com.liujun.microservice.data.action;

import com.liujun.microservice.data.TestWebParentAction;
import com.liujun.microservice.data.action.dto.TestDataUserDTO;
import com.liujun.microservice.data.action.dto.TestUserRspData;
import com.liujun.microservice.data.common.APICodeEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;

import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
public class PapUserActionTest extends TestWebParentAction {

  private String oid;

  @Before
  public void before() {
    this.oid = RandomStringUtils.randomAlphabetic(11);
  }

  @Test
  public void runTest() {
    TestDataUserDTO userDTO = new TestDataUserDTO();
    userDTO.setUserName("username1");
    userDTO.setUserPassword("pwd");
    userDTO.setOid(oid);
    userDTO.setStatus(1);
    userDTO.setCreateTime(System.currentTimeMillis());

    // 将对象装入HttpEntity中
    HttpEntity<TestDataUserDTO> request = new HttpEntity<>(userDTO);
    TestUserRspData result =
        restTemplate.postForObject("/user/insert.action", request, TestUserRspData.class);
    System.out.println(result);
    Assert.assertEquals(result.getResult(), Boolean.TRUE);
    Assert.assertEquals(result.getCode(), APICodeEnum.SUCCESS.getCode());
  }

  @After
  public void delete() throws InterruptedException {
    TestDataUserDTO userDTO = new TestDataUserDTO();
    userDTO.setOid(oid);

    // 将对象装入HttpEntity中
    HttpEntity<TestDataUserDTO> request = new HttpEntity<>(userDTO);
    Map<String, Object> result =
        restTemplate.postForObject("/user/delete.action", request, Map.class);
    System.out.println(result);
    Assert.assertEquals(result.get("result"), Boolean.TRUE);
    Assert.assertEquals(result.get("code"), APICodeEnum.SUCCESS.getCode());

    Thread.sleep(20000);
  }
}
