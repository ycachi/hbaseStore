package com.asiainfo.controller.pages;

import com.asiainfo.base.entity.busi.TradeImage;
import com.asiainfo.base.entity.system.Image;
import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.common.ImageUtil;
import com.asiainfo.common.StringTools;
import com.asiainfo.hbase.DefaultHDao;
import com.asiainfo.service.pages.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ImageController
{

    @Autowired
    private ImageService imageService;

    @RequestMapping("/imageList")
    public String list(ModelMap model, HttpServletRequest request)
    {
        List<Image> images = imageService.queryAll();
        model.addAttribute("images", images);

        return "/pages/imageList";
    }

    @RequestMapping("/viewImage")
    public String view(ModelMap model, HttpServletRequest request,String tradeId) throws Exception {
        if(!StringTools.isValid(tradeId))
        {
            throw new Exception("tradeId 不能为空！");
        }
        DefaultHDao hbaseDao = new DefaultHDao();
        String path = this.getClass().getClassLoader().getResource("").getPath();

        //A
        TradeImage newImage = hbaseDao.get(TradeImage.class,tradeId+ "_"+ "A");
        TradeImage newImageB = hbaseDao.get(TradeImage.class,tradeId+ "_"+ "B");
        TradeImage newImageC = hbaseDao.get(TradeImage.class,tradeId+ "_"+ "C");
        TradeImage newImageD = hbaseDao.get(TradeImage.class,tradeId+ "_"+ "D");
        byte[] imageByte = newImage.getImageByte();

        ImageUtil.byte2image(imageByte, path + "\\images\\down\\"+newImage.getImageName());

        imageByte = newImageB.getImageByte();
        ImageUtil.byte2image(imageByte, path + "\\images\\down\\"+newImageB.getImageName());

        imageByte = newImageC.getImageByte();
        ImageUtil.byte2image(imageByte, path + "\\images\\down\\"+newImageC.getImageName());

        imageByte = newImageD.getImageByte();
        ImageUtil.byte2image(imageByte, path + "\\images\\down\\"+newImageD.getImageName());

        model.addAttribute("tradeId", tradeId);
        model.addAttribute("imageA", "images/down/"+newImage.getImageName());
        model.addAttribute("imageB", "images/down/"+newImageB.getImageName());
        model.addAttribute("imageC", "images/down/"+newImageC.getImageName());
        model.addAttribute("imageD", "images/down/"+newImageD.getImageName());

        return "/pages/viewImage";
    }

    @RequestMapping("/newImage")
    public String newUser(Model model) {
        model.addAttribute("image", new Image());
        return "/pages/addImage";
    }


}
