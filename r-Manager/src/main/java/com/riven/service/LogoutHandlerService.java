package com.riven.service;

import com.alibaba.fastjson2.JSON;

import com.riven.model.LoginUser;
import com.riven.service.token.TokenService;
import com.riven.util.AjaxResult;
import com.riven.util.HttpStatus;
import com.riven.util.ServletUtils;
import com.riven.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录处理
 */
@Component
public class LogoutHandlerService implements LogoutSuccessHandler {
    @Resource
    private TokenService tokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if(StringUtils.isNotNull(loginUser)){
            //删除用户缓存
            tokenService.deleteLoginUser(loginUser);
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS,"退出成功")));
    }
}
