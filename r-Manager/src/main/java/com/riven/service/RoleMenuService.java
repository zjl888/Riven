package com.riven.service;

import java.util.Set;

public interface RoleMenuService {
    Set<String> selectPermissions(Long userId);
}
