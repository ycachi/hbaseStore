package com.asiainfo.config;


import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

@Configuration
@ConditionalOnClass({ CustomerDatasourcePropertiesBean.class})
@EnableConfigurationProperties(CustomerDatasourcePropertiesBean.class)
@CustomerMapperScanner(basePackages = {"${autoconfiguration.packagePath}"}, sqlSessionFactoryRef = "businessSqlSessionFactory")
public class CustomerDatasourceAutoconfig {
	/**
	* @author 
	* @className TestAutoconfigBean.java
	* @date 2017年7月3日 上午10:26:31
	* @description 
	*/
	@Autowired
	private CustomerDatasourcePropertiesBean customerDatasourcePropertiesBean;
	
	@Primary
    @Bean(name = "businessDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.business")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "businessTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("businessDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "businessSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("businessDataSource") DataSource dataSource,CustomerDatasourcePropertiesBean customerDatasourcePropertiesBean) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
      //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        //添加插件
        factoryBean.setPlugins(new Interceptor[]{pageHelper});
        
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage(customerDatasourcePropertiesBean.getAliasesPackage());
        try {
        	factoryBean.setMapperLocations(resolver.getResources(customerDatasourcePropertiesBean.getResourcesPath()));
            return factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Primary
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("businessSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
