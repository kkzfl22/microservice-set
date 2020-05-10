##使用spring-hystrix进行调用
#1,默认标注，，愉快速失败，无降级函数
@HystrixCommand使用默认的策略，默认的超时间是1秒

#2,对超时时间进行定制，以下超时间为4秒,现测试案例中，使用的时间为3秒，最差也不会超时
@HystrixCommand(commandProperties = {
	@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000") })
    		
#3,对子系统函数进行定制
@HystrixCommand(fallbackMethod = "callStudentService_Fallback")


#4,线程池和隔离策略进行定制,指定降级函数，指定线程池的大小30，指定线程池前面的等待队列大小为10
	@HystrixCommand(fallbackMethod = "callStudentService_Fallback",
			threadPoolKey = "studentServiceThreadPool",
			threadPoolProperties = {
					@HystrixProperty(name="coreSize", value="30"),
					@HystrixProperty(name="maxQueueSize", value="10")
			}
			)

#5，查看本地hystrix数据
hystrix stream:
```
http://localhost:8083/hystrix.stream
```

hystrix dashboard 
```
http://localhost:8083/hystrix



#6,使用turbire进行聚合测试