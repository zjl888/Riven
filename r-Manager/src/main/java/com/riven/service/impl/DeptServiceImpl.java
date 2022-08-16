package com.riven.service.impl;

import com.riven.constant.UserConstants;
import com.riven.core.service.impl.BaseServiceImpl;
import com.riven.dao.DeptDao;
import com.riven.exception.ServiceException;
import com.riven.model.Dept;
import com.riven.service.DeptService;
import com.riven.util.SecurityUtils;
import com.riven.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */
@Service
public class DeptServiceImpl extends BaseServiceImpl<Dept, DeptDao> implements DeptService {
    @Resource
    private DeptDao deptDao;

    /**
     * 判断部门名称是否已存在
     * @param dept
     * @return
     */
    @Override
    public String checkDeptNameUnique(Dept dept) {
        //id为null为新增，id不为null通过excel导入数据或者修改数据
        Long deptId=StringUtils.isNull(dept.getDeptId())?-1L:dept.getDeptId();
        //根据deptName，parentId来查找库中的数据
        Dept dept1 = deptDao.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        //是否查出数据，若查出数据，在excel导入时，或者更新数据时，其id是否与导入的id相等
        if (StringUtils.isNotNull(dept1)&&dept1.getDeptId().longValue()!=deptId){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增部门
     * @param dept
     * @return
     */
    @Transactional
    @Override
    public Boolean insertDept(Dept dept) {
        Dept dept1 = deptDao.findByDeptId(dept.getParentId());
        //若存在父级部门
        if (StringUtils.isNotNull(dept1)){
            //判断部门是否停用
            if (UserConstants.DEPT_DISABLE.equals(dept1.getStatus())){
                throw new ServiceException("部门停用，不允许新增");
            }
            dept.setAncestors(dept1.getAncestors()+","+dept.getParentId());
        }else {
            dept.setAncestors("0");
        }
        dept.setCreateTime(new Date());
        return StringUtils.isNotNull(deptDao.save(dept))?true:false;
    }

    /**
     * 是否含有下级部门
     * @param deptId
     * @return
     */
    @Override
    public Boolean hasChildDept(Long deptId) {
        return deptDao.findByParentId(deptId)>0?true:false;
    }

    /**
     * 逻辑删除
     * @param deptId
     * @return
     */
    @Transactional
    @Override
    public Integer logicDelete(Long deptId) {
        return deptDao.logicDelete(deptId);
    }

    /**
     * 是否含有未停用的下级部门
     * @param deptId
     * @return
     */
    @Override
    public Integer hasNormalChildDept(Long deptId) {
        return deptDao.hasNormalChildDept(deptId);
    }

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @Transactional
    @Override
    public Integer updateDept(Dept dept) {
        Dept dept1 = deptDao.findByDeptId(dept.getParentId());
        //如果存在父级部门，修改祖级列表
        if (StringUtils.isNotNull(dept1)){
            String ancestors = dept.getAncestors();
            String newAncestors=ancestors+","+dept.getDeptId();
            String oldAncestors=deptDao.findByDeptId(dept.getDeptId()).getAncestors();
            dept.setAncestors(newAncestors);
            //修改其所有子部门的组级列表
            updateChildAncestors(dept.getDeptId(),newAncestors,oldAncestors);
        }
        dept.setUpdateTime(new Date());
        Dept save = deptDao.save(dept);
        //若该部门为启用状态，则启用其所有父级部门
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())){
            updateAncestorsDeptStatus(dept);
        }
        return StringUtils.isNotNull(save)?1:0;
    }

    /**
     * 更新子部门的祖级列表
     * @param deptId
     * @param newAncestors
     * @param oldAncestors
     */
    @Override
    public void updateChildAncestors(Long deptId, String newAncestors, String oldAncestors) {
        //查找所有子部门
        List<Dept> children = deptDao.selectChildDept(deptId);
        for (Dept child:children
             ) {
            //只替换祖籍列表中发生变更的一段
            child.setAncestors(child.getAncestors().replace(oldAncestors,newAncestors));
            child.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            child.setUpdateTime(new Date());
        }
        deptDao.saveAllAndFlush(children);
    }

    /**
     * 更改部门的所有的上级部门为启用状态
     * @param dept
     */
    @Override
    public void updateAncestorsDeptStatus(Dept dept) {
        String ancestors = dept.getAncestors();
        String[] split = StringUtils.split(ancestors,",");
        Dept parent=new Dept();
        for (String str:split
             ) {
            //Long.valueOf返回包装类型Long
            //Long.parseLong返回基本数据类型long
            parent.setDeptId(Long.valueOf(str));
            parent.setUpdateTime(new Date());
            parent.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            parent.setStatus("1");
            deptDao.updateAncestorsDeptStatus(dept);
        }
    }

    /**
     * 根据deptId查询部门的详细信息
     * @param deptId
     * @return
     */
    @Override
    public Dept selectByDeptId(Long deptId) {
        return deptDao.findByDeptId(deptId);
    }

    @Transactional
    @Override
    public Integer updateTest(Dept dept) {
        //持久态
        Dept dept1=deptDao.findByDeptId(dept.getDeptId());
        //与库中数据对比，不同则更新
        dept1.setUpdateTime(new Date());
        dept1.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        return 1;
    }
}
