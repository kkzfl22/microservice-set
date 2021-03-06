package com.liujun.microservice.auth2.authcode.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
@Controller
public class DataInfoControl {

  @RequestMapping("/data/getDataInfo")
  public ResponseEntity<Map> getDataInfo() {
    Map<String, String> data = new HashMap<>(3);
    data.put("username", "feifei");
    data.put("userage", "27");
    data.put("hight", "172");
    return ResponseEntity.ok(data);
  }

  @RequestMapping("/data/getUserInfo")
  public ResponseEntity<Map> getUserinfo() {
    Map<String, String> data = new HashMap<>(3);
    data.put("id", "112233");
    data.put("username", "feifei");
    data.put("userage", "27");
    data.put("hight", "172");
    data.put("email", "132342@163.com");
    return ResponseEntity.ok(data);
  }
}
