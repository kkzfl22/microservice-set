package com.liujun.microservice.data;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author
 * @version 0.0.1
 * @date
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class TestParent {


  public static void testRun() {
    Assert.assertThat(10, Matchers.greaterThanOrEqualTo(5));
  }
}
