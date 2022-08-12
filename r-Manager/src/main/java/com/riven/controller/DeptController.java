package com.riven.controller;
import com.riven.constant.UserConstants;
import com.riven.core.controller.BaseController;
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

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @PutMapping
    public AjaxResult update(@RequestBody Dept dept){
        if(UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))){
            return AjaxResult.error("修改"+dept.getDeptName()+"部门信息失败，部门名称已存在");
        }else if (dept.getDeptId().equals(dept.getParentId())){
            return AjaxResult.error("修改"+dept.getDeptName()+"部门信息失败，上级部门不能为自身");
        }else if (UserConstants.DEPT_DISABLE.equals(dept.getStatus())&&deptService.hasNormalChildDept(dept.getDeptId())>0){
            return AjaxResult.error("修改"+dept.getDeptName()+"部门信息失败，包含未停用的子部门");
        }
        dept.setUpdateBy(getLoginUser().getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 根据deptId获取部门详细信息
     * @param deptId
     * @return
     */
    @GetMapping("/{deptId}")
    public AjaxResult selectByDeptId(@PathVariable Long deptId){
        return AjaxResult.success(deptService.selectByDeptId(deptId));
    }
}
