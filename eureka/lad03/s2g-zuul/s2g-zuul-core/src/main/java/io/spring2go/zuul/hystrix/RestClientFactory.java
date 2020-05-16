package io.spring2go.zuul.hystrix;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Lists;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import io.spring2go.zuul.common.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import io.spring2go.zuul.util.SleepUtil;

import javax.servlet.http.HttpServletResponse;

public class RestClientFactory {
  private static Logger logger = LoggerFactory.getLogger(RestClientFactory.class);

  private static final ConcurrentMap<String, RestClient> restClientMap = Maps.newConcurrentMap();

  public static RestClient getRestClient(String serviceName, IClientConfig clientConfig)
      throws ClientException {
    RestClient oldClient = restClientMap.get(serviceName);

    if (oldClient == null) {
      synchronized (RestClient.class) {
        oldClient = restClientMap.get(serviceName);
        if (oldClient == null) {
          RestClient client = getRestClientOper(serviceName, clientConfig);
          //          RestClient client = newRestClient(serviceName, clientConfig);
          oldClient = restClientMap.putIfAbsent(serviceName, client);

          if (oldClient != null) {
            oldClient.shutdown();
          }
          oldClient = client;
        }
      }
    }

    return oldClient;
  }

  public static void closeRestClient(String serviceName) {
    RestClient oldClient = restClientMap.remove(serviceName);
    if (oldClient != null) {
      SleepUtil.sleep(30 * 1000);
      oldClient.shutdown();
    }
  }

  private static RestClient getRestClientOper(String restClientName, IClientConfig clientConfig)
      throws ClientException {

    RestClient client = newRestClient(restClientName, clientConfig);

    Application application =
			  DiscoveryManager.getInstance().getDiscoveryClient().getApplication(restClientName);
	  if (application == null) {
		  throw new RuntimeException(
				  "Service-NotFoud" + HttpServletResponse.SC_NOT_FOUND + restClientName + "服务未找到");
	  }

	  List<DiscoveryEnabledServer> instances = Lists.newArrayList();
	  for (InstanceInfo info : application.getInstances()) {
		  if (info.getStatus() == InstanceInfo.InstanceStatus.UP) {
			  instances.add(new DiscoveryEnabledServer(info, false, false));
		  }
	  }

    ZoneAwareLoadBalancer loadbalancer = (ZoneAwareLoadBalancer) client.getLoadBalancer();

    loadbalancer.setServersList(instances);
    IRule rule = new RandomRule();

    loadbalancer.setRule(rule);
    client.setLoadBalancer(loadbalancer);
    return client;
  }

  private static RestClient newRestClient(String restClientName, IClientConfig clientConfig)
      throws ClientException {
    RestClient restClient = new RestClient(clientConfig);
    ILoadBalancer loadBalancer = null;
    boolean initializeNFLoadBalancer =
        Boolean.parseBoolean(
            clientConfig
                .getProperty(
                    CommonClientConfigKey.InitializeNFLoadBalancer,
                    DefaultClientConfigImpl.DEFAULT_ENABLE_LOADBALANCER)
                .toString());
    if (initializeNFLoadBalancer) {
      loadBalancer = newLoadBalancerFromConfig(restClientName, clientConfig);
    }
    if (restClient instanceof AbstractLoadBalancerAwareClient) {
      ((AbstractLoadBalancerAwareClient) restClient).setLoadBalancer(loadBalancer);
    }
    return restClient;
  }

  private static ILoadBalancer newLoadBalancerFromConfig(
      String restClientName, IClientConfig clientConfig) throws ClientException {
    ILoadBalancer lb = null;
    try {
      String loadBalancerClassName =
          (String) clientConfig.getProperty(CommonClientConfigKey.NFLoadBalancerClassName);
      lb =
          (ILoadBalancer)
              ClientFactory.instantiateInstanceWithClientConfig(
                  loadBalancerClassName, clientConfig);

      logger.info("Client:" + restClientName + " instantiated a LoadBalancer:" + lb.toString());
      return lb;
    } catch (Exception e) {
      throw new ClientException(
          "Unable to instantiate/associate LoadBalancer with Client:" + restClientName, e);
    }
  }
}
