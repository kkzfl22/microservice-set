实验一、Spring Cloud Eureka/Ribbon高级实验,将Eureka进行本地集群化的方式部署。即在同一台机器上运行两个
======

### 实验步骤

#### 1. 导入项目到IDE
* Eureka服务器[eureka-server](eureka-server)
* Time微服务服务端[time-service](time-service)
* Time微服务客户端[time-client](time-client)



#1，配制本地host文件
127.0.0.1 eurekainstance1
127.0.0.1 eurekainstance2

#2,对本地eureka的服务进行集群化启动，使用spring-boot多环境属性配制

---
spring:
  profiles: eureka-instance1
server:
  port: 8761
eureka:
  instance:
    hostname: eurekainstance1
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
  client:
    service-url:
      default-zone: http://eurekainstance2:8762/eureka


---
spring:
  profiles: eureka-instance2
server:
  port: 8762
eureka:
  instance:
    hostname: eurekainstance2
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
  client:
    service-url:
      default-zone: http://eurekainstance1:8761/eureka
	  
	  
#3,使用spring-boot分别启动eureka-instance1和eureka-instance2
可使用浏览器访问http://localhost:8761/和http://localhost:8762/检查本地服务


#4,对微服务的后端进行改造，同时向两个eureka进行注册
eureka:
  client:
    service-url:
      #eureka的服务的地址
      defaultZone: http://eurekainstance1:8761/eureka,http://eurekainstance2:8762/eureka
    #注册到eureka的服务器中的实例的名称
  instance:
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
	
#5，启动两个spring-boot的服务，可以启动时指定端口server.port=端口，启动4444和5555端口
检查http://localhost:8761/是否已经成功注册了两个服务


#6,对微服务的后端进行改造，使用自定义的负载策略
在配制文件中配制
eureka:
  client:
    service-url:
      #配制eureka的服务端的地址
      defaultZone: http://eurekainstance1:8761/eureka,http://eurekainstance2:8762/eureka
      #无需注册到eureka的服务器中
    register-with-eureka: false
	
代码中自定义策略


import org.springframework.cloud.client.discovery.DiscoveryClient;

/** 使用封装的接口来进行获取 */
  @Autowired private DiscoveryClient discoveryClient;


  /**
   * 通过自定义获取ip列表的方式进行路由
   *
   * @return
   */
  @GetMapping("/eurekaRoute")
  public String getEurekaRoteExecute() {
    String result = "defaultData";

    List<ServiceInstance> listInstances = discoveryClient.getInstances("time-server");

    if (null != listInstances && !listInstances.isEmpty()) {

      int size = listInstances.size();

      int randomIndex = ThreadLocalRandom.current().nextInt(size);

      ServiceInstance oneInstance = listInstances.get(randomIndex);

      URI produceURI =
          URI.create(String.format("http://%s:%s", oneInstance.getHost(), oneInstance.getPort()));

      result = restTemplate.getForObject(produceURI, String.class);
    }

    return result;
  }
  

#7，通过接口进行调用以测试
http://localhost:8083/eurekaRoute





