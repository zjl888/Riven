package com.riven.core.dao;
import com.riven.core.page.PageDomain;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, ID> {
    T get(String hql, Map<String, Object> params);

    List<T> list(String hql, Map<String, Object> params, PageDomain page);

    List<T> list(String hql, Map<String, Object> params, int offset, int rows);

    List<T> list(String hql, Map<String, Object> params);

    <O> List<O> listObject(String hql, Map<String, Object> params, Class clazz);

    Long count(String hql, Map<String, Object> params);


    T getBySql(String sql, Map<String, Object> params);

    List<T> listBySql(String sql, Map<String, Object> params, PageDomain page);

    List<T> listBySql(String sql, Map<String, Object> params, int offset, int rows);

    List<T> listBySql(String sql, Map<String, Object> params);

    List<Map> listMapBySql(String sql);

    List<Map> listMapBySql(String sql, Map<String, Object> params);

    List<Map> listMapBySql(String sql, Map<String, Object> params, PageDomain page);

    List<Map> listMapBySqlLowercaseKey(String sql, Map<String, Object> params);

    List<Map> listMapBySql(String hql, Map<String, Object> params, int offset, int rows);


    <O> List<O> listObjectBySql(String sql, Map<String, Object> params, Class<O> clazz);

    Long countBySql(String sql, Map<String, Object> params);

    List<T> findByAttributeContainsText(String attributeName, String text);

    List<T> findAll(Sort sort);
}