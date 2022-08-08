package com.riven.dao;
import com.riven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserDao extends JpaRepository<User,Long> {
    @Transactional
    @Query(value = "from User where username=:username")
    public User select(String username);

}
