package com.liujun.microservice.auth2.resource.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liujun
 * @version 0.0.1
 */
@Controller
public class ApiController {

  @RequestMapping("/api/datainfo")
  public ResponseEntity<Map<String, String>> getDataInfo() {
    String username =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Map<String, String> outData = new HashMap<>(2);
    outData.put("username", username);

    return ResponseEntity.ok(outData);
  }
}
