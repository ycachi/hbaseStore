package com.asiainfo.service.pages.impl;

import com.asiainfo.base.entity.system.Image;
import com.asiainfo.base.mapper.pages.ImageMapper;
import com.asiainfo.service.pages.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ImageServiceImpl implements ImageService
{
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public List<Image> queryAll()
    {
        return imageMapper.findAll();
    }
}
