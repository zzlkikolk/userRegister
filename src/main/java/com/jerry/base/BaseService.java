package com.jerry.base;

import java.io.Serializable;
import java.util.List;

/**
 * 通用业务方法接口
 * @author zhangzhilin
 * @param <T> 返回类型
 */
public interface BaseService<T> {
    //普通列表
    List<T> list(T entity);
    //保存
    T save(T entity);
    //删除
    void removeById(Serializable id);
    //获取单个
    T getOne(Serializable id);
}
