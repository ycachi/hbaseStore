package com.asiainfo.ftp.client;

import com.asiainfo.ftp.pool.FTPClientConfigure;
import com.asiainfo.ftp.pool.FTPClientFactory;
import com.asiainfo.ftp.pool.FTPClientPool;
import com.asiainfo.ftp.pool.FTPClientPoolConfig;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FTPTest
{
    public static void main(String[] args) throws IOException {
        FTPClientPoolConfig poolConfig = new FTPClientPoolConfig();
        poolConfig.minIdle = 1;
        poolConfig.maxIdle = 10;
        poolConfig.maxActive = 8;
        poolConfig.maxWait = 30000;
        FTPClientConfigure clientConfig = new FTPClientConfigure();
        clientConfig.setHost("192.168.1.201");
        clientConfig.setPort(21);
        clientConfig.setUsername("image_ftp");
//        clientConfig.setPassiveMode(true);
        clientConfig.setPassword("ftp");
        clientConfig.setEncoding("UTF-8");
        FTPClientFactory factory  = new FTPClientFactory(clientConfig);
        FTPClientPool pool = new FTPClientPool(poolConfig,factory);

        FTPClient ftp = pool.getFTPClient();

//
        byte[] by = FtpUtil.readFile(ftp,"/home/image_ftp","B.jpg");
        if(by  != null && by.length > 0)
        {
            System.out.println(by);
        }


        by = FtpUtil.readFile(ftp,"/home/image_ftp","C.jpg");
        if(by  != null && by.length > 0)
        {
            System.out.println(by);
        }
//
//        ftp.changeWorkingDirectory("/home/image_ftp");
//        InputStream in = ftp.retrieveFileStream("test");
//        StringBuffer resultBuffer = new StringBuffer();
//        if (in != null)
//        {
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String data = null;
//            try
//            {
//                while ((data = br.readLine()) != null)
//                {
//                    resultBuffer.append(data + "\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else{
//            System.out.println("in为空，不能读取。");
//        }
//        System.out.println(resultBuffer.toString());

        pool.destroy();

    }
}
