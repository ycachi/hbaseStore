package com.asiainfo.controller.pages;

import com.asiainfo.base.entity.busi.TradeReceipt;
import com.asiainfo.common.StringTools;
import com.asiainfo.common.UUIDUtil;
import com.asiainfo.hbase.DefaultHDao;
import com.asiainfo.hbase.ex.HDaoException;
import com.asiainfo.service.pages.ReceiptService;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping({"store"})
public class ReceiptController
{

    @Autowired
    private ReceiptService service;

    @RequestMapping("/receiptList")
    public String list(ModelMap model, HttpServletRequest request)
    {
        List<TradeReceipt> receipts = service.queryAll();
        model.addAttribute("receipts", receipts);

        return "/pages/receiptList";
    }

    @RequestMapping("/viewReceipt")
    public String view(ModelMap model, HttpServletRequest request,String tradeId) throws Exception {
        if(!StringTools.isValid(tradeId))
        {
            throw new Exception("tradeId 不能为空！");
        }
        DefaultHDao hbaseDao = new DefaultHDao();
        String path = this.getClass().getClassLoader().getResource("").getPath();

//
        return "/pages/viewReceipt";
    }

    @RequestMapping("/addReceipt")
    public String addPdf(Model model) {
        model.addAttribute("receipt", new TradeReceipt());
        return "/pages/addReceipt";
    }


    @RequestMapping(value = "/saveReceipt")
    public @ResponseBody String upload(@RequestParam("receipt") MultipartFile Receipt)
    {
        System.out.println("hello world !");

        try
        {

            if(Receipt.isEmpty()){
                return "文件空";
            }
            InputStream in = null;
            try {
                in = Receipt.getInputStream();
                byte[] bytes = IOUtils.toByteArray(in);
                String key = UUIDUtil.getUUIDKey();
                DefaultHDao hdao = new DefaultHDao();
                String strBase64 = new BASE64Encoder().encode(bytes);
                if(strBase64 != null)
                {
//                Receipt
                    JSONObject js = new JSONObject();
                    js.put("trade_id",key);
                    js.put("pdf",strBase64);
                    js.put("type","PDF");
                    js.put("fileName",Receipt.getOriginalFilename());
                    parseJson(js);

                }
            } catch (Exception e) {
                if(in != null )
                {
                    try
                    {
                        in.close();
                    }catch (Exception ep)
                    {}

                }


            }
        }catch(Exception e){
            return "上传失败!";
        }
        return "上传完成!";

    }

    private void parseJson(JSONObject ob) throws HDaoException
    {

        if(ob != null && !ob.isEmpty())
        {
            String key = (String)ob.get("trade_id");
            String pdf = (String)ob.get("pdf");
            String type = (String)ob.get("type");
            String fileName = ob.getString("fileName");
            DefaultHDao hdao = new DefaultHDao();
            TradeReceipt tradeReceipt = new TradeReceipt();
            tradeReceipt.setTradeId(key);
            tradeReceipt.setValue(pdf);
            tradeReceipt.setType(type);
            tradeReceipt.setFileName(fileName);
            tradeReceipt.setRowKey(key);
            hdao.put(tradeReceipt);
            service.save(tradeReceipt);
        }


    }

    public static void main(String[] args) throws Exception
    {

        FileOutputStream fop = null;
        try{
            DefaultHDao hdao = new DefaultHDao();
            TradeReceipt tradeReceipt = hdao.get(TradeReceipt.class,"e7c9c4642e3542249b88579326c76e76");
            String fileName =tradeReceipt.getFileName();
            String value =tradeReceipt.getValue();


            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(value);


            File file = new File("D://" + fileName);;
            fop = new FileOutputStream(file);

            fop.write(decodedBytes);
            fop.flush();
            fop.close();

        }catch (Exception e)
        {
            if(fop != null)
            {
                try {
                    fop.close();
                }catch(Exception eo){}

            }
        }



    }


}
