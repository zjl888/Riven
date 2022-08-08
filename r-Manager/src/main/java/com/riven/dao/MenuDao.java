package com.riven.dao;
import com.riven.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu,Long> {
}
