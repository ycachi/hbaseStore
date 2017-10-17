package com.asiainfo.base.entity.busi;

import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HRowkey;

/**
 * @author Administrator
 */

@HBaseTable(name="trade_receipt",autoCreate = true,families = {"cf"})
public class TradeReceipt
{
    @HRowkey
    private String rowKey;
    @HColumn(key="name")
    private String fileName;
    @HColumn(key="value")
    private String value;
    @HColumn(key="trade_id")
    private String tradeId;
    @HColumn(key="type")
    private  String  type;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TradeReceipt{" +
                "rowKey='" + rowKey + '\'' +
                ", fileName='" + fileName + '\'' +
                ", value='" + value + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
