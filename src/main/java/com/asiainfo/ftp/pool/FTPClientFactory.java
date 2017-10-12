package com.asiainfo.ftp.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FTPClientFactory extends BasePoolableObjectFactory<FTPClient>
{
    private static Logger logger = LoggerFactory.getLogger(FTPClientFactory.class);

    private FTPClientConfigure config;

    public FTPClientFactory(FTPClientConfigure configure)
    {
        this.config = configure;
    }

    @Override
    public FTPClient makeObject() throws Exception
    {

        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(config.getConnectTimeout());
        try {
            ftpClient.connect(config.getHost(), config.getPort());
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer refused connection");
                return null;
            }
            boolean result = ftpClient.login(config.getUsername(), config.getPassword());
            if (!result) {
                throw new Exception("ftpClient登陆失败! userName:" + config.getUsername() + " ; password:" + config.getPassword());
            }
            ftpClient.setFileType(config.getTransferFileType());
            ftpClient.setBufferSize(config.getBufferSize());
            ftpClient.setControlEncoding(config.getEncoding());
            ftpClient.setControlKeepAliveTimeout(config.getKeepAliveTimeOut());
            ftpClient.setControlKeepAliveReplyTimeout(config.getKeepAliveReplayTimeOut());
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();
            }
        } catch (Exception e)
        {
            logger.error("创建ftpClient失败!",e);
        }
        return ftpClient;


    }

    @Override
    public void destroyObject(FTPClient ftpClient) throws Exception
    {
        if(ftpClient != null)
        {
            try
            {
                if (ftpClient.isConnected())
                {
                    ftpClient.logout();
                }
            } catch (IOException e) {
                logger.error("销毁ftpClient失败!",e);
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e)
                {
                    logger.error("销毁ftpClient失败!",e);
                }
            }
        }

    }
    @Override
    public boolean validateObject(FTPClient ftpClient)
    {
        if(ftpClient != null){
            try{
                return (ftpClient.isConnected()&&ftpClient.sendNoOp());
            }catch(Exception e){
                logger.error("验证ftpClient连接有效性异常!",e);
            }
        }
        return false;
    }
}
