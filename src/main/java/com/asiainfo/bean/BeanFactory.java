package com.asiainfo.bean;

import com.asiainfo.ftp.pool.FTPClientConfigure;
import com.asiainfo.ftp.pool.FTPClientFactory;
import com.asiainfo.ftp.pool.FTPClientPool;
import com.asiainfo.ftp.pool.FTPClientPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory
{
    @Bean
    public FTPClientPool getFTPClientPool()
    {
        FTPClientPoolConfig poolConfig = new FTPClientPoolConfig();
        poolConfig.minIdle = 1;
        poolConfig.maxIdle = 10;
        poolConfig.maxActive = 8;
        poolConfig.maxWait = 30000;
        FTPClientConfigure clientConfig = new FTPClientConfigure();
        clientConfig.setHost("192.168.1.201");
        clientConfig.setPort(21);
        clientConfig.setUsername("image_ftp");
        clientConfig.setPassword("ftp");
        clientConfig.setEncoding("UTF-8");
        FTPClientFactory factory  = new FTPClientFactory(clientConfig);
        return new FTPClientPool(poolConfig,factory);
    }
}
