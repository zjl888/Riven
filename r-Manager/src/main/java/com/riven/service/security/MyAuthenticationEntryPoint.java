package com.riven.service.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理类
 * 捕获异常
 */
@Service
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String,String> map=new HashMap<>();
        if(authException instanceof UsernameNotFoundException){//捕获自定义的UsernameNotFoundException异常，未能在库中找到相同用户名，该用户不存在
            response.setStatus(401);
            map.put("msg","用户不存在");
            response.getWriter().print(map);
        }else if (authException instanceof BadCredentialsException){//捕获BadCredentialsException异常，密码错误
            response.setStatus(401);
            map.put("msg","密码错误");
            response.getWriter().print(map);
        }else if(authException instanceof Exception){
            map.put("msg",authException.getMessage());
            response.getWriter().print(map);
        }

    }
}
