package com.asiainfo.hbase.config;

import com.asiainfo.hbase.annotations.HConnectionConfig;

import java.util.HashMap;
import java.util.Map;

@HConnectionConfig
public class DefaultConfig
{
    @HConnectionConfig(value="test")
    public Map<String,String> initConfig()
    {
        Map<String,String> param = new HashMap<String,String>();
        param.put("hbase.zookeeper.quorum","hadoop01,hadoop02,hadoop03,hadoop04");
        param.put("hbase.zookeeper.property.clientPort","2181");
        return param;
    }
}
