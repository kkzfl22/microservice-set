这是一个微服务的调用链监控的案例
s2g-zuul--》microservice-ui--》microservice-backoffice--》microservice-custom        
                                                      --》microservice-userinfo
													  --》microservice-data
													  
microservice-data中集成mybatis的SQL与参数，集成springAOP的调用，及log4j的错误日志调用