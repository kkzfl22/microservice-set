## eureka的服务使用集群化方式部署，
#1，需要在本地的hosts文件中配制域名转换
127.0.0.1 eurekainstance1
127.0.0.1 eurekainstance2

#2,在application.yml文件中使用属性文件配制运行时环境，即使用---来进行配制
