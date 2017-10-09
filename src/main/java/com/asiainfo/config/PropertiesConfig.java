package com.asiainfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
@Configuration
public class PropertiesConfig {
	/**   
	* @Title: PropertiesConfig.java  
	* @Description: 配置常量参数    引用参数方式例如： @Value("#{'${webdriverChromeDriver}'}")private String webdriverChromeDriver;
	* @author    
	* @date 2017年1月17日 下午5:52:17 
	* @version V1.0   
	*/
	@Bean
    public PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() {
        ClassPathResource resource = new ClassPathResource("const.properties");
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(resource);
        return propertyPlaceholderConfigurer;
    }

}
