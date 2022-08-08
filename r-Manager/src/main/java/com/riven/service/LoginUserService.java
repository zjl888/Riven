package com.riven.service;

import com.riven.model.LoginUser;
import com.riven.service.token.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录验证类
 */
@Component
public class LoginUserService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenService tokenService;
    public String login(String username,String password){
        //创建用户名密码令牌，将令牌传递给认证管理器进行认证
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //认证成功，创建登录用户。
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //生成并返回token
        return tokenService.createToken(loginUser);

    }
}
