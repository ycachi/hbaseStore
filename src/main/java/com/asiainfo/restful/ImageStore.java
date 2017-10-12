package com.asiainfo.restful;

import com.asiainfo.base.entity.busi.ImageVO;
import com.asiainfo.base.entity.busi.StoreImageVO;
import com.asiainfo.base.entity.busi.TradeImage;
import com.asiainfo.common.StringTools;
import com.asiainfo.ftp.client.FtpUtil;
import com.asiainfo.ftp.pool.FTPClientPool;
import com.asiainfo.hbase.DefaultHDao;
import com.asiainfo.hbase.ex.HDaoException;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"store"})
public class ImageStore
{

    @Autowired
    private FTPClientPool pool;
    @RequestMapping(value = "/deal",method = RequestMethod.POST)
    public void dealImage(@RequestBody StoreImageVO storeImage) throws HDaoException
    {
        System.out.println("Hello Image! -- begin");
        FTPClient ftp = pool.getFTPClient();
        DefaultHDao hdao = new DefaultHDao();
        String tradeId = storeImage.getTradeId();
        String path = storeImage.getPath();
        List<ImageVO> images = storeImage.getImages();
        System.out.println(tradeId);
        System.out.println(path);
        if(images != null && !images.isEmpty())
        {
            byte[] by = null;
            TradeImage tradeImage = null;
            for (ImageVO image:images)
            {
                System.out.println(image.toString());
                by = FtpUtil.readFile(ftp,path,image.getName());
                if(by != null && by.length > 0)
                {
                    tradeImage = new TradeImage();
                    tradeImage.setTradeId(tradeId);
                    tradeImage.setImageType(image.getType());
                    tradeImage.setImageByte(by);
                    tradeImage.setImageName(image.getName());
                    tradeImage.setRowKey(tradeId+"_"+image.getType());
                    hdao.put(tradeImage);

                }
                
            }

        }
        if(StringTools.isValid(tradeId))
        {
            //获取客户身份证小头像

        }
        else
        {

        }

        System.out.println("Hello Image! -- end");

    }


}
