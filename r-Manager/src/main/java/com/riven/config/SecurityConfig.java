package com.riven.config;
import com.riven.service.LogoutHandlerService;
import com.riven.service.security.JwtDecoderFilter;
import com.riven.service.security.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;


/**
 * security配置
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MyAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtDecoderFilter jwtDecoderFilter;
    @Autowired
    private LogoutHandlerService logoutHandlerService;
    @Autowired
    private CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*       //以下五步是表单登录进行身份认证最简单的登陆环境
        http.formLogin() //表单登陆 1
                .and() //2
                .authorizeRequests() //下面的都是授权的配置 3
                .anyRequest() //任何请求 4
                .authenticated(); //访问任何资源都需要身份认证 5*/
        http.csrf()
                .disable()//关闭csrf功能
                .authorizeRequests()//所有请求对象
                .antMatchers("/login","/register")//配置login路径
                .anonymous()//允许匿名访问
                // 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                .anyRequest()//所有请求
                .authenticated();//需要认证才能访问
        //使用token，所以禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //异常处理端点
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        //添加filter
        http.addFilterBefore(jwtDecoderFilter, UsernamePasswordAuthenticationFilter.class);
        //添加退出登录filter
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandlerService);
        //添加跨域过滤器
        http.addFilterBefore(corsFilter,JwtDecoderFilter.class);
        http.addFilterBefore(corsFilter, LogoutFilter.class);

    }

    /**
     * 声明认证管理器bean：
     * security的登录关键类是AuthenticationManager，默认使用的是ProviderManager
     * ProviderManager在ioc运行时注册的bean，无法在编译时声明注入controller，所以将其在
     * 编译器声明为bean，添加bean注解。
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 密码加密器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 把MyUserDetailService配置给认证管理器
     * 认证管理器在进行认证时会调用UserDetailsService的实现类MyUserDetailService重写的loadUserByUsername方法
     * 把客户端传递的用户名赋给username参数，我们根据此参数调用用户信息填充UserDetails对象返回即可。
     * 真正做密码匹配的是DaoAuthenticationProvider，它是认证管理器AuthenticationManager的子类。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义DaoAuthenticationProvider时，无需在此配置，注释掉
        /*auth.userDetailsService(userDetailsService).passwordEncoder(this.passwordEncoder());*/
        super.configure(auth);
    }

    /**
     * 自定义DaoAuthenticationProvider（主要认证类）
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //把MyUserDetailService配置给认证管理器
        //
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        //关闭隐藏用户名不存在错误,否则会抛Bad credentials异常
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

}
