package com.liujun.gradle.builder.demo;

/**
 *
 *
 * @author liujun
 * @version 0.0.1
 */
public class GradleOutDemo {

  public static void main(String[] args) {
    System.out.println("out test");
    for (int i = 0; i < args.length; i++) {
      System.out.println("curr item :" + i + ",result :" + args[i]);
    }
  }
}