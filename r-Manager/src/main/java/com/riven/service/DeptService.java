package com.riven.service;

import com.riven.core.service.BaseService;
import com.riven.dao.DeptDao;
import com.riven.model.Dept;
import org.springframework.stereotype.Service;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */

public interface DeptService extends BaseService<Dept, DeptDao> {
    /**
     * 判断部门是否已存在
     * @param dept
     * @return
     */
    public String checkDeptNameUnique(Dept dept);

    /**
     * 新增部门
     * @param dept
     * @return
     */
    public Boolean insertDept(Dept dept);

    /**
     * 是否含有下级部门
     * @param deptId
     * @return
     */
    public Boolean hasChildDept(Long deptId);

    /**
     * 逻辑删除
     * @param deptId
     * @return
     */
    public Integer logicDelete(Long deptId);
}
