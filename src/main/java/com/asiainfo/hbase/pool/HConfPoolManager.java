package com.asiainfo.hbase.pool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.asiainfo.common.StringTools;
import com.asiainfo.hbase.config.HbaseConnectionConfig;
import com.asiainfo.hbase.util.ConfigUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTable Pool 管理器
 * @author
 *
 */
public class HConfPoolManager
{
    private HConfPoolManager()
    {

    }
    static Logger logger = LoggerFactory.getLogger(HConfPoolManager.class);

    public static final String DEFAULT_INSTANCE = "default";

    private  static final HashMap<String, Connection> poolCache = new HashMap<String, Connection>();
    private  static final HashMap<String, Configuration> confCache = new HashMap<String, Configuration>();

    /**
     * 创建HBase实例。
     * @param hbaseInstanceName 实例名称。
     * @param quorum hbase.zookeeper.quorum
     * @param clientPort hbase.zookeeper.property.clientPort
     * @return
     * @return Configuration
     */
    public static Connection getConnection(String hbaseInstanceName, String quorum, String clientPort,String timeOut) throws IOException
    {
        clientPort = (clientPort == null || "".equals(clientPort)) ? "2181": clientPort;
        timeOut = (timeOut == null || "".equals(clientPort)) ? "180000": timeOut;

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum",quorum);
        configuration.set("hbase.zookeeper.property.clientPort",clientPort);
        configuration.set("zookeeper.session.timeout",timeOut);

        return getConnection(hbaseInstanceName,configuration);
    }

    /**
     *
     * @param hbaseInstanceName
     * @param configuration
     */
    private static Connection getConnection(String hbaseInstanceName, Configuration configuration) throws IOException
    {
        Connection connection = null;
        if(hbaseInstanceName != null )
        {
            synchronized (poolCache)
            {
                connection = poolCache.get(hbaseInstanceName);
                if (connection == null)
                {
                    connection =  ConnectionFactory.createConnection(configuration);
                    poolCache.put(hbaseInstanceName, connection);
                }else if(connection.isClosed())
                {
                    connection.close();
                    connection =  ConnectionFactory.createConnection(configuration);
                    poolCache.put(hbaseInstanceName, connection);
                }
            }

        }
        return connection;


    }
    public static Connection getConnection(String hbaseInstanceName, Map<String, String> config) throws IOException
    {
        hbaseInstanceName = StringTools.isValid(hbaseInstanceName) ? hbaseInstanceName : DEFAULT_INSTANCE;
        Configuration conf = confCache.get(hbaseInstanceName);
        if(conf == null)
        {
            if(config == null || config.isEmpty())
            {
                config = HbaseConnectionConfig.getConnectionParam(hbaseInstanceName);
                if(config == null || config.isEmpty())
                {
                    config = ConfigUtils.getConfig(hbaseInstanceName);
                }
            }
            Configuration temp =  HBaseConfiguration.create();;
            if(config != null && !config.isEmpty())
            {
                for (Map.Entry<String, String> entry : config.entrySet())
                {
                    temp.set(entry.getKey(), entry.getValue());
                }
            }
            synchronized (confCache)
            {
                conf = confCache.get(hbaseInstanceName);
                if(conf == null)
                {
                    conf = temp;
                    confCache.put(hbaseInstanceName,conf);
                }
            }
        }

        return  getConnection(hbaseInstanceName,conf);

    }

    public static Connection  getConnection(String hbaseInstanceName) throws IOException
    {
        hbaseInstanceName = StringTools.isValid(hbaseInstanceName) ? hbaseInstanceName : DEFAULT_INSTANCE;
        return getConnection(hbaseInstanceName,new HashMap<String,String>());
    }

}
