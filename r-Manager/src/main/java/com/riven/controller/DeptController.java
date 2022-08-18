package com.riven.controller;
import com.riven.constant.UserConstants;
import com.riven.core.controller.BaseController;
import com.riven.model.Dept;
import com.riven.service.DeptService;
import com.riven.util.AjaxResult;
import com.riven.util.SecurityUtils;
import com.riven.util.SnowflakeIdWorker;
import com.riven.util.StringUtils;
import com.riven.util.poi.ExcelUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    @PostMapping
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
    @PostMapping("/updateTest")
    public AjaxResult updateTest(Dept dept){
        return toAjax(deptService.updateTest(dept));
    }

    /**
     * 查询登录用户所在机构的部门信息
     * @param dept
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(Dept dept){
        //当前登录用户所在的机构
        dept.setOrgId(getOrgId());
        List<Dept> list = deptService.list(dept);
        return AjaxResult.success(list);
    }

    /**
     * 构建前端所需要的树结构
     * @param dept
     * @return
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(Dept dept){
        dept.setOrgId(getOrgId());
        List<Dept> list = deptService.list(dept);
        return AjaxResult.success(deptService.buildDeptTree(list));
    }

    /**
     * 查询部门列表，清除本身及自身的子树
     * @param deptId
     * @return
     */
    @GetMapping("/exclude/{deptId}")
    public AjaxResult listDeptExcludeChild(@PathVariable Long deptId){
        Dept dept=new Dept();
        dept.setOrgId(getOrgId());
        List<Dept> list = deptService.list(dept);
        list.removeIf(dept1 -> dept1.getDeptId().equals(deptId) || ArrayUtils.contains(StringUtils.split(dept1.getAncestors(),","),deptId.toString()));
        return AjaxResult.success(list);
    }

    /**
     * 导出数据
     * @param dept
     * @param response
     */
    @PostMapping("/export")
    public void export(Dept dept, HttpServletResponse response){
        dept.setOrgId(getOrgId());
        List<Dept> list = deptService.list(dept);
        ExcelUtil<Dept> deptExcelUtil = new ExcelUtil<>(Dept.class);
        deptExcelUtil.exportExcel(response,list,"部门信息");
    }

    /**
     * 导出模板
     * @param response
     */
    @PostMapping("/template")
    public void exportTemplate(HttpServletResponse response){
        ExcelUtil<Dept> deptExcelUtil = new ExcelUtil<>(Dept.class);
        deptExcelUtil.importTemplateExcel(response,"模板");
    }

    /**
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public AjaxResult importDept(MultipartFile file,Boolean updateParam) throws Exception {
        ExcelUtil<Dept> deptExcelUtil = new ExcelUtil<>(Dept.class);
        List<Dept> deptList = deptExcelUtil.importExcel(file.getInputStream());
        deptService.insertList(deptList,updateParam);
        return AjaxResult.success();
    }

}
