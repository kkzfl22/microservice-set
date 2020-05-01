package com.liujun.microservice.zuul.service;

/**
 * @author liujun
 * @version 0.0.1
 */
public class TestJavaInvoke {

  public void run(String item) {
    System.out.println(item);
  }

  public void run(Integer item) {
    System.out.println("inter" + item);
  }

  public void run(double item) {
    System.out.println("double" + item);
  }

  public static void main(String[] args) {
    TestJavaInvoke invoke = new TestJavaInvoke();
    invoke.run(1);
  }
}
