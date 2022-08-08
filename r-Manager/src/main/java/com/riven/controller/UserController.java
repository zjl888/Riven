package com.riven.controller;
import com.riven.constant.Constants;
import com.riven.model.LoginUser;
import com.riven.model.Test;
import com.riven.model.User;
import com.riven.service.RoleMenuService;
import com.riven.service.UserService;
import com.riven.util.AjaxResult;
import com.riven.util.SecurityUtils;
import com.riven.util.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RoleMenuService roleMenuService;

    @PreAuthorize("@ss.hasPermi('system:user:select')")
    @PostMapping(value = "/select")
    public AjaxResult select(@RequestBody String username){
        User user = userService.select(username);
        AjaxResult ajaxResult=new AjaxResult();
        ajaxResult.put(Constants.SUCCESS,user);
        return ajaxResult;
    }
    @GetMapping("/getInfo")
    public AjaxResult getInfo(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Set<String> permissions = roleMenuService.selectPermissions(loginUser.getUser().getId());
        AjaxResult ajaxResult=new AjaxResult();
        ajaxResult.put("permissions",permissions);
        return ajaxResult;
    }
    @PostMapping("/export")
    public void export(HttpServletResponse response){
        List<User> list=userService.selectAll();
        ExcelUtil<User> excelUtil=new ExcelUtil<User>(User.class);
        excelUtil.exportExcel(response,list,"用户信息");
    }
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Test> excelUtil=new ExcelUtil<Test>(Test.class);
        List<Test> testList = excelUtil.importExcel(file.getInputStream());
        userService.insert(testList);
        return AjaxResult.success("导入成功");
    }
    @PostMapping("/template")
    public void template(HttpServletResponse response){
        ExcelUtil<Test> excelUtil=new ExcelUtil<>(Test.class);
        excelUtil.importTemplateExcel(response,"模板");
    }

}
