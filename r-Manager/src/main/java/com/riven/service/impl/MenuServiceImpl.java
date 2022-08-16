package com.riven.service.impl;

import com.riven.constant.Constants;
import com.riven.constant.UserConstants;
import com.riven.core.service.impl.BaseServiceImpl;
import com.riven.dao.MenuDao;
import com.riven.model.Menu;
import com.riven.model.vo.MetaVo;
import com.riven.model.vo.RouterVo;
import com.riven.service.menuService;
import com.riven.util.SecurityUtils;
import com.riven.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author :zhujl
 * @date : 2022/8/15
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, MenuDao> implements menuService {
    @Resource
    private MenuDao menuDao;
    @Override
    public List<Menu> selectMenuTreeById(Long userId) {
        List<Menu> menus=new ArrayList<>();
        if (SecurityUtils.isAdmin(userId)){
            menus=menuDao.selectAllMenu();
        }else {
            menus=menuDao.selectMenuByUserId(userId);
        }
        return createMenuTree(menus);
    }

    @Override
    public List<Menu> createMenuTree(List<Menu> list) {
        List<Menu> menuTree=new ArrayList<>();
        //遍历传入的所需要构建的菜单数组
        for (Menu menu:list
             ) {
            //找出最顶层的菜单项，遍历构建各个子树
            if (menu.getParentId()==0){
                createTopChild(list, menu);
                menuTree.add(menu);
            }
        }
        return menuTree;
    }

    @Override
    public void createTopChild(List<Menu> list, Menu menu) {
        //获取节点的子树
        List<Menu> children = createChildren(list, menu);
        menu.setChildren(children);
        //遍历子树的节点，递归创建子树
        for (Menu child:children
             ) {
            //判断其是否含有子节点
            if (hasChild(list,menu)){
                createTopChild(children,child);
            }
        }
    }

    @Override
    public List<Menu> createChildren(List<Menu> list,Menu menu) {
        ArrayList<Menu> children = new ArrayList<>();
        //递归所有节点
        for (Menu m:children
             ) {
            //判断父节点menu是否含有子节点
            if (m.getParentId().longValue()==menu.getId()){
                //将其子节点加入子节点列表
                children.add(m);
            }
        }
        return children;
    }

    @Override
    public Boolean hasChild(List<Menu> list, Menu menu) {
        return StringUtils.isNotEmpty(createChildren(list,menu));
    }

    @Override
    public List<RouterVo> buildMenus(List<Menu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (Menu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<Menu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().longValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<Menu> selectAll(Menu menu, Long userId) {
        StringBuilder sql=new StringBuilder("select distinct m.* ");
        Map<String,Object> params=new HashMap<>();
        String buildSql = buildSql(menu, userId, params, true);
        sql.append(buildSql);
        List<Menu> menus = menuDao.listBySql(sql.toString(), params);
        return menus;
    }

    @Override
    public String buildSql(Menu menu, Long userId, Map<String, Object> params, Boolean orderNum) {
        StringBuilder sql=new StringBuilder("from z_menu m ");
        StringBuilder where=new StringBuilder("where 1=1 ");
        //如果是admin，可以查看所有菜单
        if (SecurityUtils.isAdmin(userId)){
            userId=null;
        }
        //不是管理员，关联查询
        if(StringUtils.isNotNull(userId))
        {
            sql.append("left join role_menu rm on rm.menu_id=m.id ");
            sql.append("left join user_role ur on ur.role_id=rm.role_id ");
            where.append("and ur.user_id=:userId");
            params.put("userId",userId);
        }
        if (StringUtils.isNotEmpty(menu.getMenuName())){
            where.append("and m.menu_name like :menuName");
            params.put("menuName","%"+menu.getMenuName()+"%");
        }
        if (StringUtils.isNotEmpty(menu.getStatus())){
            where.append("and m.status=:status");
            params.put("status",menu.getStatus());
        }
        if (orderNum){
            where.append(" order by m.order_num");
        }
        return sql.append(where).toString();
    }

    @Override
    public String checkMenuNameUnique(Menu menu) {
        //添加或者更新
        Long menuId=StringUtils.isNull(menu.getId())?1L:menu.getId();
        //同级目录下是否有同名
        Menu menu1 = menuDao.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        //判断是否有同名，更新数据时判断这个同名数据是否是需要更新的数据项本身
        if (StringUtils.isNotNull(menu1)&&menuId.longValue()!=menu1.getId()){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public Integer addMenu(Menu menu) {
        menu.setCreateTime(new Date());
        Menu save = menuDao.save(menu);
        return StringUtils.isNotNull(save)?1:0;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(Menu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().longValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().longValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType()) && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().longValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }
    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(Menu menu) {
        return menu.getParentId().longValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType()) && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(Menu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(Menu menu) {
        return menu.getParentId().longValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }
    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
    }

}
