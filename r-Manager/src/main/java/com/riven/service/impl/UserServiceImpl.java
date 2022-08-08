package com.riven.service.impl;
import com.riven.dao.TestDao;
import com.riven.dao.UserDao;
import com.riven.model.Test;
import com.riven.model.User;
import com.riven.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private TestDao testDao;
    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User select(String username) {
        return userDao.select(username);
    }

    @Override
    public Boolean register(User user) {
        return userDao.save(user)!=null;
    }

    @Override
    public List<User> selectAll() {
        return userDao.findAll();
    }

    @Override
    public void insert(List<Test> testList) {
        testDao.saveAllAndFlush(testList);
    }
}
