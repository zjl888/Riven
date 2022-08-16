package com.riven.service;

import com.riven.core.service.BaseService;
import com.riven.dao.MenuDao;
import com.riven.model.Menu;
import com.riven.model.vo.RouterVo;

import java.util.List;
import java.util.Map;

/**
 * @author :zhujl
 * @date : 2022/8/15
 */
public interface menuService extends BaseService<Menu, MenuDao> {
    /**
     * 根据用户id构建菜单树
     * @param userId
     * @return
     */
    List<Menu> selectMenuTreeById(Long userId);

    /**
     * 构建菜单树
     * @param list
     * @return
     */
    List<Menu> createMenuTree(List<Menu> list);

    /**
     * 递归构建menu的子树
     * @param list
     * @param menu
     * @return
     */
    void createTopChild(List<Menu> list,Menu menu);

    /**
     * 获取节点menu的子节点
     * @param menu
     * @return
     */
    List<Menu> createChildren(List<Menu> list,Menu menu);

    /**
     * 判断是否有子节点
     * @param list
     * @param menu
     * @return
     */
    Boolean hasChild(List<Menu> list,Menu menu);
    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<Menu> menus);

    /**
     * 获取该用户权限下所有菜单列表
     * @param menu
     * @param userId
     * @return
     */
    public List<Menu> selectAll(Menu menu,Long userId);

    /**
     * 构建sql语句
     * @param menu
     * @param userId
     * @param params
     * @param orderNum
     * @return
     */
    public String buildSql(Menu menu, Long userId, Map<String,Object> params,Boolean orderNum);

    /**
     * 检测菜单名称是否唯一
     * @param menu
     * @return
     */
    public String checkMenuNameUnique(Menu menu);

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    public Integer addMenu(Menu menu);

}
