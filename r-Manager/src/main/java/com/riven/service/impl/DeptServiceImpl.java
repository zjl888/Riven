package com.riven.service.impl;

import com.riven.constant.UserConstants;
import com.riven.core.service.impl.BaseServiceImpl;
import com.riven.dao.DeptDao;
import com.riven.exception.ServiceException;
import com.riven.model.Dept;
import com.riven.service.DeptService;
import com.riven.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */
@Service
public class DeptServiceImpl extends BaseServiceImpl<Dept, DeptDao> implements DeptService {
    @Resource
    private DeptDao deptDao;

    @Override
    public String checkDeptNameUnique(Dept dept) {
        //id为null为新增，id不为null通过excel导入数据
        Long deptId=StringUtils.isNull(dept.getDeptId())?-1L:dept.getDeptId();
        //根据deptName，parentId来查找库中的数据
        Dept dept1 = deptDao.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        //是否查出数据，若查出数据，在excel导入时，其id是否与导入的id相等
        if (StringUtils.isNotNull(dept1)&&dept1.getDeptId().longValue()!=deptId){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
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

    @Override
    public Boolean hasChildDept(Long deptId) {
        return deptDao.findByParentId(deptId)>0?true:false;
    }

    @Transactional
    @Override
    public Integer logicDelete(Long deptId) {
        return deptDao.logicDelete(deptId);
    }
}
