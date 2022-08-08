package com.riven.service;
import com.riven.model.Test;
import com.riven.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    User select(String username);
    Boolean register(User user);
    List<User> selectAll();
    void insert(List<Test> testList);
}
