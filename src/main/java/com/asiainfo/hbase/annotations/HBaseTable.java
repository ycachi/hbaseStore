package com.asiainfo.hbase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义HBase表。
 * @author
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HBaseTable {
    /**
     * HTable表名。
     * @return
     */
    String name();

    /**
     * 动态表名创建器。
     */
    Class<?> nameCreator() default Void.class;

    /**
     * 是否在表不存在的时候自动创建表。
     * @return
     */
    boolean autoCreate() default false;

    /**
     * 需要自动创建表时，列族的定义。
     * @return
     */
    String[] families() default { "f" };
}
