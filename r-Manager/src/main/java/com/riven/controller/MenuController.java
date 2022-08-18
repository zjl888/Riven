package com.riven.controller;

import com.riven.constant.UserConstants;
import com.riven.core.controller.BaseController;
import com.riven.model.Menu;
import com.riven.service.menuService;
import com.riven.util.AjaxResult;
import com.riven.util.SnowflakeIdWorker;
import com.riven.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/16
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Resource
    private menuService menuService;
    @RequestMapping("/list")
    public AjaxResult list(Menu menu){
        List<Menu> menus = menuService.selectAll(menu, getLoginUser().getUserId());
        return AjaxResult.success(menus);
    }
    @PostMapping
    public AjaxResult addMenu(@RequestBody Menu menu){
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))){
            return AjaxResult.error("添加菜单'"+menu.getMenuName()+"'失败，该菜单已存在");
        }else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())&&StringUtils.ishttp(menu.getPath())){
            return AjaxResult.error("添加菜单'"+menu.getMenuName()+"'失败,地址必须以http(s)://开头");
        }
        menu.setId(SnowflakeIdWorker.idWorker.nextId());
        menu.setCreateBy(getUsername());
        if (menu.getOrderNum()==null){
            menu.setOrderNum(0);
        }
        return toAjax(menuService.addMenu(menu));
    }

}
