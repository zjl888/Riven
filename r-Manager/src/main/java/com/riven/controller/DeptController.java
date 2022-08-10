package com.riven.controller;

import com.riven.constant.UserConstants;
import com.riven.core.controller.BaseController;
import com.riven.dao.DeptDao;
import com.riven.model.Dept;
import com.riven.service.DeptService;
import com.riven.util.AjaxResult;
import com.riven.util.SnowflakeIdWorker;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author :zhujl
 * @date : 2022/8/10
 */
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController {
    @Resource
    private DeptService deptService;

    /**
     * 新增部门
     * @param dept
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Dept dept){
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))){
            return AjaxResult.error("新增"+dept.getDeptName()+"失败，该部门已存在");
        }
        dept.setDeptId(SnowflakeIdWorker.idWorker.nextId());
        dept.setCreateBy(getLoginUser().getUsername());
        if (dept.getOrderNum()==null){
            dept.setOrderNum(0);
        }
        dept.setDelFlag("0");
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 逻辑删除
     * @param deptId
     * @return
     */
    @DeleteMapping("/{deptId}")
    public AjaxResult delete(@PathVariable Long deptId){
        if (deptService.hasChildDept(deptId)){
            return AjaxResult.error("存在下级部门，无法直接删除!");
        }
        return toAjax(deptService.logicDelete(deptId));
    }
    @PutMapping
    public AjaxResult update(@RequestBody Dept dept){
        return AjaxResult.success();
    }
}
