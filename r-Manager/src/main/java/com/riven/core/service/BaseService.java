package com.riven.core.service;
import com.riven.core.dao.BaseDao;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, D extends BaseDao<T, Serializable>> {

    /**
     * 保存对象
     *
     * @param entity
     */
    void save(T entity);

    /**
     * 根据id得到对象
     *
     * @param id
     * @return
     */
    T get(Serializable id);

    /**
     * 更新对象
     *
     * @param entity
     */
    void update(T entity);

    void delete(Serializable id);

    void delete(T entity);

    /**
     * 批量删除
     *
     * @param ids 需要删除的字典ID
     * @return 结果
     */
    void delete(Serializable[] ids);

    List<T> findAll();
}