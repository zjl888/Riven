package com.riven.dao;
import com.riven.core.dao.BaseDao;
import com.riven.model.Menu;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface MenuDao extends BaseDao<Menu, Serializable> {
    @Query(value = "from Menu m where m.menuType in ('M','C') and m.status='0' order by m.orderNum,m.parentId")
    public List<Menu> selectAllMenu();
    //select distinct m.*
    @Query(value = " from Menu m left join RoleMenu rm on m.id=rm.menuId left join Role r on r.roleId=rm.roleId left join userRole ur on ur.roleId=r.roleId left join User u on u.id=ur.userId where u.id=:userId and m.status='0' and m.menuType in ('M','C') order by m.orderNum,m.parentId"
    )
    public List<Menu> selectMenuByUserId(Long userId);
    @Query(value = "select * from z_Menu  where menu_Name=:menuName and parent_id=:parentId limit 1",nativeQuery = true)
    public Menu checkMenuNameUnique(String menuName,Long parentId);
}
