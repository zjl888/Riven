package com.riven.service;

import com.riven.core.service.BaseService;
import com.riven.dao.DeptDao;
import com.riven.model.Dept;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */

public interface DeptService extends BaseService<Dept, DeptDao> {
    /**
     * 判断部门名称是否已存在
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

    /**
     * 是否含有未停用的下级部门
     * @param deptId
     * @return
     */
    public Integer hasNormalChildDept(Long deptId);

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    public Integer updateDept(Dept dept);

    /**
     * 更新子部门的祖级列表
     * @param deptId
     * @param newAncestors
     * @param oldAncestors
     */
    void updateChildAncestors(Long deptId,String newAncestors,String oldAncestors);

    /**
     * 更改部门的所有的上级部门为启用状态
     * @param dept
     */
    void updateAncestorsDeptStatus(Dept dept);

    /**
     * 根据deptId查询部门详细信息
     * @param deptId
     * @return
     */
    Dept selectByDeptId(Long deptId);

    Integer updateTest(Dept dept);

    /**
     * 查询部门列表
     * @param dept
     * @return
     */
    List<Dept> list(Dept dept);

    /**
     * 创建前端需要的树结构
     * @param list
     * @return
     */
    List<Dept> buildDeptTree(List<Dept> list);

    /**
     * 递归构建子树
     * @param list
     * @param dept
     */
    void buildChildTree(List<Dept> list,Dept dept);

    /**
     * 构建传入节点的子节点
     * @param list
     * @param dept
     * @return
     */
    List<Dept> buildChild(List<Dept> list,Dept dept);

    /**
     * 批量插入
     * @param deptList
     */
    void insertList(List<Dept> deptList,Boolean updateParam);
}
