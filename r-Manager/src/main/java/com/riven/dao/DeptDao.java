package com.riven.dao;

import com.riven.core.dao.BaseDao;
import com.riven.model.Dept;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/9
 */
public interface DeptDao extends BaseDao<Dept,Serializable> {
    /**
     * 部门名称是否已存在
     * @param deptName
     * @param parentId
     * @return
     */
    @Query(value = "select * from Z_Dept d where d.dept_Name=:deptName and d.parent_Id=:parentId limit 1",nativeQuery = true)
    Dept checkDeptNameUnique(String deptName,Long parentId);

    /**
     * 根据deptId查找部门
     * @param deptId
     * @return
     */
    Dept findByDeptId(Long deptId);

    /**
     * 查找该部门的子部门
     * @param deptId
     * @return
     */
    @Query(value = "select count(d) from Dept d where d.parentId=:deptId and d.delFlag='0'")
    Integer findByParentId(Long deptId);

    /**
     * 逻辑删除
     * @param deptId
     * @return
     */
    //返回影响的行数
    @Modifying
    @Query(value = "update Dept d set d.delFlag='1' where d.deptId=:deptId")
    Integer logicDelete(Long deptId);

    /**
     * 查找所有的未停用的子级部门
     * @param deptId
     * @return
     */
    //FIND_IN_SET(str,strlist),str是否存在于strlist当中，不存在返回0
    //存在，例如（‘1’，‘3，2，1’）返回3，即str在strlist中的位置。
    @Query(value = "select count(*) from Z_Dept  where status=0 and del_Flag=0 and FIND_IN_SET(dept_Id=:deptId,ancestors)",nativeQuery = true)
    Integer hasNormalChildDept(Long deptId);

    /**
     * 查找所有子级部门
     * @param deptId
     * @return
     */
    @Query(value = "select * from Z_DEPT where FIND_IN_SET(dept_Id=:deptId,ancestors)",nativeQuery = true)
    List<Dept> selectChildDept(Long deptId);

    /**
     * 启用部门
     * @param dept
     */
    @Modifying
    @Query(value = "update z_dept set status=:#{#dept.status},update_Time=:#{#dept.updateTime},update_By=:#{#dept.updateBy} where dept_Id=:#{#dept.deptId}",nativeQuery = true)
    void updateAncestorsDeptStatus(@Param("dept") Dept dept);
}
