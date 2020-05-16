
---
spring:
  profiles: eureka-server1
server:
  port: 8761
eureka:
  instance:
    hostname: eurekaserver1
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
  client:
    service-url:
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

