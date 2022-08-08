package com.riven.service.permission;
import com.riven.model.LoginUser;
import com.riven.util.SecurityUtils;
import com.riven.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("ss")
public class PermissionService {
    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    public boolean hasPermi(String permisson){
        if (StringUtils.isEmpty(permisson)){
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Set<String> permissions = loginUser.getPermissions();
        if (StringUtils.isNull(loginUser)&&StringUtils.isEmpty(permissions)){
            return false;
        }
        return hasPermissions(permissions,permisson);
    }
    public boolean hasPermissions(Set<String> permissions,String permission){
        return permissions.contains(StringUtils.trim(permission))||permissions.contains(ALL_PERMISSION);
    }
}
