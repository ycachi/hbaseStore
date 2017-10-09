package com.asiainfo.hbase.util;

import java.util.Date;
import java.util.Map;

import com.asiainfo.hbase.BytesConvertable;
import org.apache.hadoop.hbase.util.Bytes;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import static com.google.common.collect.Maps.*;

public class Types {
    private static Map<Class<?>, Class<?>> primitiveMap = newHashMap();
    private static Map<Class<?>, BytesConvertable<?>> basicConverters = newHashMap();
    private static Map<Class<?>, Integer> bytesLenMap = newHashMap();
    private static BytesConvertable<Object> jsonConverter = new BytesConvertable<Object>() {

        public byte[] toBytes(Object object) {
            String jsonString = JSON.toJSONString(object, SerializerFeature.WriteClassName);
            return Bytes.toBytes(jsonString);
        }

        public Object fromBytes(byte[] bytes) {
            String jsonString = Bytes.toString(bytes);
            return JSON.parse(jsonString);
        }

    };
    static {
        bytesLenMap.put(Boolean.class, Bytes.SIZEOF_BOOLEAN);
        bytesLenMap.put(Byte.class, 1);
        bytesLenMap.put(Character.class, 1);
        bytesLenMap.put(Short.class, Bytes.SIZEOF_SHORT);
        bytesLenMap.put(Integer.class, Bytes.SIZEOF_INT);
        bytesLenMap.put(Long.class, Bytes.SIZEOF_LONG);
        bytesLenMap.put(Float.class, Bytes.SIZEOF_FLOAT);
        bytesLenMap.put(Double.class, Bytes.SIZEOF_DOUBLE);
        bytesLenMap.put(Date.class, Bytes.SIZEOF_LONG);

        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(char.class, Character.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);

        basicConverters.put(Boolean.class, new BytesConvertable<Boolean>() {
            public byte[] toBytes(Boolean object) {
                return Bytes.toBytes(object.booleanValue());
            }

            public Boolean fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_BOOLEAN ? Bytes.toBoolean(bytes) : null;
            }
        });
        basicConverters.put(Byte.class, new BytesConvertable<Byte>() {
            public byte[] toBytes(Byte object) {
                return new byte[] { object.byteValue() };
            }

            public Byte fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_BYTE ? bytes[0] : null;
            }
        });

        basicConverters.put(Character.class, new BytesConvertable<Character>() {
            public byte[] toBytes(Character object) {
                return Bytes.toBytes(new String(new char[] { object.charValue() }));
            }

            public Character fromBytes(byte[] bytes) {
                return bytes.length == 1 ? Bytes.toString(bytes).charAt(0) : null;
            }
        });

        basicConverters.put(Short.class, new BytesConvertable<Short>() {
            public byte[] toBytes(Short object) {
                return Bytes.toBytes(object.shortValue());
            }

            public Short fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_SHORT ? Bytes.toShort(bytes) : null;
            }
        });
        basicConverters.put(Integer.class, new BytesConvertable<Integer>() {
            public byte[] toBytes(Integer object) {
                return Bytes.toBytes(object.intValue());
            }

            public Integer fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_INT ? Bytes.toInt(bytes) : null;
            }
        });
        basicConverters.put(Long.class, new BytesConvertable<Long>() {
            public byte[] toBytes(Long object) {
                return Bytes.toBytes(object.longValue());
            }

            public Long fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_LONG ? Bytes.toLong(bytes) : null;
            }
        });
        basicConverters.put(Float.class, new BytesConvertable<Float>() {
            public byte[] toBytes(Float object) {
                return Bytes.toBytes(object.floatValue());
            }

            public Float fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_FLOAT ? Bytes.toFloat(bytes) : null;
            }
        });
        basicConverters.put(Double.class, new BytesConvertable<Double>() {
            public byte[] toBytes(Double object) {
                return Bytes.toBytes(object.doubleValue());
            }

            public Double fromBytes(byte[] bytes) {
                return bytes.length == Bytes.SIZEOF_DOUBLE ? Bytes.toDouble(bytes) : null;
            }
        });
        basicConverters.put(String.class, new BytesConvertable<String>() {
            public byte[] toBytes(String object) {
                return Bytes.toBytes(object);
            }

            public String fromBytes(byte[] bytes) {
                return Bytes.toString(bytes);
            }
        });
        basicConverters.put(Date.class, new BytesConvertable<Date>() {
            public byte[] toBytes(Date object) {
                return Bytes.toBytes(object.getTime());
            }

            public Date fromBytes(byte[] bytes) {
                return new Date(Bytes.toLong(bytes));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] toBytes(T value) {
        if (value == null) return null;

        if (value instanceof byte[]) return (byte[]) value;

        Class<?> valueClass = value.getClass();

        BytesConvertable<?> bytesConvertable = basicConverters.get(valueClass);
        if (bytesConvertable == null && value instanceof BytesConvertable) {
            bytesConvertable = (BytesConvertable<?>) value;
        }
        if (bytesConvertable == null) {
            bytesConvertable = jsonConverter;
        }

        return ((BytesConvertable<T>) bytesConvertable).toBytes(value);
    }

    public static <T> int getBytesLen(Class<T> type) {
        Class<?> targetType = type;
        if (targetType.isPrimitive()) targetType = primitiveMap.get(targetType);

        Integer bytesLen = bytesLenMap.get(targetType);

        return bytesLen != null ? bytesLen.intValue() : -1;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromBytes(byte[] bytes, Class<T> type) {
        if (bytes == null) { return null; }

        if (type == byte[].class) { return (T) bytes; }

        Class<?> targetType = type;
        if (targetType.isPrimitive()) {
            targetType = primitiveMap.get(targetType);
        }

        BytesConvertable<?> bytesConvertable = basicConverters.get(targetType);
        if (bytesConvertable == null && BytesConvertable.class.isAssignableFrom(type)) {
            bytesConvertable = (BytesConvertable<?>) Clazz.newInstance(type);
        }
        if (bytesConvertable == null) {
            bytesConvertable = jsonConverter;
        }

        return (T) bytesConvertable.fromBytes(bytes);
    }

}
