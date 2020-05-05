package com.liujun.microservice.data.main;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(value = {"com.liujun.microservice.**.dao"})
public class DataApplication {

  public static void main(String[] args) {

    SpringApplication.run(DataApplication.class, args);
  }

  @Autowired private DataSource dataSource;

  @Bean
  public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sqlSessionFactoryBean.setMapperLocations(
        resolver.getResources("classpath*:/mapper/**/*Mapper.xml"));

    Interceptor[] plugin = new Interceptor[1];
    plugin[0] = new ZipkinMybatisPlugin();
    sqlSessionFactoryBean.setPlugins(plugin);

    return sqlSessionFactoryBean.getObject();
  }
}
