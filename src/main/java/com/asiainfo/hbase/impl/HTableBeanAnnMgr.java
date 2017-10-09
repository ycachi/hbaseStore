package com.asiainfo.hbase.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HDynamic;
import com.asiainfo.hbase.annotations.HRowkey;
import com.asiainfo.hbase.ex.HTableDefException;
import com.asiainfo.hbase.util.Clazz;
import com.asiainfo.hbase.util.Strs;

public class HTableBeanAnnMgr {
    private static volatile HashMap<Class<?>, HTableBeanAnn> cache = new HashMap<Class<?>, HTableBeanAnn>();
    private static volatile HashSet<String> tableExistanceCheckCache = new HashSet<String>();

    public static <T> HTableBeanAnn getBeanAnn(String hbaesInstanceName, Class<T> beanClass)
            throws HTableDefException {
        HTableBeanAnn hTableBeanAnn = cache.get(beanClass);
        if (hTableBeanAnn != null) return hTableBeanAnn;

        synchronized (cache) {
            hTableBeanAnn = cache.get(beanClass);
            if (hTableBeanAnn != null) return hTableBeanAnn;

            HBaseTable htableAnn = beanClass.getAnnotation(HBaseTable.class);
            if (htableAnn == null) throw new HTableDefException(beanClass + " is not annotationed by HTable");

            hTableBeanAnn = new HTableBeanAnn();
            hTableBeanAnn.setHBaseTable(beanClass);

            for (Field field : beanClass.getDeclaredFields()) {
                processHRowkey(hTableBeanAnn, field);
                processHColumn(hTableBeanAnn, field);
                processHDynamic(hTableBeanAnn, field);
            }

            checkAnnotion(beanClass, hTableBeanAnn);
            hTableBeanAnn.afterPropertiesSet();
            checkTableExistence(hbaesInstanceName, htableAnn, beanClass);

            cache.put(beanClass, hTableBeanAnn);
        }

        return hTableBeanAnn;
    }


    private static void checkAnnotion(Class<?> beanClass, HTableBeanAnn hTableBeanAnn) throws HTableDefException {
        if (hTableBeanAnn.getHRowkeyField() == null)
            throw new HTableDefException(beanClass + " does not define a @HRowkey or @HRowkeyPart field.");
        if (hTableBeanAnn.getHColumnFields().size() == 0 && hTableBeanAnn.getHDynamicFields().size() == 0)
            throw new HTableDefException(beanClass + " should define at least one @HColumn or @HDynamic filed.");
    }

    private static void processHDynamic(HTableBeanAnn hTableBeanAnn, Field field) throws HTableDefException {
        HDynamic hdynamic = field.getAnnotation(HDynamic.class);
        if (hdynamic == null) return;

        // 检查rowkey的字段类型是否是TypeConvertable
        if (!Map.class.isAssignableFrom(field.getType()))
            throw new HTableDefException("@HDynamic can only defined on Map object");

        HBaseTable hbaseTable = hTableBeanAnn.getBeanClass().getAnnotation(HBaseTable.class);
        checkFamily(field.getName(), hdynamic.family(), hbaseTable.families());
        hTableBeanAnn.addHDynamic(field);
    }

    private static void processHColumn(HTableBeanAnn hTableBeanAnn, Field field) throws HTableDefException {
        HColumn hcolumn = field.getAnnotation(HColumn.class);
        if (hcolumn == null) return;

        HBaseTable hbaseTable = hTableBeanAnn.getBeanClass().getAnnotation(HBaseTable.class);
        checkFamily(field.getName(), hcolumn.family(), hbaseTable.families());
        hTableBeanAnn.addHColumn(field);
    }

    private static void checkFamily(String fieldName, String family, String[] families) throws HTableDefException {
        if (Strs.isEmpty(family) && (families == null || families.length == 0 || Strs.isEmpty(families[0])))
            throw new HTableDefException(fieldName + " does not define a family");
    }

    /**
     * @param hTableBeanAnn
     * @param field
     * @throws HTableDefException
     */
    private static void processHRowkey(HTableBeanAnn hTableBeanAnn, Field field) throws HTableDefException {
        HRowkey hrowkey = field.getAnnotation(HRowkey.class);
        if (hrowkey == null) return;

        if (hTableBeanAnn.getHRowkeyField() != null)
            throw new HTableDefException("@HRowkey can only define on no more than one field.");

        hTableBeanAnn.setHRowkey(field);
    }



    private static void checkTableExistence(String hbaesInstanceName, HBaseTable htableAnn, Class<?> beanClass)
            throws HTableDefException {
        //命名规则规则需自定义，没法自动建表
        if (htableAnn.nameCreator() != Void.class) return;

        // 检查HTable表名是否定义
        String tableName = getTableName(hbaesInstanceName,htableAnn,beanClass);
        if (Strs.isEmpty(tableName))
            throw new HTableDefException(beanClass + " is annotationed by @HTable with empty name");

        checkAndCreateTable(hbaesInstanceName, htableAnn, beanClass, tableName);
    }

    protected static void checkAndCreateTable(String hbaesInstanceName, HBaseTable htableAnn, Class<?> beanClass,
            String tableName) throws HTableDefException {
        String cachedName = hbaesInstanceName + "$" + tableName;
        if (tableExistanceCheckCache.contains(cachedName)) return;

        synchronized (tableExistanceCheckCache) {
            if (tableExistanceCheckCache.contains(cachedName)) return;

            checkAndCreateTableWoCache(hbaesInstanceName, htableAnn, tableName);
        }
    }

    protected static void checkAndCreateTableWoCache(String hbaseInstanceName, HBaseTable htableAnn, String tableName)
            throws HTableDefException {
        Admin admin = null;

        try
        {
            admin = HBaseManager.createAdmin(hbaseInstanceName);
            TableName tableN = TableName.valueOf(tableName);
            boolean exist = admin.tableExists(tableN);
            boolean enable = false;
            if(exist)
            {
                enable = admin.isTableEnabled(tableN);
                if(!enable)
                {
                    throw new HTableDefException(tableName + " is not enabled");
                }
            }
            else
            {
                if(htableAnn.autoCreate())
                {
                    if (htableAnn.families() == null || htableAnn.families().length == 0)
                        throw new HTableDefException(tableName + " does not define its families");

                    HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                    for (String fam : htableAnn.families())
                    {
                        tableDesc.addFamily(new HColumnDescriptor(fam));
                    }
                    admin.createTable(tableDesc);

                }
                else
                {
                    throw new HTableDefException(tableName + " does not exist");
                }
            }
            String cachedName = hbaseInstanceName + "$" + tableName;
            tableExistanceCheckCache.add(cachedName);
        } catch (Exception e)
        {
            throw new HTableDefException(e);
        } finally
        {
            HBaseManager.close(admin);
        }
    }

    public static String getTableName(String hbaesInstanceName, HBaseTable hbaseTable, Class<?> beanClass)
            throws HTableDefException {
        if (hbaseTable.nameCreator() == Void.class) return hbaseTable.name();

        Object nameCreator = Clazz.newInstance(hbaseTable.nameCreator());
        Method method = findProperMethod(nameCreator.getClass());

        if (method == null) throw new HTableDefException("no proper method found for " + hbaseTable.nameCreator());

        String tableName = invokeMethod(method, nameCreator, hbaseTable.name());
        if (Strs.isEmpty(tableName))
            throw new HTableDefException(hbaseTable.nameCreator() + " create an empty name");

        return tableName;
    }

    public static Method findProperMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) continue;
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1 || parameterTypes[0] != String.class) continue;
            if (method.getReturnType() != String.class) continue;

            return method;
        }
        return null;
    }

    public static String invokeMethod(Method method, Object nameCreator, String name) throws HTableDefException {
        try {
            return (String) method.invoke(nameCreator, new Object[] { name });
        } catch (Exception e) {
            throw new HTableDefException(e);
        }
    }
}
