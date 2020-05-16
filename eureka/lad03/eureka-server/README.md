##eureka的服务端
#1，在maven中引入eureka-server的包
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

#2，在springboot的启动主函数中加入启动eureka-server的注解
@EnableEurekaServer

#3，进行集群化的配制
spring:
  application:
    #应用注册到eureka服务的名称,用于进行服务发现
    name: eureka-server
eureka:
  client:
    #是否将自己注册到eureka中
    register-with-eureka: true
    #是否开启同步，开启，在集群部署时，需要集群中复制数据，故需要开启
    fetch-registry: true
  server:
    #eureka进行同步延迟的时间
    wait-time-in-ms-when-sync-empty: 0
    #eureka是否启用自保护模式
    enable-self-preservation: false


#springboot的多配制化注册
---
spring:
  profiles: eureka-server1
server:
  port: 8761
eureka:
  instance:
    hostname: eurekaserver1
    #实例的id,使用ip:应用名：端口号的形式
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
  client:
    service-url:
        #同步的eureka地地址
      default-zone: http://eurekaserver2:9992/eureka


---
spring:
  profiles: eureka-server2
server:
  port: 9992
eureka:
  instance:
    hostname: eurekaserver2
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
  client:
    service-url:
      default-zone: http://eurekaserver1:8761/eureka

#4,进行集群化的启动，在springboot的启动的active-profiles参数中填入配制的profiles的id本例中为eureka-server1和eureka-server2