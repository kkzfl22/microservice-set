package com.liujun.microservice.data.main;

import com.liujun.microservice.data.common.ApiResponse;
import com.liujun.microservice.data.dao.DataUserInfoDAO;
import com.liujun.microservice.data.entity.DataUserInfoPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户类型操作
 *
 * @author liujun
 * @version 0.0.1
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class DataUserAction {

  @Autowired private DataUserInfoDAO userApplicationService;

  /**
   * 数据添加操作
   *
   * @param userDto
   * @return
   */
  @RequestMapping(value = "/insert.action")
  public ApiResponse insert(@RequestBody DataUserInfoPO userDto, HttpServletRequest request) {

    int rsp = 0;

    rsp = userApplicationService.insert(userDto);

    if (rsp > 0) {
      return ApiResponse.ok();
    } else {
      return ApiResponse.fail();
    }
  }

  /**
   * 数据添加操作
   *
   * @param userDto
   * @return
   */
  @RequestMapping(value = "/update.action", method = RequestMethod.POST)
  public ApiResponse update(@RequestBody DataUserInfoPO userDto, HttpServletRequest request) {

    // 进行数据的更新操作
    int rsp = userApplicationService.update(userDto);
    if (rsp >= 0) {
      return ApiResponse.ok();
    } else {
      return ApiResponse.fail();
    }
  }

  /**
   * 数据添加操作
   *
   * @param userDto
   * @return
   */
  @RequestMapping(value = "/delete.action", method = RequestMethod.POST)
  public ApiResponse delete(@RequestBody DataUserInfoPO userDto, HttpServletRequest request) {

    // 进行数据的更新操作
    int rsp = userApplicationService.delete(userDto);
    if (rsp >= 0) {
      return ApiResponse.ok();
    } else {
      return ApiResponse.fail();
    }
  }

  /**
   * 数据添加操作
   *
   * @param userDto
   * @return
   */
  @RequestMapping(value = "/queryPage.action", method = RequestMethod.POST)
  public ApiResponse quereyPage(@RequestBody DataUserInfoPO userDto, HttpServletRequest request) {

    //    // 进行分页准备
    //    Page<PageInfo> pageInfo = PageHelper.startPage(userDto.getPage(), userDto.getPageSize());
    // 进行数据的更新操作
    List<DataUserInfoPO> pageData = userApplicationService.queryPage(userDto);

    return ApiResponse.ok(pageData, pageData.size());
  }

  /**
   * 数据添加操作
   *
   * @param userDto
   * @return
   */
  @RequestMapping(value = "/detail.action", method = RequestMethod.POST)
  public ApiResponse detail(@RequestBody DataUserInfoPO userDto, HttpServletRequest request) {

    // 进行数据的更新操作
    DataUserInfoPO pageData = userApplicationService.queryById(userDto);

    if (null != pageData) {
      return ApiResponse.ok(pageData);
    } else {
      return ApiResponse.ok(new Object());
    }
  }
}
