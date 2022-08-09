package com.riven.core.service.impl;
import com.riven.core.dao.BaseDao;
import com.riven.core.service.BaseService;
import com.riven.util.bean.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<T, D extends BaseDao<T, Serializable>> implements InitializingBean, BaseService<T, D> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected D baseDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 根据M,反射获得符合M类型的manager
        List<Field> fields = BeanUtils.getFieldsByType(this,
                BeanUtils.getSuperClassGenericType(getClass(), 1));
        Assert.isTrue(fields.size() == 1,
                "subclass's has not only one entity manager property.");
        try {
            baseDao = (
                    D) BeanUtils.forceGetProperty(this, fields.get(0)
                    .getName());
            Assert.notNull(baseDao,
                    "subclass not inject manager to action successful.");
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
    }

    @Override
    public void  save(T entity) {
        baseDao.save(entity);
    }

    @Override
    public T get(Serializable id) {
        Optional<T> entity = baseDao.findById(id);
        if(entity.isPresent()){
            return entity.get();
        }else{
            return null;
        }
    }

    public D getDao() {
        return baseDao;
    }

    @Override
    public void update(T entity) {
        baseDao.save(entity);
    }

    @Override
    public void delete(Serializable id) {
        baseDao.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Override
    public void delete(Serializable[] ids) {
        for(Serializable id : ids){
            delete(id);
        }
    }

    @Override
    public List<T> findAll() {
        return baseDao.findAll();
    }

}
