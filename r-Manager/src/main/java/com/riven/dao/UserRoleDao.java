package com.riven.dao;
import com.riven.model.userRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleDao extends JpaRepository<userRole,Long> {
}
