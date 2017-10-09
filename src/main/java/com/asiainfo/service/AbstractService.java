package com.asiainfo.service;


import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.asiainfo.base.mapper.BaseMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * Service接口的实现
 */
public abstract class AbstractService<T> implements BaseService<T> {

    @Autowired
    protected BaseMapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public int save(T model) {
        return mapper.save(model);
    }

    public int saveList(List<T> models) {
    	return mapper.saveList(models);
    }

    public int deleteByMap(Map<String,String> paramMap) {
    	return mapper.deleteByMap(paramMap);
    }

    public int deleteById(String id) {
    	return mapper.deleteById(id);
    }

    public int update(T model) {
    	return mapper.update(model);
    }

    public T findById(String id) {
        return mapper.findById(id);
    }

    @Override
    public T findByObject(T model) throws TooManyResultsException {
        return mapper.findByObject(model);
    }

    public List<T> findByIds(String ids) {
        return mapper.findByIds(ids);
    }

    public List<T> findListByMap(Map<String,String> paramMap) {
        return mapper.findListByMap(paramMap);
    }

    public List<T> findAll() {
        return mapper.findAll();
    }
}
