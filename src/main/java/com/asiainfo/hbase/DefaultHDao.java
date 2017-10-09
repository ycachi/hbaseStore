package com.asiainfo.hbase;

import static com.asiainfo.hbase.impl.HTableBeanAnnMgr.getBeanAnn;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HDynamic;
import com.asiainfo.hbase.annotations.HTypePair;
import com.asiainfo.hbase.util.*;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;

import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import com.asiainfo.hbase.ex.EmptyValueException;
import com.asiainfo.hbase.ex.FamilyEmptyException;
import com.asiainfo.hbase.ex.HDaoException;
import com.asiainfo.hbase.ex.HTableDefException;
import com.asiainfo.hbase.impl.HBaseManager;
import com.asiainfo.hbase.impl.HTableBeanAnn;
import com.asiainfo.hbase.impl.HTableBeanAnnMgr;
import com.asiainfo.hbase.pool.HConfPoolManager;

public class DefaultHDao extends BaseHDao {
    private static final int FETCH_ROWS = 1000;
    private String hbaseInstanceName;

    public DefaultHDao() {
        this(HConfPoolManager.DEFAULT_INSTANCE);
    }

    public DefaultHDao(String hbaesInstanceName) {
        this.hbaseInstanceName = hbaesInstanceName;
    }

    @Override
    public <T> void delete(String family, Class<T> beanClass, Object rowkey, Object key, Object... keys)
            throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(rowkey);
        Delete delete = new Delete(bRowkey);
        createDeleteKeys(getDefaultFamily(ann, family), delete, key, keys);
        commitDelete(ann.getHBaseTable(), delete, beanClass);
    }

    private void createDeleteKeys(String family, Delete delete, Object key, Object... keys) {
        byte[] famBytes = Bytes.toBytes(family);
        delete.deleteColumn(famBytes, Types.toBytes(key));
        for (Object key1 : keys)
            delete.deleteColumn(famBytes, Types.toBytes(key1));
    }

    private void addKeyAndFirstKeyOnlyFilter(Get get) {
        FilterList filterList = new FilterList();
        filterList.addFilter(new KeyOnlyFilter());
        filterList.addFilter(new FirstKeyOnlyFilter());
        get.setFilter(filterList);
    }

    @Override
    public <T> void put(T bean) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, bean.getClass());
        byte[] bRowkey = ann.getRowkey(bean);

        Put put = new Put(bRowkey);
        createPutValues(bean, ann, put);
        commitPut(ann, put, bean.getClass());
    }

    @Override
    public <T> void put(List<T> beans) throws HDaoException {
        if (beans == null || beans.size() == 0) return;

        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beans.get(0).getClass());
        ArrayList<Put> putsList = new ArrayList<Put>(beans.size());
        for (T bean : beans) {
            byte[] bRowkey = ann.getRowkey(bean);

            Put put = new Put(bRowkey);
            createPutValues(bean, ann, put);
            putsList.add(put);
        }

        commitPut(ann, putsList, beans.get(0).getClass());
    }

    @Override
    protected <T> T getImpl(Class<T> beanClass, Object bean) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(bean);
        return getImpl(bRowkey, beanClass);
    }

    private <T> T getImpl(byte[] bRowkey, Class<T> beanClass) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        Get get = new Get(bRowkey);
        Result rs = getValues(ann, get, beanClass);
        if (rs.isEmpty()) return null;

        T retBean = Clazz.newInstance(beanClass);
        ann.setRowkey(retBean, bRowkey);
        setValues(bRowkey, retBean, ann, rs);

        return retBean;
    }

    @Override
    protected <T> T getImpl(Class<T> beanClass, Object bean, String family, String... families) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(bean);

        Get get = new Get(bRowkey);
        Result rs = getValues(beanClass, ann, get, family, families);
        if (rs.isEmpty()) return null;

        T retBean = Clazz.newInstance(beanClass);
        ann.setRowkey(retBean, bRowkey);

        setValues(retBean, ann, rs, family, families);
        return retBean;
    }

    @Override
    public <T> T get(Class<T> beanClass, Object rowkey, String family, String... families) throws HDaoException {
        return getImpl(beanClass, rowkey, family, families);
    }


    private <T> void delete(byte[] bRowkey, Class<T> beanClass) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        Delete delete = new Delete(bRowkey);
        commitDelete(ann.getHBaseTable(), delete, beanClass);
    }


    private Result getValues(Class<?> beanClass, HTableBeanAnn ann, Get get, String family, String... families)
            throws HDaoException {
        Table table = null;
        try {
            get.addFamily(Bytes.toBytes(family));
            for (String fam : families)
                get.addFamily(Bytes.toBytes(fam));

            table = getHTable(ann, beanClass);
            return table.get(get);
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(table);
        }
    }

    private Result getValues(HTableBeanAnn ann, Get get, Class<?> beanClass) throws HDaoException {
        Table table = null;
        try {
            for (byte[] family : ann.getBfamilies())
                get.addFamily(family);

            table = getHTable(ann, beanClass);
            return table.get(get);
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(table);

        }
    }

    private Table getHTable(HTableBeanAnn ann, Class<?> beanClass) {
        return getHTable(ann.getHBaseTable(), beanClass);
    }

    private Table getHTable(HBaseTable hBaseTable, Class<?> beanClass)
    {
        Table table = null;
        try
        {
            table = HBaseManager.createTable(hbaseInstanceName,getTableName(hBaseTable, beanClass));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return table;
    }

    private String getTableName(HBaseTable hBaseTable, Class<?> beanClass) {
        try {
            return HTableBeanAnnMgr.getTableName(hbaseInstanceName, hBaseTable, beanClass);
        } catch (HTableDefException e) {
            // here should not happen b'coz there was table existance check before.
            throw new RuntimeException(e);
        }
    }

    private void createPutValues(Object bean, HTableBeanAnn ann, Put put)
            throws HDaoException {
        int kvNums = 0;
        for (Field field : ann.getHColumnFields())
            kvNums += processHColumnFields(bean, ann, put, field);

        for (Field field : ann.getHDynamicFields())
            kvNums += processHDynamicFields(bean, ann, put, field);

        if (kvNums == 0) throw new EmptyValueException("There is no values to do in this operation");
    }

    private int processHDynamicFields(Object bean, HTableBeanAnn ann, Put put, Field field) throws HDaoException {
        @SuppressWarnings("unchecked")
        Map<Object, Object> value = (Map<Object, Object>)getFieldValue(ann, bean, field);
        if (value == null || value.isEmpty()) return 0;

        HDynamic hdynamic = field.getAnnotation(HDynamic.class);
        String family = getDefaultFamily(ann, hdynamic.family());
        for (Entry<Object, Object> entry : value.entrySet())
            put.add(Bytes.toBytes(family), Types.toBytes(entry.getKey()), Types.toBytes(entry.getValue()));

        return value.size();
    }

    private int processHColumnFields(Object bean, HTableBeanAnn ann, Put put, Field field)
            throws FamilyEmptyException {
        HColumn kvAnn = field.getAnnotation(HColumn.class);

        String keyValue = kvAnn.key();
        String key = (keyValue == null || keyValue.length() == 0) ? field.getName() : keyValue;
        Object value = getFieldValue(ann, bean, field);
        if (value == null) return 0;

        String family = getDefaultFamily(ann, kvAnn.family());
        put.add(Bytes.toBytes(family), Bytes.toBytes(key), Types.toBytes(value));
        return 1;
    }


    private boolean isAllowedFamily(String strFamily, String family, String... families) {
        if (Strs.equals(strFamily, family)) return true;
        for (String fam : families)
            if (Strs.equals(strFamily, fam)) return true;

        return false;
    }

    private void setValues(Object bean, HTableBeanAnn ann, Result rs, String family, String... families)
            throws FamilyEmptyException {
        for (byte[] bfamily : ann.getBfamilies()) {
            String strFamily = Bytes.toString(bfamily);
            if (isAllowedFamily(strFamily, family, families)) {
                ArrayList<String> usedQualifiers = new ArrayList<String>();
                populateHColumn(bean, ann, rs, bfamily, strFamily, usedQualifiers);
                populateHDynamic(bean, ann, rs, bfamily, strFamily, usedQualifiers);
            }
        }
    }

    private void setValues(byte[] bRowkey, Object bean, HTableBeanAnn ann, Result rs)
            throws HDaoException {
        for (byte[] bfamily : ann.getBfamilies()) {
            String strFamily = Bytes.toString(bfamily);
            List<String> usedQualifiers = new ArrayList<String>();
            populateHColumn(bean, ann, rs, bfamily, strFamily, usedQualifiers);
            populateHDynamic(bean, ann, rs, bfamily, strFamily, usedQualifiers);
        }
    }

    private void setFieldValue(HTableBeanAnn ann, Object bean, Field field, Object value) {
        Fields.setFieldValue(ann.getMethodAccess(), ann.getFieldAccess(), bean, field, value);
    }

    private Object getFieldValue(HTableBeanAnn ann, Object bean, Field field) {
        return Fields.getFieldValue(ann.getMethodAccess(), ann.getFieldAccess(), bean, field);
    }

    private void populateHDynamic(Object bean, HTableBeanAnn ann, Result rs, byte[] bfamily, String strFamily,
            List<String> usedQualifiers) throws FamilyEmptyException {
        NavigableMap<byte[], byte[]> familyMap = rs.getFamilyMap(bfamily);
        FAMILYMA: for (Entry<byte[], byte[]> entry : familyMap.entrySet()) {
            if (usedQualifiers.contains(Hex.toHex(entry.getKey()))) continue;

            for (Field field : ann.getHDynamicFields()) {
                HDynamic hdynamic = field.getAnnotation(HDynamic.class);
                String family = getDefaultFamily(ann, hdynamic.family());
                if (!family.equals(strFamily)) continue;

                HTypePair[] mapping = hdynamic.mapping();
                for (HTypePair hTypePair : mapping) {
                    Object key = Types.fromBytes(entry.getKey(), hTypePair.keyType());
                    if (key == null) continue;

                    Object value = Types.fromBytes(entry.getValue(), hTypePair.valueType());
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> map = (Map<Object, Object>)getFieldValue(ann, bean, field);
                    if (map == null) {
                        map = new HashMap<Object, Object>();
                        setFieldValue(ann, bean, field, map);
                    }

                    map.put(key, value);
                    continue FAMILYMA;
                }
            }

        }
    }

    private void populateHColumn(Object bean, HTableBeanAnn ann, Result rs, byte[] bfamily, String strFamily,
            List<String> usedQualifiers) throws FamilyEmptyException {
        for (Field field : ann.getHColumnFields()) {
            HColumn hcolumn = field.getAnnotation(HColumn.class);
            String family = getDefaultFamily(ann, hcolumn.family());
            if (!family.equals(strFamily)) continue;

            String key = Strs.defaultString(hcolumn.key(), field.getName());
            byte[] qualifier = Bytes.toBytes(key);
            usedQualifiers.add(Hex.toHex(qualifier));
            byte[] value = rs.getValue(bfamily, qualifier);
            setFieldValue(ann, bean, field, Types.fromBytes(value, field.getType()));
        }
    }

    private String getDefaultFamily(HTableBeanAnn ann, String family) throws FamilyEmptyException {
        String retFamily = family;
        if (Strs.isEmpty(retFamily)) {
            String[] families = ann.getHBaseTable().families();
            retFamily = families != null && families.length > 0 ? families[0] : null;
        }

        if (Strs.isEmpty(retFamily)) throw new FamilyEmptyException();

        return retFamily;
    }

    private void commitPut(HTableBeanAnn ann, Put put, Class<?> beanClass) throws HDaoException {
        commitPut(ann, Arrays.asList(put), beanClass);
    }

    private void commitPut(HTableBeanAnn ann, List<Put> put, Class<?> beanClass) throws HDaoException {
        Table table = null;
        try {
            table = getHTable(ann, beanClass);
            table.put(put);
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(table);
        }
    }

    private void commitDelete(HBaseTable htableAnn, Delete delete, Class<?> beanClass) throws HDaoException {
        Table hTable = getHTable(htableAnn, beanClass);
        commitDelete(hTable, delete);
    }

    private void commitDelete(Table hTable, Delete delete) throws HDaoException {
        try {
            hTable.delete(delete);
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(hTable);
        }
    }

    private void closeHTable(Table table) {
        if (table == null) return;
        try {
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(table != null)
            {
                try
                {
                    table.close();
                }catch(IOException e)
                {}
            }


        }
    }

    public <T, V> V get(String family, Class<T> beanClass, Object rowkey, Class<V> valueType, Object key)
            throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(rowkey);

        Get get = new Get(bRowkey);
        byte[] bkey = Types.toBytes(key);
        byte[] bfamily = Bytes.toBytes(getDefaultFamily(ann, family));
        Result rs = getSingleValue(ann, get, bfamily, bkey, beanClass);
        if (rs.isEmpty()) { return null; }

        return Types.fromBytes(rs.getValue(bfamily, bkey), valueType);
    }

    private Result getSingleValue(HTableBeanAnn ann, Get get, byte[] family, byte[] key, Class<?> beanClass)
            throws HDaoException {
        Table table = null;
        try {
            get.addFamily(family);
            get.setFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(key)));
            table = getHTable(ann, beanClass);

            return table.get(get);
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(table);
        }
    }

    protected <T> boolean merge(T bean, boolean ifInsertElseUpdate) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, bean.getClass());
        byte[] bRowkey = ann.getRowkey(bean);
        try {

            Get get = new Get(bRowkey);
            addKeyAndFirstKeyOnlyFilter(get);

            Result rs = getValues(ann, get, bean.getClass());
            if (rs.isEmpty() ^ ifInsertElseUpdate) return false;

            Put put = new Put(bRowkey);
            createPutValues(bean, ann, put);
            commitPut(ann, put, bean.getClass());
            return true;
        } finally {
        }
    }

    public <T> void put(String family, Class<T> beanClass, Object rowkey, Object key, Object value, Object... kvs)
            throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(rowkey);
        byte[] bfamily = Bytes.toBytes(getDefaultFamily(ann, family));
        Put put = new Put(bRowkey);
        createPutKv(ann, put, bfamily, key, value, kvs);
        commitPut(ann, put, beanClass);
    }

    public <T> void delete(Class<T> beanClass, Object rowkey) throws HDaoException
    {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] bRowkey = ann.getRowkey(rowkey);
        delete(bRowkey, beanClass);
    }

    private <T> void createPutKv(HTableBeanAnn ann, Put put, byte[] family, Object key, Object value, Object[] kvs) {
        put.add(family, Types.toBytes(key), Types.toBytes(value));
        for (int i = 0; i < kvs.length; i += 2)
            if (i + 1 < kvs.length) put.add(family, Types.toBytes(kvs[i]), Types.toBytes(kvs[i + 1]));
    }

    protected <T> List<T> queryImpl(Class<T> beanClass, Object startRowkey, Object stopRowkey, int maxRows) throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        byte[] startRow = ann.getRowkey(startRowkey);
        byte[] stopRow = stopRowkey != null ? ann.getRowkey(stopRowkey) : null;
        return queryImpl(beanClass, startRow, stopRow, maxRows);
    }

    private <T> List<T> queryImpl(Class<T> beanClass, byte[] startRow, byte[] stopRow, int maxRows)
            throws HDaoException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        Scan scan = stopRow != null ? new Scan(startRow, stopRow) : new Scan(startRow);
        scan.setCaching(FETCH_ROWS);

        Table table = null;
        try {
            for (byte[] family : ann.getBfamilies())
                scan.addFamily(family);

            table = getHTable(ann, beanClass);
            ResultScanner rr = table.getScanner(scan);

            int rows = 0;
            ArrayList<T> arrayList = new ArrayList<T>();
            L: for (Result[] rss = rr.next(FETCH_ROWS); rss != null && rss.length > 0; rss = rr.next(FETCH_ROWS)) {
                for (Result rs : rss) {
                    T retBean = Clazz.newInstance(beanClass);
                    ann.setRowkey(retBean, rs.getRow());
                    setValues(rs.getRow(), retBean, ann, rs);
                    arrayList.add(retBean);
                    if (maxRows > 0 && ++rows == maxRows) break L;
                }
            }

            return arrayList;
        } catch (IOException e) {
            throw new HDaoException(e);
        } finally {
            closeHTable(table);
        }
    }

    public <T> void trunc(Class<T> beanClass) throws HTableDefException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        Admin admin = null;
        try {
            admin = HBaseManager.createAdmin(hbaseInstanceName);
            String tableName = getTableName(ann.getHBaseTable(), beanClass);
            TableName tableN = TableName.valueOf(tableName);
            HTableDescriptor tableDescriptor = admin.getTableDescriptor(tableN);

            if (!admin.isTableDisabled(tableN)) admin.disableTable(tableN);

            admin.deleteTable(tableN);
            admin.createTable(tableDescriptor);
        } catch (Exception e) {
            throw new HTableDefException(e);
        } finally {
            HBaseManager.close(admin);
        }
    }


    public <T> void create(Class<T> beanClass) throws HTableDefException {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);

        Admin admin = null;
        try {
            admin = HBaseManager.createAdmin(hbaseInstanceName);
            String tableName = getTableName(ann.getHBaseTable(), beanClass);
            Set<String> families = ann.getFamilies();
            if(families != null && !families.isEmpty())
            {
                HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                for (String str : families)
                {
                    HColumnDescriptor family = new HColumnDescriptor(str);
                    family.setCompressionType(Compression.Algorithm.SNAPPY);
                    family.setMaxVersions(1);
                    tableDesc.addFamily(family);
                }
                admin.createTable(tableDesc);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new HTableDefException(e);
        } finally {
            HBaseManager.close(admin);
        }
    }

    public <T> void drop(Class<T> beanClass) throws HTableDefException
    {
        HTableBeanAnn ann = getBeanAnn(hbaseInstanceName, beanClass);
        Admin admin = null;
        try {
            admin = HBaseManager.createAdmin(hbaseInstanceName);
            String tableName = getTableName(ann.getHBaseTable(), beanClass);
            TableName TableN = TableName.valueOf(tableName);
            System.out.println("TableName: "+tableName);
            admin.disableTable(TableN);
            admin.deleteTable(TableN);


        } catch (Exception e) {
            e.printStackTrace();
            throw new HTableDefException(e);
        } finally {
            HBaseManager.close(admin);
        }
    }

}
