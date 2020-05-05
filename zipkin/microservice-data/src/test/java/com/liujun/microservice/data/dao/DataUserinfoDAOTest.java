package com.liujun.microservice.data.dao;

import com.liujun.microservice.data.TestParent;
import com.liujun.microservice.data.entity.DataUserInfoPO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用户信息表(pap_userinfo)数据库操作单元测试
 *
 * @version 0.0.1
 * @author liujun
 */
public class DataUserinfoDAOTest extends TestParent {

  @Autowired private DataUserInfoDAO instDao;

  private DataUserInfoPO operPo;

  @Before
  public void beforeSetPo() {
    operPo = this.getDataBean();
  }

  public DataUserInfoPO getDataBean() {
    DataUserInfoPO paramBean = new DataUserInfoPO();
    // 用户手机
    paramBean.setUserMobile(RandomStringUtils.randomAlphabetic(4));
    // 用户邮件地址
    paramBean.setUserEmail(RandomStringUtils.randomAlphabetic(18));
    // 账号状态:, 0:停用, 1:正常, 2:密码错误而锁定, 3:管理员强制锁定
    paramBean.setStatus(RandomUtils.nextInt(0, 1 << 2));
    // 用户创建时间
    paramBean.setCreateTime(RandomUtils.nextLong(0, 1 << 5));

    // 用户的id
    paramBean.setOid(RandomStringUtils.randomAlphabetic(11));
    // 用户名
    paramBean.setUserName(RandomStringUtils.randomAlphabetic(11));
    // 用户密码
    paramBean.setUserPassword(RandomStringUtils.randomAlphabetic(16));
    // 用户描述
    paramBean.setRemark(RandomStringUtils.randomAlphabetic(11));
    return paramBean;
  }

  @Test
  public void testInsert() {
    int rs = instDao.insert(operPo);
    Assert.assertEquals(1, rs);
  }

  @Test
  public void testUpdate() {
    int rs = instDao.insert(operPo);
    Assert.assertEquals(1, rs);
    DataUserInfoPO paramPo = operPo;
    // 用户手机
    paramPo.setUserMobile(RandomStringUtils.randomAlphabetic(14));
    // 用户邮件地址
    paramPo.setUserEmail(RandomStringUtils.randomAlphabetic(16));
    // 账号状态:, 0:停用, 1:正常, 2:密码错误而锁定, 3:管理员强制锁定
    paramPo.setStatus(RandomUtils.nextInt(0, 1 << 5));
    // 用户创建时间
    paramPo.setCreateTime(RandomUtils.nextLong(0, 1 << 17));
    // 用户名
    paramPo.setUserName(RandomStringUtils.randomAlphabetic(3));

    // 用户描述
    paramPo.setRemark(RandomStringUtils.randomAlphabetic(7));
    int rsUpd = instDao.update(paramPo);
    Assert.assertEquals(1, rsUpd);
  }

  @Test
  public void testQueryPage() {
    int rs = instDao.insert(operPo);
    Assert.assertEquals(1, rs);

    DataUserInfoPO paramBean = operPo;
    List<DataUserInfoPO> queryRsp = instDao.queryPage(paramBean);
    Assert.assertNotNull(queryRsp);
    DataUserInfoPO rsGetBean = queryRsp.get(0);
    assertQueryRsp(rsGetBean, operPo);
  }

  @Test
  public void testQueryById() {
    int rs = instDao.insert(operPo);
    Assert.assertEquals(1, rs);

    DataUserInfoPO rsGetBean = instDao.queryById(operPo);

    assertQueryRsp(rsGetBean, operPo);
  }

  private void assertQueryRsp(DataUserInfoPO src, DataUserInfoPO target) {
    // 用户手机
    Assert.assertEquals(src.getUserMobile(), target.getUserMobile());
    // 用户邮件地址
    Assert.assertEquals(src.getUserEmail(), target.getUserEmail());
    // 账号状态:, 0:停用, 1:正常, 2:密码错误而锁定, 3:管理员强制锁定
    Assert.assertEquals(src.getStatus(), target.getStatus());
    // 用户创建时间
    Assert.assertEquals(src.getCreateTime(), target.getCreateTime());

    // 用户的id
    Assert.assertEquals(src.getOid(), target.getOid());
    // 用户名
    Assert.assertEquals(src.getUserName(), target.getUserName());

    // 用户描述
    Assert.assertEquals(src.getRemark(), target.getRemark());
  }

  @After
  public void testDelete() {
    DataUserInfoPO paramPo = operPo;
    int rs = instDao.delete(paramPo);
    Assert.assertEquals(1, rs);
  }
}
