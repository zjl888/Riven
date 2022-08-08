package com.riven.dao;
import com.riven.model.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Set;

public interface RoleMenuDao extends JpaRepository<RoleMenu,Long> {
    @Transactional
    @Query(value = "select m.perms from Menu m left join RoleMenu rm on rm.menuId=m.id left join userRole ur on ur.roleId=rm.roleId left join User u on u.id=ur.userId where u.id=:userId")
    public Set<String> selectPermissions(Long userId);
}
