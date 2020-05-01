package com.liujun.microservice.demo.cat.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import com.dianping.cat.Cat.Context;

import java.io.IOException;

/**
 * 此用于在resttemplate发送数据时的拦截设置操作
 *
 * @author liujun
 * @version 0.0.1
 */
@Service
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution)
      throws IOException {
    Transaction tran = Cat.newTransaction(CatConstants.TYPE_CALL, httpRequest.getURI().toString());

    try {
      HttpHeaders header = httpRequest.getHeaders();
      // 保存和传递cat的调用链的上下文
      Context ctx = new CatContext();
      Cat.logRemoteCallClient(ctx);
      header.add(CatHttpConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID, ctx.getProperty(Context.ROOT));
      header.add(
          CatHttpConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID, ctx.getProperty(Context.PARENT));
      header.add(CatHttpConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID, ctx.getProperty(Context.CHILD));

      // 保证请求被继续执行
      ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);

      tran.setStatus(Transaction.SUCCESS);

      return response;
    } catch (Exception e) {
      e.printStackTrace();
      Cat.getProducer().logError(e);
      tran.setStatus(e);
      throw e;
    } finally {
      tran.complete();
    }
  }
}
