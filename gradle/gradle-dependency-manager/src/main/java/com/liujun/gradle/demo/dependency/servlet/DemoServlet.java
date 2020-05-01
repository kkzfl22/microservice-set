package com.liujun.gradle.demo.dependency.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/** serlet操作 */
public class DemoServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(DemoServlet.class);

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String name = req.getParameter("name");
    String data = req.getParameter("data");
    System.out.println("name:" + name + ",data:" + data);

    logger.info("data {} value {}", name, data);
    String outdata = "name:" + name + ",data:" + data;

    OutputStream output = resp.getOutputStream();
    try {
      output.write(outdata.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.flush();
        output.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
