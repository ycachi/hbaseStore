package com.asiainfo.hbase.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HBase连接配置标注。
 * @author
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface HConnectionConfig {
    /**
     * instance name.
     * @return
     */
    String value() default "";

}
