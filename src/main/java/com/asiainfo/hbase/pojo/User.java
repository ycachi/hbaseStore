package com.asiainfo.hbase.pojo;

import com.asiainfo.hbase.annotations.HBaseTable;
import com.asiainfo.hbase.annotations.HColumn;
import com.asiainfo.hbase.annotations.HRowkey;
import com.asiainfo.hbase.impl.ContextNameCreator;

@HBaseTable(name = "test_user", nameCreator = ContextNameCreator.class, autoCreate = false, families = { "cf" })
public class User extends Base {
    private static final long serialVersionUID = -296221253940505599L;
    @HRowkey
    private String rowkey;
    @HColumn
    private String name;
    @HColumn(family="0",key="01")
    private String desc;
    @HColumn
    private int age;
    @HColumn
    private String sex;

    public User()
    {

    }
    public String getRowkey() {
        return rowkey;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "rowkey='" + rowkey + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }

}
