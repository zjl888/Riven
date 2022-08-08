package com.riven.dao;
import com.riven.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author :zhujl
 * @date : 2022/8/5
 */
public interface TestDao extends JpaRepository<Test,Integer> {
}
