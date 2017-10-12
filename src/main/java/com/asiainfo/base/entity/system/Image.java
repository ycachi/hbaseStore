package com.asiainfo.base.entity.system;

import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HRowkey;

@HBaseTable(name="image",families = {"cf"})
public class Image
{
    @HRowkey
    private String rowkey;
    @HColumn(key="forward_image")
    private byte[] forwardImage;
    @HColumn(key="back_image")
    private byte[] backImage;

    private String forwardPath;

    private String backPath;

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public byte[] getForwardImage() {
        return forwardImage;
    }

    public void setForwardImage(byte[] forwardImage) {
        this.forwardImage = forwardImage;
    }

    public byte[] getBackImage() {
        return backImage;
    }

    public void setBackImage(byte[] backImage) {
        this.backImage = backImage;
    }

    public String getForwardPath() {
        return forwardPath;
    }

    public void setForwardPath(String forwardPath) {
        this.forwardPath = forwardPath;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }
}
