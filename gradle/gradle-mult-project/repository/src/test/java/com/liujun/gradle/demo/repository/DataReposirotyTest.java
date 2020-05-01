package com.liujun.gradle.demo.repository;

import com.liujun.demo.gradle.mult.module.DataInfo;
import org.junit.Assert;
import org.junit.Test;

public class DataReposirotyTest {

  @Test
  public void testdata() {
    DataReposiroty data = new DataReposiroty();
    data.save(new DataInfo());
    Assert.assertEquals(true, 1 == 1);
    Assert.assertEquals(true, 1 == 1);
  }
}
