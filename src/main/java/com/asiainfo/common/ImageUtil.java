package com.asiainfo.common;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

public class ImageUtil
{

    private static Logger logger = Logger.getLogger(ImageUtil.class);

    //图片到byte数组
    public static byte[] image2byte(byte[] image, String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new FileImageInputStream(new File(path));
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            logger.error("文件找不到异常！",ex1);

        } catch (IOException ex1) {
            logger.error("IO处理异常！",ex1);
        }finally {

            if(input != null)
            {
                try
                {
                    input.close();
                }catch (Exception e)
                {}
            }
            if(output != null)
            {
                try
                {
                    output.close();
                }catch (Exception e)
                {}
            }
        }
        return data;
    }

    //byte数组到图片
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        FileImageOutputStream imageOutput = null;
        try {
            File file=  new File(path);
            if(file != null && file.exists())
            {
                file.delete();
            }
            imageOutput = new FileImageOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception ex) {
            logger.error("字节码写图片异常！",ex);
        }
        finally {
            if(imageOutput != null)
            {
                try
                {
                    imageOutput.close();
                }catch (Exception ex)
                {}

            }

        }
    }

    //byte数组到16进制字符串
    public static String byte2string(byte[] data) {
        if (data == null || data.length <= 1) return "0x";
        if (data.length > 200000) return "0x";
        StringBuffer sb = new StringBuffer();
        int buf[] = new int[data.length];
        //byte数组转化成十进制
        for (int k = 0; k < data.length; k++) {
            buf[k] = data[k] < 0 ? (data[k] + 256) : (data[k]);
        }
        //十进制转化成十六进制
        for (int k = 0; k < buf.length; k++) {
            if (buf[k] < 16) sb.append("0" + Integer.toHexString(buf[k]));
            else sb.append(Integer.toHexString(buf[k]));
        }
        return "0x" + sb.toString().toUpperCase();
    }

}
