package com.riven.service.impl;

import com.riven.dao.RoleMenuDao;
import com.riven.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public Set<String> selectPermissions(Long userId) {
        Set<String> permissions = roleMenuDao.selectPermissions(userId);
        return permissions;
    }
}
