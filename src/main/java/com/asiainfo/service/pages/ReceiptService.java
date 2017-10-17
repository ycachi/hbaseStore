package com.asiainfo.service.pages;

import com.asiainfo.base.entity.busi.TradeReceipt;

import java.util.List;

public interface ReceiptService
{
    List<TradeReceipt> queryAll();
    int save(TradeReceipt receipt);
}
