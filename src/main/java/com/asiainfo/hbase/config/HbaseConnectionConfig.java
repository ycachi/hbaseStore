package com.asiainfo.hbase.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class HbaseConnectionConfig
{
    private static Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
    private static final String SEPARATOR = ".";
    static
    {
        Properties prop = new Properties();
        try
        {
            InputStream in = Object.class.getResourceAsStream("/hbase.properties");
            prop.load(in);
            Set<Map.Entry<Object, Object>> set = prop.entrySet();
            if(set != null && !set.isEmpty())
            {
                String key = null;
                String value = null;
                String prefix = null;
                Map<String,String> param = null;
                for(Map.Entry<Object, Object> entry:set)
                {
                    if(entry.getKey() != null && entry.getValue() != null)
                    {
                        key = String.valueOf(entry.getKey());
                        value = String.valueOf(entry.getValue());
                        if(key.contains(SEPARATOR))
                        {
                            prefix = key.substring(0,key.indexOf(SEPARATOR));
                            key = key.substring(key.indexOf(SEPARATOR) + 1);
                            if(key != null && key.length() > 0)
                            {
                                param = map.get(prefix);
                                if(param == null)
                                {
                                    param = new HashMap<String,String>();
                                    map.put(prefix,param);
                                }
                                param.put(key,value);
                            }

                        }

                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HbaseConnectionConfig() { }

    public static Configuration  getConnectionConf(String instanceName)
    {
        instanceName = (instanceName == null ||instanceName == "")
                    ? "default" : instanceName;
        Configuration configuration = HBaseConfiguration.create();
        Map<String,String> param = map.get(instanceName);
        for (Map.Entry<String, String> entry : param.entrySet())
        {
            configuration.set(entry.getKey(), entry.getValue());
        }
        return configuration;
    }

    public static Map<String,String>  getConnectionParam(String instanceName)
    {
        instanceName = (instanceName == null ||instanceName == "")
                ? "default" : instanceName;
        Configuration configuration = HBaseConfiguration.create();
        Map<String,String> param = map.get(instanceName);
        return param;
    }
}
