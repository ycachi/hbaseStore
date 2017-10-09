package com.asiainfo.hbase.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import org.apache.hadoop.hbase.util.Bytes;
import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HDynamic;
import com.asiainfo.hbase.ex.HDaoException;
import com.asiainfo.hbase.ex.HTableDefException;
import com.asiainfo.hbase.util.Fields;
import com.asiainfo.hbase.util.Strs;
import com.asiainfo.hbase.util.Types;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import static com.google.common.collect.Lists.*;

import static com.google.common.collect.Sets.*;

public class HTableBeanAnn {
    private Class<?> beanClass;

    private Field hRowkeyField;

    private MethodAccess methodAccess;
    private FieldAccess fieldAccess;

    private ArrayList<Field> hColumnFields = newArrayList();
    private ArrayList<Field> hDynamicFields = newArrayList();

    private Set<String> families = newHashSet();
    private Set<byte[]> bfamilies = newHashSet();
    public void afterPropertiesSet() throws HTableDefException {
        for (String f : families) {
            bfamilies.add(Bytes.toBytes(f));
        }
    }

    public byte[] getRowkey(Object bean) {
        if (bean instanceof byte[]) {
            return (byte[]) bean;
        }

        if (hRowkeyField != null) {
            Object rowkeyValue = bean;
            if (bean.getClass() == beanClass) {
                rowkeyValue = Fields
                        .getFieldValue(methodAccess, fieldAccess, bean, getHRowkeyField());
            }

            return Types.toBytes(rowkeyValue);
        }

        return null;
    }

    public void setRowkey(Object bean, byte[] bRowkey) throws HDaoException {
        if (hRowkeyField != null)
        {
            Fields.setFieldValue(methodAccess, fieldAccess, bean, hRowkeyField,
                    Types.fromBytes(bRowkey, hRowkeyField.getType()));
            return;
        }
    }

    public void setHBaseTable(Class<?> beanClass) {
        this.setBeanClass(beanClass);
        HBaseTable hbaseTable = beanClass.getAnnotation(HBaseTable.class);
        if (hbaseTable.families() != null) {
            for (String family : hbaseTable.families()) {
                families.add(family);
            }
        }
    }

    public void setHRowkey(Field field) {
        this.hRowkeyField = field;
    }

    public void addHColumn(Field field) {
        hColumnFields.add(field);
        HColumn hcolumn = field.getAnnotation(HColumn.class);
        if (Strs.isNotEmpty(hcolumn.family())) {
            families.add(hcolumn.family());
        }
    }

    public Field getHRowkeyField() {
        return hRowkeyField;
    }

    public void setRowkeyField(Field rowkeyField) {
        this.hRowkeyField = rowkeyField;
    }

    public void addHDynamic(Field field) {
        this.hDynamicFields.add(field);
        HDynamic hdynamic = field.getAnnotation(HDynamic.class);
        if (Strs.isNotEmpty(hdynamic.family())) {
            families.add(hdynamic.family());
        }
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.methodAccess = MethodAccess.get(beanClass);
        this.fieldAccess = FieldAccess.get(beanClass);
    }

    public ArrayList<Field> getHColumnFields() {
        return hColumnFields;
    }

    public ArrayList<Field> getHDynamicFields() {
        return hDynamicFields;
    }

    public Set<String> getFamilies() {
        return families;
    }

    public void setFamilies(Set<String> families) {
        this.families = families;
    }

    public Set<byte[]> getBfamilies() {
        return bfamilies;
    }

    public void setBfamilies(Set<byte[]> bfamilies) {
        this.bfamilies = bfamilies;
    }


    public HBaseTable getHBaseTable() {
        return beanClass.getAnnotation(HBaseTable.class);
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public FieldAccess getFieldAccess() {
        return fieldAccess;
    }

}
