package com.liujun.microservice.demo.cat.backoffice.config;

import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 执行调用的拦截操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Service
public class CatResTemplateInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution)
      throws IOException {

    Transaction trans = Cat.newTransaction(CatConstants.TYPE_CALL, httpRequest.getURI().toString());

    try {

      HttpHeaders headers = httpRequest.getHeaders();
      Context ctx = new CatContext();

      Cat.logRemoteCallClient(ctx);
      headers.add(CatHttpConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID, ctx.getProperty(Context.ROOT));
      headers.add(
          CatHttpConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID, ctx.getProperty(Context.PARENT));
      headers.add(
          CatHttpConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID, ctx.getProperty(Context.CHILD));

      // 请求继续被执行
      ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
      trans.setStatus(Transaction.SUCCESS);

      return response;
    } catch (Exception e) {
      e.printStackTrace();
      Cat.getProducer().logError(e);
      trans.setStatus(e);
      throw e;
    } finally {
      trans.complete();
    }
  }
}
