package com.riven.dao;

import com.riven.core.dao.BaseDao;
import com.riven.model.Dept;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/9
 */
public interface DeptDao extends BaseDao<Dept,Serializable> {
    @Query(value = "select * from Z_Dept d where d.dept_Name=:deptName and d.parent_Id=:parentId limit 1",nativeQuery = true)
    Dept checkDeptNameUnique(String deptName,Long parentId);

    Dept findByDeptId(Long deptId);

    @Query(value = "select count(d) from Dept d where d.parentId=:deptId and d.delFlag='0'")
    Integer findByParentId(Long deptId);

    //返回影响的行数
    @Modifying
    @Query(value = "update Dept d set d.delFlag='1' where d.deptId=:deptId")
    Integer logicDelete(Long deptId);
}
