package com.asiainfo.base.entity.busi;

import java.util.List;

public class StoreImageVO
{
    private String tradeId;
    private String path;
    private List<ImageVO> images;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ImageVO> getImages() {
        return images;
    }

    public void setImages(List<ImageVO> images) {
        this.images = images;
    }
}
