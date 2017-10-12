package com.asiainfo.ftp.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPClientPool extends GenericObjectPool<FTPClient>
{
    private static Logger logger = LoggerFactory.getLogger(FTPClientPool.class);

    public FTPClientPool(FTPClientPoolConfig poolConfig, PoolableObjectFactory factory)
    {
        super(factory, poolConfig);
    }

    public FTPClient getFTPClient()
    {
        try{
            return borrowObject();
        }catch(Exception e){
            logger.error("连接池中获取FTPClient失败！",e);
        }
        return null;
    }

    public void returnFTPClient(FTPClient resource)
    {
        try {
            returnObject(resource);
        }catch (Exception e) {
            logger.error("FTPClient归还连接池失败！",e);
        }
    }

    public void destroy()
    {
        try{
            close();
        }catch (Exception e) {
            logger.error("连接池销毁失败！",e);
        }
    }

    public int inPoolSize()
    {
        try{
            return getNumIdle();
        }catch(Exception e){
            logger.error("获取连接池大小失败！",e);
            return 0;
        }
    }

    public int borrowSize()
    {
        try{
            return getNumActive();
        }catch(Exception e){
            logger.error("获取连接池连接数失败！",e);
            return 0;
        }
    }
}
