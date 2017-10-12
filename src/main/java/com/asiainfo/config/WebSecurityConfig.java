package com.asiainfo.config;

import com.asiainfo.common.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@EnableConfigurationProperties(SecuritySettings.class)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/**  
	* @Title：WebSecurityConfig.java
	* @Author:
	* @Description:Spring Security权限核心模块
	* @Date：2017年2月15日下午5:47:02  
	*/
	private static final Logger logger = Logger.getLogger(WebSecurityConfig.class);
	@Autowired
    private SecuritySettings settings;
	@Autowired
    private CustomizeUserSecurityConfig customizeUserSecurityConfig;
	@Autowired
	private CustomSuccessHandler customSuccessHandler;
    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        System.out.println("注册自定义userService"+customizeUserSecurityConfig.toString());
    	return customizeUserSecurityConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("使用自定义customizeUserSecurityConfig"+customizeUserSecurityConfig);
        //user Details Service验证
    	auth.userDetailsService(customizeUserSecurityConfig).passwordEncoder(new PasswordEncoder(){

            public String encode(CharSequence rawPassword) {
                return MD5Util.encode((String)rawPassword);
            }

            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode((String)rawPassword));
            }});

    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.headers().frameOptions().disable();
        http
        	.headers().disable()
	        .authorizeRequests()
		    	.anyRequest().authenticated()
		    	.and()
		    .formLogin()
		        .loginPage("/login").successHandler(customSuccessHandler)
		        .permitAll()
		        .and()
		    .logout().logoutUrl("/logout").permitAll()
		    	.logoutSuccessUrl("/login")
		    .and()
                .exceptionHandling().accessDeniedPage(settings.getDeniedpage());;
        logger.info("http规则配置");

    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/syn/**","/store/**", "/assets/**", "/fonts/**", "/images/**", "/**/favicon.ico", "/stylesheets/**", "/javascripts/**","/down/**");
    }
}

