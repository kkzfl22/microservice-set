package org.example.user.facade;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dto.UserInfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 测试数据的构建
 *
 * @author liujun
 * @since 2022/5/21
 */
public class UserInfoDataBuilder {

  public static List<UserInfoData> builderData(int size) {
    List<UserInfoData> dataList = new ArrayList<>(size + 2);
    for (int i = 0; i < size; i++) {
      dataList.add(builderData());
    }
    return dataList;
  }

  private static UserInfoData builderData() {
    UserInfoData target = new UserInfoData();

    target.setId(UUID.randomUUID().toString());
    target.setUserName(RandomStringUtils.randomAlphabetic(20));
    target.setPwd(RandomStringUtils.randomAlphabetic(64));
    target.setFullName(RandomStringUtils.randomAlphabetic(20));
    target.setPhone("13" + RandomStringUtils.randomNumeric(9));
    target.setEmail(RandomStringUtils.randomAlphabetic(10) + "@test.com");
    target.setCardId(RandomStringUtils.randomAlphanumeric(18));
    target.setAddress(RandomStringUtils.randomAlphabetic(128));
    target.setCity(RandomStringUtils.randomAlphabetic(10));
    target.setCreateTime(System.nanoTime());

    return target;
  }
}
