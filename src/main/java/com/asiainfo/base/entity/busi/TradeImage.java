package com.asiainfo.base.entity.busi;

import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HRowkey;
import com.asiainfo.hbase.pojo.Base;

import java.util.Arrays;

@HBaseTable(name="trade_image",autoCreate = true,families = {"cf","fi"})
public class TradeImage extends Base
{
    @HRowkey
    private String rowKey;
    @HColumn(key="name")
    private String imageName;
    @HColumn(key="desc")
    private  String  imageDesc;
    @HColumn(key="image")
    private byte[] imageByte;
    @HColumn(key="trade")
    private String tradeId;
    @HColumn(key="type")
    private  String  imageType;


    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Override
    public String toString() {
        return "TradeImage{" +
                "rowKey='" + rowKey + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageDesc='" + imageDesc + '\'' +
                ", imageByte=" + Arrays.toString(imageByte) +
                ", tradeId='" + tradeId + '\'' +
                ", imageType='" + imageType + '\'' +
                '}';
    }
}
