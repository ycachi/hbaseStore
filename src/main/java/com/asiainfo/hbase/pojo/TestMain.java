package com.asiainfo.hbase.pojo;

import com.asiainfo.common.ImageUtil;
import com.asiainfo.hbase.DefaultHDao;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.plexus.logging.LoggerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class TestMain
{
    static
    {
        LogManager.resetConfiguration();
        PropertyConfigurator.configure("F:\\springboot\\log4j.properties");

    }

    static Logger logger = LoggerFactory.getLogger(TestMain.class);
    public  static void main(String args[])
    {

        logger.debug("abcss");
        logger.info("info");
        try
        {
            DefaultHDao hdao = new DefaultHDao();
//            hdao.create(User.class);
//            hdao.drop(User.class);
//            User  user = new User();
//            user.setAge(15);
//            user.setRowkey("2017092710499001");
//            user.setName("hello world");
//            user.setSex("Man");
//            user.setDesc("welcome");
//            hdao.put(user);
//            User temp=  hdao.get(User.class,"2017092710499001");
//            System.out.println(temp.toString());


//写入图片测试
            logger.info("处理图片开始。。。。");
//            TradeImage image = new TradeImage();
//            image.setTradeId("091298119192102121");
//            image.setImageDesc("身份证图片!");
//            image.setImageType("A");
//            image.setImageName("091298119192102121_A.jpg");
//            image.setRowKey("091298119192102121_A_0919");
//            byte[] data = null;
//            try
//            {
//                data = ImageUtil.image2byte("F:\\091298119192102121_A.jpg");
//                image.setImageByte(data);
//            }catch (Exception e)
//            {
//
//            }
//
////            hdao.drop(TradeImage.class);
////            hdao.put(image);
//            TradeImage newImage = hdao.get(TradeImage.class,"091298119192102121_A_0919");
//            List<TradeImage> list = hdao.query(TradeImage.class,"0000000000000000000000000","9999999999999999999999999");
//            System.out.println("hello:" + newImage.toString());
//            ImageUtil.byte2image(newImage.getImageByte(),"F:\\091298119192102121_A_C.jpg");
//
//
//
//


            System.out.println("hello world!");

        }catch (Exception e)
        {
            e.printStackTrace();

        }

    }
}
