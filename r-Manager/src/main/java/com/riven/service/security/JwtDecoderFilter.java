package com.riven.service.security;
import com.riven.model.LoginUser;
import com.riven.service.token.TokenService;
import com.riven.util.SecurityUtils;
import com.riven.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token解析filter
 * 解析请求头中携带的token
 */
@Service
public class JwtDecoderFilter extends OncePerRequestFilter {
    @Resource
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*        //获取请求头中的token
        String authorization = request.getHeader("Authorization");
        if (authorization!=null){
            boolean verified = tokenService.Verified(authorization);
            if (verified){
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("zjl", "zjl123", Collections.singleton(authority));
                //将认证通过的信息填充到安全上下文中（用于一次请求的授权）
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }*/
        //从请求头中获取token，解析出其中的uuid，拼接获取key从缓存中取出loginUser
        LoginUser loginUser = tokenService.getLoginUser(request);
        //判断缓存中是否存在loginUser，若不在则登录失败
        if (StringUtils.isNotNull(loginUser)&& StringUtils.isNull(SecurityUtils.getAuthentication())) {
            //检验token是否过期
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //将认证通过的信息填充到安全上下文中（用于一次请求的授权）
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
