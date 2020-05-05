package com.liujun.microservice.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.liujun.microservice.data.dao"})
public class DataApplication {

  public static void main(String[] args) throws InterruptedException {

    SpringApplication.run(DataApplication.class, args);
  }

  // @Autowired private DataSource dataSource;

  //  @Bean
  //  public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
  //
  //    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
  //    sqlSessionFactoryBean.setDataSource(dataSource);
  //    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
  //    sqlSessionFactoryBean.setMapperLocations(
  //        resolver.getResources("classpath*:/mapper/**/*Mapper.xml"));
  //
  //    Interceptor[] plugin = new Interceptor[1];
  //    plugin[0] = new CatMybatisPlugin();
  //    sqlSessionFactoryBean.setPlugins(plugin);
  //
  //    return sqlSessionFactoryBean.getObject();
  //  }
}
