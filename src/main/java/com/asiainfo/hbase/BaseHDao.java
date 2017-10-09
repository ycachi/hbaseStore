package com.asiainfo.hbase;

import java.util.List;
import com.asiainfo.hbase.ex.HDaoException;

public abstract class BaseHDao implements HDao {
    public <T> void put(T bean) throws HDaoException {
        put(bean);
    }

    public <T> void put(List<T> beans) throws HDaoException {
        put(beans);
    }

    public <T, V> V get(Class<T> beanClass, Object rowkey, Class<V> valueType, Object key) throws HDaoException {
        return get("", beanClass, rowkey, valueType, key);
    }


    public <T> void put(Class<T> beanClass, Object rowkey, Object key, Object value, Object... kvs)
            throws HDaoException {
        put("", beanClass, rowkey, key, value, kvs);
    }


    public <T> List<T> query(Class<T> beanClass, Object startRowkey, Object stopRowkey) throws HDaoException {
        return query(beanClass, startRowkey, stopRowkey, 0);
    }


    public <T> void delete(Class<T> beanClass, Object rowkey, Object key, Object... keys) throws HDaoException {
        delete("", beanClass, rowkey, key, keys);
    }


    public <T> boolean insert(T bean) throws HDaoException {
        return merge(bean, true);
    }

    public <T> boolean update(T bean) throws HDaoException {
        return merge(bean, false);
    }

    public <T> T get(Class<T> beanClass, Object rowkey) throws HDaoException {
        return getImpl(beanClass, rowkey);
    }

    public <T> List<T> query(Class<T> beanClass, Object startRowkey, Object stopRowkey, int maxRows)
            throws HDaoException {
        return queryImpl(beanClass, startRowkey, stopRowkey, maxRows);
    }

    abstract protected <T> T getImpl(Class<T> beanClass, Object bean) throws HDaoException;

    abstract protected <T> T getImpl(Class<T> beanClass, Object bean, String family, String... families)
            throws HDaoException;

    abstract protected <T> List<T> queryImpl(Class<T> beanClass, Object startRowkey, Object stopRowkey, int maxRows) throws HDaoException;

    abstract protected <T> boolean merge(T bean, boolean isInsert) throws HDaoException;
}
