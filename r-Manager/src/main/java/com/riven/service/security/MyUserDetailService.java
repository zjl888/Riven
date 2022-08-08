package com.riven.service.security;
import com.riven.model.LoginUser;
import com.riven.model.User;
import com.riven.service.RoleMenuService;
import com.riven.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 扩展用户信息获取方式
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;
    @Resource
    private RoleMenuService roleMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.select(username);
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        //自定义抛出异常
        if (user==null){
            throw new UsernameNotFoundException("用户不存在!");
        }
        //简单权限，测试使用
        //SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getEx());
        //创建UserDetails对象返回给认证提供者
        return new LoginUser(user.getId().toString(),user,roleMenuService.selectPermissions(user.getId()));
                //User(username,passwordEncoder.encode(user.getPassword()), Collections.singleton(authority));
    }
}
