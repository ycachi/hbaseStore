package com.asiainfo.hbase.impl;

import com.asiainfo.hbase.ex.HTableDefException;
import com.asiainfo.hbase.pool.HConfPoolManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;



public class HBaseManager
{

    public static Table createTable(String hbaseInstanceName,String tableName) throws HTableDefException {
        try {
            Connection connection =  HConfPoolManager.getConnection(hbaseInstanceName);
            Table table = connection.getTable(TableName.valueOf(tableName));
            return table;
        } catch (Exception e) {
            throw new HTableDefException(e);
        }
    }

    public static Admin createAdmin(String hbaseInstanceName) throws HTableDefException {
        try {
            Connection connection = HConfPoolManager.getConnection(hbaseInstanceName);
            Admin admin = connection.getAdmin();
            return admin;
        } catch (Exception e) {
            throw new HTableDefException(e);
        }
    }

    public static Connection createConnection(String hbaseInstanceName) throws HTableDefException {
        try {
            Connection connection =  HConfPoolManager.getConnection(hbaseInstanceName);
            return connection;
        } catch (Exception e) {

            throw new HTableDefException(e);
        }
    }

    public static void close(Admin admin) {
        if (admin == null) return;
        try
        {
            admin.close();
            admin.getConnection().close();
        }catch (Exception e)
        {

        }finally {

        }

    }

    public static void close(Table table) {
        if (table == null) return;
        try
        {
            table.close();
        }catch (Exception e)
        {

        }finally {
        }

    }

    public static void main(String args[])
    {
        try
        {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "hadoop01,hadoop02,hadoop03,hadoop04");
//            conf.set("hadoop.job.ugi","root,root");
            Connection connection = ConnectionFactory.createConnection(conf);
            Admin admin = null;
            try {
                admin = connection.getAdmin();
                TableName[] tableNames = admin.listTableNames();
                System.out.println("tableName length : " + tableNames.length);
                if(tableNames != null && tableNames.length > 0)
                {
                    for (TableName name:tableNames)
                    {
                        System.out.println(name);
                    }
                }
                TableName name = TableName.valueOf("test_user");
//                admin.disableTable(name);
//                admin.deleteTable(name);
//                DefaultHDao hdao = new DefaultHDao();
//                hdao.create(User.class);
//                HTableDescriptor tableDescriptor = new HTableDescriptor("test_user");
//                HColumnDescriptor family = new HColumnDescriptor("cf");
//                family.setCompressionType(Compression.Algorithm.SNAPPY);
//                family.setMaxVersions(1);
//                tableDescriptor.addFamily(family);
//                admin.createTable(tableDescriptor);
                System.out.print("hello world!");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(admin != null)
                {
                    close(admin);
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



}
