package com.riven.core.dao.impl;
import com.riven.core.dao.BaseDao;
import com.riven.core.page.PageDomain;
import com.riven.util.AliasToBeanResultTransformerPlus;
import com.riven.util.AliasToEntityMapResultLowercaseKeyTransformer;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author:Suhail
 * @Date:create in 2018/8/21
 */
@NoRepositoryBean
public class BaseDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    @PersistenceContext
    private EntityManager em;

    public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    public BaseDaoImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }

    @Override
    public T get(String hql, Map<String, Object> params) {
        Query q = em.createQuery(hql);
        setParams(q, params);
        List<T> l = q.getResultList();
        if (l != null && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    @Override
    public List<T> list(String hql, Map<String, Object> params, PageDomain page) {
        if (page.getPageNum() == null || page.getPageSize() == null) {
            return this.list(hql, params);
        }
        return this.list(hql, params, (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
    }

    @Override
    public List<T> list(String hql, Map<String, Object> params, int offset, int rows) {
        Query q = em.createQuery(hql);
        setParams(q, params);
        q.setFirstResult(offset).setMaxResults(rows);
        return q.getResultList();
    }

    @Override
    public List<T> list(String hql, Map<String, Object> params) {
        Query q = em.createQuery(hql);
        setParams(q, params);
        return q.getResultList();
    }

    @Override
    public <O> List<O> listObject(String hql, Map<String, Object> params, Class clazz) {
        Query q = em.createQuery(hql);
        setParams(q, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(new AliasToBeanResultTransformer(clazz));
        return q.getResultList();
    }

    @Override
    public Long count(String hql, Map<String, Object> params) {
        if (!hql.toLowerCase().trim().startsWith("select")) {
            hql = "select count(*) " + hql;
        }
        Query q = em.createQuery(hql);
        setParams(q, params);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public T getBySql(String sql, Map<String, Object> params) {
        Query q = em.createNativeQuery(sql, getDomainClass());
        setParams(q, params);
        List<T> l = q.getResultList();
        if (l != null && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    @Override
    public List<T> listBySql(String sql, Map<String, Object> params, PageDomain page) {
        if (page.getPageNum() == null || page.getPageSize() == null) {
            return this.listBySql(sql, params);
        }
        return this.listBySql(sql, params, (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
    }

    @Override
    public List<T> listBySql(String sql, Map<String, Object> params, int offset, int rows) {
        Query q = em.createNativeQuery(sql, getDomainClass());
        setParams(q, params);
        q.setFirstResult(offset).setMaxResults(rows);
        return q.getResultList();
    }

    @Override
    public List<T> listBySql(String sql, Map<String, Object> params) {
        Query q = em.createNativeQuery(sql, getDomainClass());
        setParams(q, params);
        return q.getResultList();
    }

    @Override
    public List<Map> listMapBySql(String sql) {
        return this.listMapBySql(sql, new HashMap<>());
    }

    @Override
    public List<Map> listMapBySql(String sql, Map<String, Object> params) {
        Query q = em.createNativeQuery(sql);
        setParams(q, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    public List<Map> listMapBySql(String sql, Map<String, Object> params, PageDomain page) {
        if (page.getPageNum() == null || page.getPageSize() == null) {
            return this.listMapBySql(sql, params);
        }
        return this.listMapBySql(sql, params, (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
    }

    @Override
    public List<Map> listMapBySqlLowercaseKey(String sql, Map<String, Object> params) {
        Query q = em.createNativeQuery(sql);
        setParams(q, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultLowercaseKeyTransformer.INSTANCE);
        return q.getResultList();
    }

    @Override
    public List<Map> listMapBySql(String hql, Map<String, Object> params, int offset, int rows) {
        Query q = em.createNativeQuery(hql);
        setParams(q, params);
        q.setFirstResult(offset).setMaxResults(rows);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    public <O> List<O> listObjectBySql(String sql, Map<String, Object> params, Class<O> clazz) {

        Query q = em.createNativeQuery(sql);
        setParams(q, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(new AliasToBeanResultTransformerPlus(clazz));
        return q.getResultList();
    }

    @Override
    public Long countBySql(String sql, Map<String, Object> params) {
        Query q = em.createNativeQuery(sql);
        setParams(q, params);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return this.findAll(sort);
    }

    private void setParams(Query q, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                q.setParameter(key, obj);
            }
        }
    }

    private void setParams(Query q, Object... params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i, params[i]);
            }
        }
    }


    @Override
    public List<T> findByAttributeContainsText(String attributeName, String text) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root).where(builder.like(root.<String>get(attributeName), "%" + text + "%"));
        TypedQuery<T> query = em.createQuery(cQuery);
        return query.getResultList();
    }
}
