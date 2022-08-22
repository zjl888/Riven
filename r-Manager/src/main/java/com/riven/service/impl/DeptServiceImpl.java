package com.riven.service.impl;

import com.riven.constant.UserConstants;
import com.riven.core.service.impl.BaseServiceImpl;
import com.riven.dao.DeptDao;
import com.riven.exception.ServiceException;
import com.riven.model.Dept;
import com.riven.service.DeptService;
import com.riven.util.SecurityUtils;
import com.riven.util.SnowflakeIdWorker;
import com.riven.util.StringUtils;
import com.riven.util.bean.BeanValidators;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Validator;
import java.util.*;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */
@Service
public class DeptServiceImpl extends BaseServiceImpl<Dept, DeptDao> implements DeptService {
    @Resource
    private DeptDao deptDao;
    @Resource
    private Validator validator;

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

    @Override
    public List<Dept> list(Dept dept) {
        StringBuilder sql=new StringBuilder("select * from z_dept where del_flag='0' ");
        Map<String,Object> params=new HashMap<>();
        if (StringUtils.isNotNull(dept.getOrgId())){
            sql.append("and org_id=:orgId ");
            params.put("orgId",dept.getOrgId());
        }
        if (StringUtils.isNotNull(dept.getDeptName())){
            sql.append("and dept_name=:deptName ");
            params.put("deptName",dept.getDeptName());
        }
        if (StringUtils.isNotNull(dept.getStatus())){
            sql.append("and status=:status ");
            params.put("status",dept.getStatus());
        }
        sql.append("order by parent_id,order_num");
        return deptDao.listBySql(sql.toString(),params);
    }

    @Override
    public List<Dept> buildDeptTree(List<Dept> list) {
        ArrayList<Dept> returnList = new ArrayList<>();
        for (Dept dept:list
             ) {
            if (dept.getParentId()==0){
                returnList.add(dept);
            }
            buildChildTree(list,dept);
        }
        return returnList;
    }

    @Override
    public void buildChildTree(List<Dept> list, Dept dept) {
        List<Dept> deptList = buildChild(list, dept);
        dept.setChildren(deptList);
        for (Dept dept1:deptList
             ) {
            if (StringUtils.isNotNull(deptDao.selectChildUnique(dept1.getDeptId()))){
                buildChild(list,dept1);
            }
        }
    }

    @Override
    public List<Dept> buildChild(List<Dept> list, Dept dept) {
        ArrayList<Dept> deptList = new ArrayList<>();
        for (Dept dept1:list
             ) {
            if (dept.getDeptId().longValue()==dept1.getParentId()){
                deptList.add(dept1);
            }
        }
        return deptList;
    }
    @Transactional
    @Override
    public String insertList(List<Dept> deptList,Boolean updateParam) {
        if (StringUtils.isEmpty(deptList)||deptList.size()==0){
            throw new ServiceException("导入数据不能为空");
        }
        int successfulNum=0;
        int failureNum=0;
        StringBuilder successMsg=new StringBuilder();
        StringBuilder failureMsg=new StringBuilder();
        for (Dept dept:deptList
             ) {
            try{
                //excel中是否有部门名称、机构id、父部门id
                if (StringUtils.isNotEmpty(dept.getDeptName())&&StringUtils.isNotNull(dept.getOrgId())&&StringUtils.isNotNull(dept.getParentId())){
                    //同一机构，同一父部门下是否存在同名部门
                    if (StringUtils.isNull(deptDao.selectDept(dept.getDeptName(),dept.getOrgId(),dept.getParentId()))){
                        //不存在，插入信息
                        BeanValidators.validateWithException(validator,dept);
                        dept.setCreateBy(SecurityUtils.getLoginUser().getUsername());
                        dept.setCreateTime(new Date());
                        dept.setDeptId(SnowflakeIdWorker.idWorker.nextId());
                        dept.setDelFlag("0");
                        dept.setAncestors(deptDao.findByDeptId(dept.getParentId()).getAncestors()+","+dept.getParentId());
                        deptDao.save(dept);
                        successfulNum++;
                        successMsg.append("<br/>" + successfulNum + "、部门 " + dept.getDeptName() + " 导入成功");
                    }else if (updateParam){
                        //存在同名部门，更新信息
                        BeanValidators.validateWithException(validator,dept);
                        Dept dept1 = deptDao.findByDeptId(dept.getDeptId());
                        dept1.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
                        dept1.setUpdateTime(new Date());
                        dept1.setOrderNum(dept.getOrderNum());
                        dept1.setOrgId(dept.getOrgId());
                        dept1.setParentId(dept.getParentId());
                        updateDept(dept1);
                        successfulNum++;
                        successMsg.append("<br/>" + successfulNum + "、部门 " + dept.getDeptName() + " 更新成功");
                    }else {
                        //存在，不更新
                        failureNum++;
                        failureMsg.append("<br/>"+ failureNum + "、"+dept.getDeptName()+"插入失败，部门名称已存在");
                    }
                }else {
                    //excel中信息不完整
                    failureNum++;
                    failureMsg.append("<br/>"+ failureNum + "excel中未输入部门名称或者岗位id或者pId，插入失败");
                }
            }catch (Exception e){
                failureNum++;
                failureMsg.append("<br/>" + failureNum + "、部门 " + dept.getDeptName() + " 导入失败："+e.getMessage());
            }
        }
        if (failureNum>0){
            failureMsg.insert(0,"很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successfulNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
