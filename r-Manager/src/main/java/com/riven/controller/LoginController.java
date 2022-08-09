package com.riven.controller;
import com.riven.constant.Constants;
import com.riven.model.LoginBody;
import com.riven.model.RegisterBody;
import com.riven.model.User;
import com.riven.service.LoginUserService;
import com.riven.service.RegisterService;
import com.riven.service.UserService;
import com.riven.util.AjaxResult;
import com.riven.util.RedisCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private LoginUserService loginUserService;
    @Resource
    private UserService userService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RegisterService registerService;

    /**
     * 登录
     * @param loginBody
     * @return
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody){
        AjaxResult ajaxResult=new AjaxResult();
        //返回token
        String token = loginUserService.login(loginBody.getUsername(), loginBody.getPassword());
        //User user = (User)authenticate.getPrincipal();
        //判断是否认证成功
        //System.out.println(authenticate.isAuthenticated());
        ajaxResult.put(Constants.TOKEN,token);
        ajaxResult.put("msg","success");
        return ajaxResult;
    }

    /**
     * 注册
     * @param registerBody
     * @return
     */
    @PostMapping ("/register")
    public String register(@RequestBody RegisterBody registerBody){
        String msg = registerService.register(registerBody);
        return msg;
    }
    @RequestMapping ("/test")
    public String test(){
        System.out.println(111);
        System.out.println(2222);
        String token = redisCache.getCacheObject("token");
        return token;
    }
    @PostMapping("/save")
    public String save(User user){
        userService.save(user);
        return "成功";
    }
    @PostMapping("/select")
    public User select(String username){
        return userService.select(username);

    }
}
