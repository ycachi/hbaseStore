package com.asiainfo.service.pages.impl;

import com.asiainfo.base.entity.busi.TradeReceipt;
import com.asiainfo.base.mapper.pages.ReceiptMapper;
import com.asiainfo.service.pages.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ReceiptServiceImpl implements ReceiptService
{
    @Autowired
    private ReceiptMapper receiptMapper;

    @Override
    public List<TradeReceipt> queryAll()
    {
        return receiptMapper.findAll();
    }
    @Override
    public int save(TradeReceipt receipt){ return receiptMapper.save(receipt); }
}
