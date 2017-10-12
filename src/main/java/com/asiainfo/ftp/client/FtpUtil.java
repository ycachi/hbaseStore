package com.asiainfo.ftp.client;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FtpUtil
{
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public static byte[] readFile(FTPClient ftp, String  path, String fileName)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            if(ftp != null && ftp.isConnected())
            {
                ftp.changeWorkingDirectory(path);
                ftp.retrieveFile(fileName,out);
                return out.toByteArray();

            }

        }catch(Exception e)
        {
            logger.error("读取文件流异常！",e);
        }finally
        {
            if(out != null)
            {
                try {
                    out.close();
                }catch(Exception e)
                {
                    logger.error("关闭IO 流异常！",e);
                }

            }
        }
        return null;

    }


    /**
     * 上传文件到FTP服务器
     * @param localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName 上传到服务器的文件名
     * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录
     * @return
     */
    public static boolean upload(FTPClient ftp,String localDirectoryAndFileName, String ftpFileName, String ftpDirectory)
    {
        if (ftp == null || !ftp.isConnected())
        {
            return false;
        }
        boolean flag = false;
        File srcFile = new File(localDirectoryAndFileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcFile);
            // 创建目录
            mkDir(ftp,ftpDirectory);
            // 上传
            flag = ftp.storeFile(new String(ftpFileName.getBytes(), "iso-8859-1"), fis);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(fis != null )
            {
                try {
                    fis.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        return flag;
    }

    /**
     * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
     * @param ftpPath 需要创建的目录
     * @return
     */
    public static boolean mkDir(FTPClient ftp,String ftpPath)
    {
        if (ftp == null || !ftp.isConnected())
        {
            return false;
        }
        try
        {
            // 将路径中的斜杠统一
            char[] chars = ftpPath.toCharArray();
            StringBuffer sbStr = new StringBuffer(256);
            for (int i = 0; i < chars.length; i++)
            {
                if ('\\' == chars[i]) {
                    sbStr.append('/');
                } else {
                    sbStr.append(chars[i]);
                }
            }
            ftpPath = sbStr.toString();
            System.out.println("ftpPath:" + ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftp.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
                ftp.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftp.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                    ftp.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从FTP服务器上下载文件
     * @param ftpDirectoryAndFileName ftp服务器文件路径，以/dir形式开始
     * @param localDirectoryAndFileName 保存到本地的目录
     * @return
     */
    public static boolean get(FTPClient ftpClient,String ftpDirectory,String ftpFileName, String localDirectoryAndFileName)
    {
        if (ftpClient == null || !ftpClient.isConnected())
        {
            return false;
        }
        ftpClient.enterLocalPassiveMode();
        try
        {
            ftpClient.changeWorkingDirectory(ftpDirectory);
            ftpClient.retrieveFile(new String(ftpFileName.getBytes(), "iso-8859-1"),
                    new FileOutputStream(localDirectoryAndFileName));
            logger.info(ftpClient.getReplyString());
            return true;
        } catch (IOException e)
        {
            logger.error("下载失败！ftpFile：" + ftpDirectory + "/" +ftpFileName ,e);
            return false;
        }
    }
}
