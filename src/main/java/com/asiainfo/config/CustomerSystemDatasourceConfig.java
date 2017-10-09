package com.asiainfo.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**  
* @Title：MasterMyBatisConfig.java
* @Author:
* @Description:主数据源配置
* @Date：2017年5月24日下午5:47:02  
*/
@Configuration
@MapperScan(basePackages = {"com.asiainfo.base.mapper"}, sqlSessionFactoryRef = "systemSqlSessionFactory")
public class CustomerSystemDatasourceConfig{

    @Bean(name = "systemDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.business")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "systemTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("systemDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "systemSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("systemDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("com.asiainfo.base.entity.system");
        try {
        	factoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
            return factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("systemSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
