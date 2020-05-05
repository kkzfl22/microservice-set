package com.liujun.microservice.data.dao;

import com.liujun.microservice.data.entity.DataUserInfoPO;

import java.util.List;

/**
 * 用户信息数据库操作
 *
 * @version 0.0.1
 * @author liujun
 */
public interface DataUserInfoDAO {

  /**
   * 用户信息添加操作
   *
   * @param param 参数信息
   * @return 数据库影响的行数
   */
  int insert(DataUserInfoPO param);


  /**
   * 用户信息添加操作
   *
   * @param param 参数信息
   * @return 数据库影响的行数
   */
  int update(DataUserInfoPO param);


  /**
   * 用户信息添加操作
   *
   * @param param 参数信息
   * @return 数据库影响的行数
   */
  int delete(DataUserInfoPO param);


  /**
   * 用户信息分页查询操作
   *
   * @param param 参数信息
   * @return 数据库查询结果集
   */
  List<DataUserInfoPO> queryPage(DataUserInfoPO param);

  /**
   * 用户信息根据id进行查询操作
   *
   * @param param 主键查询参数信息
   * @return 数据库查询结果集
   */
  DataUserInfoPO queryById(DataUserInfoPO param);
}
