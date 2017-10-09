// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 
// Source File Name:   Base.java

package com.asiainfo.hbase.pojo;


import java.io.Serializable;

public abstract class Base implements Serializable
{

    public Base()
    {
    }

    public String getRowKey()
    {
        return rowKey;
    }

    public void setRowKey(String rowKey)
    {
        this.rowKey = rowKey;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private String rowKey;
    private String value;
    private String tableName;
}
